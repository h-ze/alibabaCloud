package com.hz.server.service;

import java.io.InputStream;

public interface OssService {

    String initMultiPartUpload(String fileName, String filePath);

    int upload(String objectName, String upload, InputStream inputStream, Integer curPartSize, Integer partNumber);

    int completeMultipartUpload(String objectName, String uploadId);

    String getUrl(String ossKey);

    InputStream getFileStream(String ossKey);

    void delete(String ossKey);

    String doesObjectExist(String ossKey);


}
