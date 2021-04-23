package com.neu.video_recognize.service.invoke;


import java.io.IOException;

public interface InvokeService {

    void invokeAlgorithm(Integer FileId) throws IOException;

    int insertRecord(Integer uId, String token);
}
