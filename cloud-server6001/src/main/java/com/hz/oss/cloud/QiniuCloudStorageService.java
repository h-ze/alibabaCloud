
package com.hz.oss.cloud;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.common.exception.RRException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

/**
 * 七牛云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
public class QiniuCloudStorageService extends CloudStorageService {
    private UploadManager uploadManager;
    private String token;

    public QiniuCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).
                uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new RRException("上传文件失败，请核对七牛配置信息", e);
        }

        return config.getQiniuDomain() + "/" + path;
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
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(byte[] data, long size, String suffix) {
        return null;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
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
