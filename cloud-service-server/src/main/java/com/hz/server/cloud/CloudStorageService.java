package com.hz.server.cloud;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.hz.web.utils.DateUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 云存储(支持七牛、阿里云、腾讯云、又拍云)
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class CloudStorageService {

    /** 云存储配置信息 */
    CloudStorageConfig config;

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;

        if(StringUtils.isNotBlank(prefix)){
            path = prefix + "/" + path;
        }

        return path + suffix;
    }

    /**
     * 文件上传
     * @param data    文件字节数组
     * @param path    文件路径，包含文件名
     * @return        返回http地址
     */
    public abstract String upload(byte[] data, String path);

    /**
     * 文件上传
     * @param data     文件字节数组
     * @param suffix   后缀
     * @return         返回http地址
     */
    public abstract String uploadSuffix(byte[] data, String suffix);

    public abstract String uploadSuffix(byte[] data,long size, String suffix);

    /**
     * 文件上传
     * @param inputStream   字节流
     * @param path          文件路径，包含文件名
     * @return              返回http地址
     */
    public abstract String upload(InputStream inputStream, String path);

    /**
     * 文件上传
     * @param inputStream  字节流
     * @param suffix       后缀
     * @return             返回http地址
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix);


    /**
     * 创建存储空间
     * @param bucketName 存储空间
     * @return
     */
    public abstract String createBucketName(String bucketName);

    /**
     * 删除存储空间buckName
     */
    public abstract void deleteBucket();

    /**
     * 创建模拟文件夹
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
    public abstract String createFolder(String folder);

    /**
     * 根据key删除OSS服务器上的文件
     * @param folder  模拟文件夹名 如"qj_nanjing/"
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public abstract String deleteFile(String bucketName,String folder, String key);

    /**
     * 上传图片至OSS
     * @param folder 模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */
    public abstract String uploadObject2OSS(File file,String folder);

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public abstract String getContentType(String fileName);


    /**
     * 设置存储空间的读写权限。例如将examplebucket的读写权限ACL设置为私有Private。
     *  * 私有 	存储空间的拥有者和授权用户有该存储空间内的文件的读写权限，其他用户没有权限操作该存储空间内的文件。 	CannedAccessControlList.Private
     *  * 公共读 	存储空间的拥有者和授权用户有该存储空间内的文件的读写权限，其他用户只有该存储空间内的文件的读权限。请谨慎使用该权限。 	CannedAccessControlList.PublicRead
     *  * 公共读写 	所有用户都有该存储空间内的文件的读写权限。请谨慎使用该权限。 	CannedAccessControlList.PublicReadWrite
     * @param bucketName
     * @return
     */
    public abstract String setBucketAcl(String bucketName);

    /**
     * 设置文件访问权限
     * @param bucketName
     * @param objectName
     * @return
     */
    public abstract String setFileAcl(String bucketName,String objectName);

    /**
     * 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
     * @return
     */
    public abstract String setClientConfiguration();


    /**
     * 根据文件名下载文件
     * @param objectName
     * @return
     */
    public abstract String downloadFile(String objectName);

    public abstract URL getDownloadPath(String bucketName, String key);


    /**
     * 列举当前账号下的所有存储空间。
     * @return
     */
    public abstract List<Bucket> getAllBucketList();


    /**
     * 列举指定前缀的存储空间。
     * @param prefix 前缀
     * @return
     */
    public abstract List<Bucket> getBucketListByPrefix(String prefix);

    //
    //

    /**
     * 列举指定marker之后的存储空间。
     * @param marker
     * @return 填写marker。从marker之后按字母排序的第一个开始返回存储空间。
     */
    public abstract List<Bucket> getBucketListByMarker(String marker);

    /**
     * 限定此次列举存储空间的最大个数为500。默认值为100，最大值为1000。
     * @param number
     * @return
     */
    public abstract List<Bucket> getBucketListMax(Integer number);

    /**
     * 获取存储空间的信息（Info），包括存储空间所在地域、创建日期等。
     * @param bucketName
     * @return
     */
    public abstract BucketInfo getBucketInfo(String bucketName);

    /**
     * 列举examplebucket存储空间下的文件。默认列举100个文件。
     * @param bucketName
     * @return
     */
    public abstract List getFileList(String bucketName);

    /**
     * 获取存储空间所在的地域（称为Region或Location）：
     * @param bucketName
     * @return
     */
    public abstract String getLocation(String bucketName);

    public abstract void setBucketTag(String bucketName);

    /**
     * prefix 	限定返回的文件必须以prefix作为前缀。 	setPrefix(String prefix)
     * delimiter 	对文件名称进行分组的一个字符。所有名称包含指定的前缀且第一次出现delimiter字符之间的文件作为一组元素（commonPrefixes）。 	setDelimiter(String delimiter)
     * maxKeys 	限定此次列举文件的最大个数。默认值为100，最大值为1000。 	setMaxKeys(Integer maxKeys)
     * startAfter 	此次列举文件的起点。 	setStartAfter(String startAfter)
     * continuationToken 	此次列举文件使用的continuationToken。 	setContinuationToken(String continuationToken)
     * encodingType 	请求响应体中文件名称采用的编码方式，目前仅支持URL。 	setEncodingType(String encodingType)
     * fetchOwner 	本次列举结果中是否包含文件的owner的信息。 	setFetchOwner(boolean fetchOwner )
     *
     * 列举文件
     */
    public abstract List getListFile(String bucketName,String keyPrefix) throws UnsupportedEncodingException;


    /**
     * 批量删除文件
     * @param bucketName
     */
    public abstract void deleteFileList(String bucketName);


    /**
     * 拷贝文件
     * @param sourceBucketName 填写源Bucket名称
     * @param sourceKey 填写源Object的完整路径。Object完整路径中不能包含Bucket名称。
     * @param destinationBucketName 填写与源Bucket处于同一地域的目标Bucket名称。
     * @param destinationKey 填写目标Object的完整路径。Object完整路径中不能包含Bucket名称。
     */
    public abstract void copyFile(String sourceBucketName,String sourceKey,String destinationBucketName,String destinationKey);


    /**
     * 分片拷贝文件 禁止覆盖同名文件
     * @param sourceBucketName 填写源Bucket名称
     * @param sourceKey 填写源Object的完整路径。Object完整路径中不能包含Bucket名称。
     * @param destinationBucketName 填写与源Bucket处于同一地域的目标Bucket名称。
     * @param destinationKey 填写目标Object的完整路径。Object完整路径中不能包含Bucket名称。
     */
    public abstract void splitCopyFile(String sourceBucketName,String sourceKey,String destinationBucketName,String destinationKey);

    /**
     * 分片上传时禁止覆盖同名文件
     * @param bucketName
     * @param objectName
     */
    public abstract void splitUpload(String bucketName,String objectName);
}
