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
    public boolean requestInvokePermission(Integer uId) {
        User u = userMapper.selectByPrimaryKey(uId);
        if (u.getInvokeTime() >= u.getTotalInvokeTime()){
            return false;
        }else{
            // 将用户调用次数加一
            User updateInfo = new User();
            updateInfo.setId(uId);
            updateInfo.setInvokeTime(u.getInvokeTime() + 1);
            return true;
        }
    }

    @Override
    @Async
    public void invokeAlgorithm(Integer fileId) throws IOException {
        // 获取file对象，找到视频存储的绝对路径
        File f = fileMapper.selectByPrimaryKey(fileId);
        String absolutePath = videoPath + "/" + f.getFilePath();

        // 更新file对象的recognize_time，重置识别结果
        File updateInfo = new File();
        updateInfo.setId(fileId);
        updateInfo.setRecognizeResult("");
        updateInfo.setLastRecognizeTime(new Date(System.currentTimeMillis()));
        fileMapper.updateByPrimaryKeySelective(updateInfo);

        // 开始识别
        CommandLine cmdLine = new CommandLine(pythonPath);
        cmdLine.addArgument(scriptPath);
        cmdLine.addArgument(absolutePath);
        DefaultExecutor executor = new DefaultExecutor();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        executor.setStreamHandler(streamHandler);
        executor.execute(cmdLine);

        String rs = outputStream.toString();
        // 将识别结果保存到数据库
        updateInfo = new File();
        updateInfo.setId(fileId);
        updateInfo.setRecognizeResult(rs);
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
}
