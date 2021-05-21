package com.neu.video_recognize.service.invoke;

import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.po.InvokeRecord;
import com.neu.video_recognize.entity.po.User;
import com.neu.video_recognize.mapper.FileMapper;
import com.neu.video_recognize.mapper.InvokeRecordMapper;
import com.neu.video_recognize.mapper.UserMapper;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public class CommandInvoke implements InvokeService{

    @Value("${script-path}")
    private String scriptPath;

    @Value("${python-path}")
    private String pythonPath;

    @Value("${upload.video}")
    private String videoPath;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private InvokeRecordMapper invokeRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean requestInvokePermission(Integer uId, int times) {
        User u = userMapper.selectByPrimaryKey(uId);
        if (u.getInvokeTime() + times > u.getTotalInvokeTime()){
            return false;
        }else{
            // 将用户调用次数加上times次
            User updateInfo = new User();
            updateInfo.setId(uId);
            updateInfo.setInvokeTime(u.getInvokeTime() + times);
            return true;
        }
    }

    public boolean isRecognizing(File f){
        long gap = System.currentTimeMillis() - f.getLastRecognizeTime().getTime();
        // 上次开始识别时间在10分钟内，且识别结果为空，说明正在识别中
        return gap < 600000 && f.getRecognizeResult().equals("");
    }

    @Override
    @Async
    public void invokeAlgorithm(File f) throws IOException {
        // 获取file对象，找到视频存储的绝对路径
        String absolutePath = videoPath + "/" + f.getFilePath();

        // 更新file对象的recognize_time，重置识别结果
       resetResultAndRecognizeTime(f);

        // 开始识别
        CommandLine cmdLine = new CommandLine(pythonPath);
        cmdLine.addArgument(scriptPath);
        cmdLine.addArgument(absolutePath);
        DefaultExecutor executor = new DefaultExecutor();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine);

        String rs = extractResult(outputStream.toString());
        // 将识别结果保存到数据库
        File updateInfo = new File();
        updateInfo.setId(f.getId());
        updateInfo.setRecognizeResult(rs);
        fileMapper.updateByPrimaryKeySelective(updateInfo);
    }

    private String extractResult(String output){
        String[] lines = output.split("\n");
        String rs = "";
        for (String line : lines){
            if (line.startsWith("rs:")){
                rs = line.substring(3);
                break;
            }
        }
        return rs;
    }

    public void resetResultAndRecognizeTime(File f){
        if (f.getRecognizeResult() != null && f.getRecognizeResult().equals(""))
            return;
        File updateInfo = new File();
        updateInfo.setId(f.getId());
        updateInfo.setRecognizeResult("");
        updateInfo.setLastRecognizeTime(new Date(System.currentTimeMillis()));
        fileMapper.updateByPrimaryKeySelective(updateInfo);
    }

    @Override
    public int insertRecord(Integer uId, String token) {
        InvokeRecord record = new InvokeRecord();
        record.setInvokerId(uId);
        record.setAccessToken(token);
        record.setIsApiInvoke(token != null);
        record.setInvokeTime(new Date(System.currentTimeMillis()));
        return invokeRecordMapper.insert(record);
    }

    @Override
    public List<InvokeRecord> getInvokeRecordsBy(Integer uId) {
        return invokeRecordMapper.selectByUserId(uId);
    }

    @Override
    public void invokeAll(List<Integer> fileIds, List<String> failed, List<String> succeed) throws IOException {
        for (Integer id : fileIds){
            File f = fileMapper.selectByPrimaryKey(id);
            if (isRecognizing(f)){
                failed.add(f.getName());
            }else{
                succeed.add(f.getName());
                resetResultAndRecognizeTime(f);
                invokeAlgorithm(f);
            }
        }
    }
}
