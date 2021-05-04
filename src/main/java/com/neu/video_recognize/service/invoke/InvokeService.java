package com.neu.video_recognize.service.invoke;


import com.neu.video_recognize.entity.po.InvokeRecord;

import java.io.IOException;
import java.util.List;

public interface InvokeService {

    boolean requestInvokePermission(Integer uId);

    void invokeAlgorithm(Integer FileId) throws IOException;

    int insertRecord(Integer uId, String token);

    List<InvokeRecord> getInvokeRecordsBy(Integer uId);
}
