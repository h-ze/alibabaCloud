package com.common.entity;

import java.util.Date;

public class UserFile {
    private Integer id;
    private String oldFileName;
    private String newFileName;
    private String ext;
    private String path;
    private String size;
    private String type;
    private String isImg;
    private Integer downcounts;
    private Date uploadTime;
    private Integer userId;

    public UserFile() {
    }

    public UserFile(Integer id, String oldFileName, String newFileName, String ext, String path, String size, String type, String isImg, Integer downcounts, Date uploadTime, Integer userId) {
        this.id = id;
        this.oldFileName = oldFileName;
        this.newFileName = newFileName;
        this.ext = ext;
        this.path = path;
        this.size = size;
        this.type = type;
        this.isImg = isImg;
        this.downcounts = downcounts;
        this.uploadTime = uploadTime;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsImg() {
        return isImg;
    }

    public void setIsImg(String isImg) {
        this.isImg = isImg;
    }

    public Integer getDowncounts() {
        return downcounts;
    }

    public void setDowncounts(Integer downcounts) {
        this.downcounts = downcounts;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserFile{" +
                "id=" + id +
                ", oldFileName='" + oldFileName + '\'' +
                ", newFileName='" + newFileName + '\'' +
                ", ext='" + ext + '\'' +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                ", type='" + type + '\'' +
                ", isImg='" + isImg + '\'' +
                ", downcounts=" + downcounts +
                ", uploadTime=" + uploadTime +
                ", userId='" + userId + '\'' +
                '}';
    }
}
