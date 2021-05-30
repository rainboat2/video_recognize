package com.neu.video_recognize.entity.vo;

import java.util.List;

public class DeleteFileListWrapper {

    List<Integer> fileIds;

    List<Integer> directoryIds;

    public List<Integer> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<Integer> fileIds) {
        this.fileIds = fileIds;
    }

    public List<Integer> getDirectoryIds() {
        return directoryIds;
    }

    public void setDirectoryIds(List<Integer> directoryIds) {
        this.directoryIds = directoryIds;
    }
}
