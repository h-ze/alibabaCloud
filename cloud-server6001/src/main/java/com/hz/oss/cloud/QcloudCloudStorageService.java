/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.hz.oss.cloud;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.common.exception.RRException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * 腾讯云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
public class QcloudCloudStorageService extends CloudStorageService {
    private COSClient client;

    public QcloudCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
    	Credentials credentials = new Credentials(config.getQcloudAppId(), config.getQcloudSecretId(),
                config.getQcloudSecretKey());
    	
    	//初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        clientConfig.setRegion(config.getQcloudRegion());
        
    	client = new COSClient(clientConfig, credentials);
    }

    @Override
    public String upload(byte[] data, String path) {
        //腾讯云必需要以"/"开头
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        
        //上传到腾讯云
        UploadFileRequest request = new UploadFileRequest(config.getQcloudBucketName(), path, data);
        String response = client.uploadFile(request);

        JSONObject jsonObject = JSONObject.parseObject(response);

        if(jsonObject.getInteger("code") != 0) {
            throw new RRException("文件上传失败，" + jsonObject.getString("message"));
        }

        return config.getQcloudDomain() + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
    	try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RRException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(byte[] data, long size, String suffix) {
        return null;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String createBucketName(String bucketName) {
        return null;
    }

    @Override
    public void deleteBucket() {

    }

    @Override
    public String createFolder(String folder) {
        return null;
    }

    @Override
    public String deleteFile(String bucketName,String folder, String key) {
        return null;
    }

    @Override
    public String uploadObject2OSS(File file, String folder) {
        return null;
    }


    @Override
    public String getContentType(String fileName) {
        return null;
    }

    @Override
    public String setBucketAcl(String bucketName) {
        return null;
    }

    @Override
    public String setFileAcl(String bucketName, String objectName) {
        return null;
    }

    @Override
    public String setClientConfiguration() {
        return null;
    }

    @Override
    public String downloadFile(String objectName) {
        return null;
    }

    @Override
    public URL getDownloadPath(String bucketName, String key) {
        return null;
    }

    @Override
    public List<Bucket> getAllBucketList() {
        return null;
    }

    @Override
    public List<Bucket> getBucketListByPrefix(String prefix) {
        return null;
    }

    @Override
    public List<Bucket> getBucketListByMarker(String marker) {
        return null;
    }

    @Override
    public List<Bucket> getBucketListMax(Integer number) {
        return null;
    }

    @Override
    public BucketInfo getBucketInfo(String bucketName) {
        return null;
    }

    @Override
    public List getFileList(String bucketName) {
        return null;
    }

    @Override
    public String getLocation(String bucketName) {
        return null;
    }

    @Override
    public void setBucketTag(String bucketName) {

    }

    @Override
    public List getListFile(String bucketName, String keyPrefix) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public void deleteFileList(String bucketName) {

    }

    @Override
    public void copyFile(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {

    }

    @Override
    public void splitCopyFile(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {

    }

    @Override
    public void splitUpload(String bucketName, String objectName) {

    }
}
