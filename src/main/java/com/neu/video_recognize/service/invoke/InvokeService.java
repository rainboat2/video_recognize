package com.neu.video_recognize.service.invoke;


import com.neu.video_recognize.entity.po.File;
import com.neu.video_recognize.entity.po.InvokeRecord;

import java.io.IOException;
import java.util.List;

public interface InvokeService {

    boolean requestInvokePermission(Integer uId, int times);

    void invokeAlgorithm(File f) throws IOException;

    boolean isRecognizing(File f);

    void resetResultAndRecognizeTime(File f);

    int insertRecord(Integer uId, String token);

    List<InvokeRecord> getInvokeRecordsBy(Integer uId);

    void invokeAll(List<Integer> fileIds, List<String> failed, List<String> succeed) throws IOException;

}
