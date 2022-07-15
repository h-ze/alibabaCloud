package com.hz.service.impl;

import com.hz.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service("ossService")
public class OssServiceImpl implements OssService {

    private final static Logger logger =LoggerFactory.getLogger(OssServiceImpl.class);



    @Override
    public String initMultiPartUpload(String fileName, String filePath) {

        return null;
    }

    @Override
    public int upload(String objectName, String upload, InputStream inputStream, Integer curPartSize, Integer partNumber) {
        return 0;
    }

    @Override
    public int completeMultipartUpload(String objectName, String uploadId) {
        return 0;
    }

    @Override
    public String getUrl(String ossKey) {
        return null;
    }

    @Override
    public InputStream getFileStream(String ossKey) {
        return null;
    }

    @Override
    public void delete(String ossKey) {

    }

    @Override
    public String doesObjectExist(String ossKey) {
        return null;
    }
}
