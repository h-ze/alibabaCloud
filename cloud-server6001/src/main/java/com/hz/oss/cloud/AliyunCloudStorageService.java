package com.hz.oss.cloud;

import com.aliyun.oss.*;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.*;
import com.common.exception.RRException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 阿里云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
public class AliyunCloudStorageService extends CloudStorageService {

    private static final Logger logger =LoggerFactory.getLogger(AliyunCloudStorageService.class);

    //oss2.5.0版本
    //private OSSClient hz.client;

    private OSS client;

    public AliyunCloudStorageService(CloudStorageConfig config){
        this.config = config;

        //初始化
        init();
    }

    private void init(){

        // 创建ClientConfiguration实例，您可以根据实际情况修改默认参数。3.10版本
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置是否支持CNAME。CNAME用于将自定义域名绑定到目标Bucket。
        conf.setSupportCname(true);
        client = new OSSClientBuilder().build(config.getAliyunEndPoint(),
                config.getAliyunAccessKeyId(),
                config.getAliyunAccessKeySecret(),
                conf);

        // 创建OSSClient实例。2.5.0版本
        //hz.client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(), config.getAliyunAccessKeySecret());


        // 关闭OSSClient。
        //hz.client.shutdown();
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }






    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            client.putObject(config.getAliyunBucketName(), path, inputStream);
        } catch (Exception e){
            throw new RRException("上传文件失败，请检查配置信息", e);
        }

        return config.getAliyunDomain() + "/" + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(byte[] data, long size, String suffix) {
        logger.info("suffix长度为: {}",suffix);
        String resultStr = null;
        try {

          /*  // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getAliyunBucketName(), suffix, new ByteArrayInputStream(data));

            // 指定上传文件操作时是否覆盖同名Object。
            // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object。
            // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
            // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setHeader("x-oss-forbid-overwrite", "true");
            putObjectRequest.setMetadata(metadata);

            // 上传文件。
            hz.client.putObject(putObjectRequest);*/



            //以输入流的形式上传文件
            InputStream is = new ByteArrayInputStream(data);
            //InputStream is = new FileInputStream(data);
            //文件名
            String fileName = suffix;
            //文件大小
            long fileSize = size;
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();

            // 指定上传文件操作时是否覆盖同名Object。
            // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object。
            // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
            // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
            metadata.setHeader("x-oss-forbid-overwrite", "true");

            //上传的文件的长度
            metadata.setContentLength(data.length);
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = client.putObject(config.getAliyunBucketName(), suffix, is, metadata);

            //String location = hz.client.getBucketLocation(config.getAliyunBucketName());

            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

    /**
     * 创建存储空间
     * @param bucketName 存储空间
     * @return
     */
    public String createBucketName(String bucketName){
        //存储空间
        //判断存储空间examplebucket是否存在。如果返回值为true，则存储空间存在，如果返回值为false，则存储空间不存在。
        boolean doesBucketExist = client.doesBucketExist(bucketName);
        logger.info("创建存储空间: {}",doesBucketExist);
        if(!doesBucketExist){

            // 创建CreateBucketRequest对象。
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);

            createBucketRequest.setBucketName(bucketName);
            // 如果创建存储空间的同时需要指定存储类型和数据容灾类型, 请参考如下代码。
            // 此处以设置存储空间的存储类型为标准存储为例介绍。
            //createBucketRequest.setStorageClass(StorageClass.Standard);
            // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
            //createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
            // 设置存储空间的权限为公共读，默认为私有。
            //createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);

            //创建存储空间
            //Bucket bucket=hz.client.createBucket(bucketName);
            Bucket bucket = client.createBucket(createBucketRequest);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketName;
    }

    /**
     * 删除存储空间buckName
     */
    @Override
    public void deleteBucket(){
        client.deleteBucket(config.getAliyunBucketName());
        logger.info("删除" + config.getAliyunBucketName() + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
    public String createFolder(String folder){
        //文件夹名
        //判断文件夹是否存在，不存在则创建
        if(!client.doesObjectExist(config.getAliyunBucketName(), folder)){
            //创建文件夹
            client.putObject(config.getAliyunBucketName(), folder, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = client.getObject(config.getAliyunBucketName(), folder);
            return object.getKey();
        }
        return folder;
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param folder  模拟文件夹名 如"qj_nanjing/"
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public String deleteFile(/*OSSClient ossClient, String bucketName, */String bucketName,String folder, String key){

        try{

            client.deleteObject(bucketName, folder + key);

        }catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return "删除失败，"+oe.getErrorMessage();
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the hz.client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return "删除失败，"+ce.getMessage();
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }finally {
            /*if (hz.client != null) {
                hz.client.shutdown();
            }*/
        }
        logger.info("删除" + config.getAliyunBucketName() + "下的文件" + folder + key + "成功");
        return "删除" + config.getAliyunBucketName() + "下的文件" + folder + key + "成功";
    }

    /**
     * 上传图片至OSS
     * @param file 上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param folder 模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */
    public  String uploadObject2OSS(File file,  String folder) {
        String resultStr = null;
        try {
            //以输入流的形式上传文件
            InputStream is = new FileInputStream(file);
            //文件名
            String fileName = file.getName();
            //文件大小
            long fileSize = file.length();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = client.putObject(config.getAliyunBucketName(), folder + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public  String getContentType(String fileName){
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }

    @Override
    public String setBucketAcl(String bucketName) {
        // 设置存储空间的读写权限。例如将examplebucket的读写权限ACL设置为私有Private。
        client.setBucketAcl(bucketName, CannedAccessControlList.Private);
        return null;
    }

    @Override
    public String setFileAcl(String bucketName, String objectName) {
        // 设置文件的访问权限为公共读。
        client.setObjectAcl(bucketName, objectName, CannedAccessControlList.PublicRead);


        return null;
    }

    @Override
    public String setClientConfiguration() {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();

        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(10000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(5);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
        conf.setSupportCname(true);
        // 设置是否开启二级域名的访问方式，默认不开启。
        conf.setSLDEnabled(true);
        // 设置连接OSS所使用的协议（HTTP或HTTPS），默认为HTTP。
        conf.setProtocol(Protocol.HTTP);
        // 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
        conf.setUserAgent("aliyun-sdk-java");
        // 设置代理服务器端口。
        conf.setProxyHost("<yourProxyHost>");
        // 设置代理服务器验证的用户名。
        conf.setProxyUsername("<yourProxyUserName>");
        // 设置代理服务器验证的密码。
        conf.setProxyPassword("<yourProxyPassword>");
        // 设置是否开启HTTP重定向，默认开启。
        conf.setRedirectEnable(true);
        // 设置是否开启SSL证书校验，默认开启。
        conf.setVerifySSLEnable(true);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(), config.getAliyunAccessKeySecret(), conf);
        return null;
    }

    @Override
    public String downloadFile(String objectName) {
        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            OSSObject ossObject = client.getObject(config.getAliyunBucketName(), objectName);
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            if (content != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    System.out.println("\n" + line);
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                content.close();
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the hz.client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
        }finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return null;
    }

    @Override
    public URL getDownloadPath(String bucketName, String key) {
        // 设置签名URL过期时间为3600秒（1小时）。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        URL url = client.generatePresignedUrl(bucketName, key, expiration);
        return url;
    }

    public List<Bucket> getAllBucketList(){
        // 列举当前账号下的所有存储空间。
        List<Bucket> buckets = client.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }
        return buckets;
    }

    @Override
    public List<Bucket> getBucketListByPrefix(String prefix) {

        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        // 列举指定前缀的存储空间。
        // 填写前缀。
        listBucketsRequest.setPrefix(prefix);
        BucketList bucketList = client.listBuckets(listBucketsRequest);
        for (Bucket bucket : bucketList.getBucketList()) {
            System.out.println(" - " + bucket.getName());
        }
        return bucketList.getBucketList();
    }

    @Override
    public List<Bucket> getBucketListByMarker(String marker) {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();

        // 列举指定marker之后的存储空间。
        // 填写marker。从marker之后按字母排序的第一个开始返回存储空间。
        listBucketsRequest.setMarker(marker);
        BucketList bucketList = client.listBuckets(listBucketsRequest);
        for (Bucket bucket : bucketList.getBucketList()) {
            System.out.println(" - " + bucket.getName());
        }
        return bucketList.getBucketList();
    }

    @Override
    public List<Bucket> getBucketListMax(Integer number) {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        // 限定此次列举存储空间的最大个数为500。默认值为100，最大值为1000。
        listBucketsRequest.setMaxKeys(500);
        BucketList bucketList = client.listBuckets(listBucketsRequest);
        for (Bucket bucket : bucketList.getBucketList()) {
            System.out.println(" - " + bucket.getName());
        }
        return bucketList.getBucketList();
    }

    @Override
    public BucketInfo getBucketInfo(String bucketName) {
        // 存储空间的信息包括地域（Region或Location）、创建日期（CreationDate）、拥有者（Owner）等。
        // 填写Bucket名称，例如examplebucket。
        BucketInfo info = client.getBucketInfo(bucketName);
        // 获取地域。
        info.getBucket().getLocation();
        // 获取创建日期。
        info.getBucket().getCreationDate();
        // 获取拥有者信息。
        info.getBucket().getOwner();
        // 获取容灾类型。
        info.getDataRedundancyType();
        return info;
    }

    @Override
    public List getFileList(String bucketName) {
        // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
        ObjectListing objectListing = client.listObjects(bucketName);
        // objectListing.getObjectSummaries获取所有文件的描述信息。
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }
        return objectListing.getObjectSummaries();
    }

    @Override
    public String getLocation(String bucketName) {
        String location = client.getBucketLocation(bucketName);
        System.out.println(location);
        return location;
    }

    @Override
    public void setBucketTag(String bucketName) {
        try {
            // 设置Bucket标签。
            SetBucketTaggingRequest request = new SetBucketTaggingRequest(bucketName);
            // 依次填写Bucket标签的键（例如owner）和值（例如John）。
            request.setTag("owner", "John");
            request.setTag("location", "hangzhou");
            client.setBucketTagging(request);



            // 获取Bucket标签信息。
            TagSet tagSet = client.getBucketTagging(new GenericRequest(bucketName));
            Map<String, String> tags = tagSet.getAllTags();
            for(Map.Entry tag:tags.entrySet()){
                System.out.println("key:"+tag.getKey()+" value:"+tag.getValue());
            }



            // 列举带指定标签的Bucket。
            ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
            // 依次填写Bucket标签的键（例如owner）和值（例如John）。
            listBucketsRequest.setTag("owner", "John");
            BucketList bucketList = client.listBuckets(listBucketsRequest);
            for (Bucket o : bucketList.getBucketList()) {
                System.out.println("list result bucket: " + o.getName());
            }



            // 删除Bucket标签。
            client.deleteBucketTagging(new GenericRequest(bucketName));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the hz.client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
    }

    public List getListFile(String bucketName,String keyPrefix) throws UnsupportedEncodingException {
        // 设置最大个数。
        final int maxKeys = 200;
        String nextContinuationToken = null;
        ListObjectsV2Result result = null;
        // 指定前缀，例如exampledir/object。
        //final String keyPrefix = "exampledir/object";

        // 指定返回结果使用URL编码，则您需要对结果中的prefix、delemiter、startAfter、key和commonPrefix进行URL解码。
        do {
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName).withMaxKeys(maxKeys);
            if (!StringUtils.isEmpty(keyPrefix)){
                listObjectsV2Request.setPrefix(keyPrefix);
            }
            listObjectsV2Request.setEncodingType("url");
            listObjectsV2Request.setContinuationToken(nextContinuationToken);
            result = client.listObjectsV2(listObjectsV2Request);
            List<OSSObjectSummary> objectSummaries = result.getObjectSummaries();


            // prefix解码。
            if (result.getPrefix() != null) {
                String prefix = URLDecoder.decode(result.getPrefix(), "UTF-8");
                System.out.println("prefix: " + prefix);
            }

            // delimiter解码。
            if (result.getDelimiter() != null) {
                String delimiter = URLDecoder.decode(result.getDelimiter(), "UTF-8");
                System.out.println("delimiter: " + delimiter);
            }

            // startAfter解码。
            if (result.getStartAfter() != null) {
                String startAfter = URLDecoder.decode(result.getStartAfter(), "UTF-8");
                System.out.println("startAfter: " + startAfter);
            }

            // 文件名称解码。
            for (OSSObjectSummary s : result.getObjectSummaries()) {
                String decodedKey = URLDecoder.decode(s.getKey(), "UTF-8");
                System.out.println("key: " + decodedKey);
            }

            // commonPrefixes解码。
            for (String commonPrefix: result.getCommonPrefixes()) {
                String decodeCommonPrefix = URLDecoder.decode(commonPrefix, "UTF-8");
                System.out.println("CommonPrefix:" + decodeCommonPrefix);
            }

            nextContinuationToken = result.getNextContinuationToken();

            return objectSummaries;
        } while (result.isTruncated());
    }

    @Override
    public void deleteFileList(String bucketName) {

        // 指定前缀。用于删除examplebucket中以file为前缀的文件。
        final String prefix = "file";


        // 填写不包含Bucket名称在内的目录完整路径。例如Bucket下testdir目录的完整路径为testdir/。
        //删除examplebucket中testdir目录及目录下的所有文件
        //final String prefix = "testdir/";


        // 列举所有包含指定前缀的文件并删除。
        String nextMarker = null;
        ObjectListing objectListing = null;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                    .withPrefix(prefix)
                    .withMarker(nextMarker);

            objectListing = client.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<String>();
                for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                    System.out.println("key name: " + s.getKey());
                    keys.add(s.getKey());
                }
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys).withEncodingType("url");
                DeleteObjectsResult deleteObjectsResult = client.deleteObjects(deleteObjectsRequest);
                List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
                try {
                    for(String obj : deletedObjects) {
                        String deleteObj =  URLDecoder.decode(obj, "UTF-8");
                        System.out.println(deleteObj);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());


        // 删除文件。
        // 填写需要删除的多个文件完整路径。文件完整路径中不能包含Bucket名称。
        /*List<String> keys = new ArrayList<String>();
        keys.add("exampleobjecta.txt");
        keys.add("testfolder/sampleobject.txt");
        keys.add("exampleobjectb.txt");

        DeleteObjectsResult deleteObjectsResult = hz.client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys).withEncodingType("url"));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        try {
            for(String obj : deletedObjects) {
                String deleteObj =  URLDecoder.decode(obj, "UTF-8");
                System.out.println(deleteObj);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    public void copyFile(String sourceBucketName,String sourceKey,String destinationBucketName,String destinationKey){
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        //String endpoint = "yourEndpoint";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        //String accessKeyId = "yourAccessKeyId";
        //String accessKeySecret = "yourAccessKeySecret";
        // 填写源Bucket名称。
        //String sourceBucketName = "srcexamplebucket";
        // 填写源Object的完整路径。Object完整路径中不能包含Bucket名称。
        //String sourceKey = "srcexampleobject.txt";
        // 填写与源Bucket处于同一地域的目标Bucket名称。
        //String destinationBucketName = "desexamplebucket";
        // 填写目标Object的完整路径。Object完整路径中不能包含Bucket名称。
        //String destinationKey = "desexampleobject.txt";

        // 创建OSSClient实例。
        //OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建CopyObjectRequest对象。
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey);

        // 设置新的文件元信息。
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("text/txt");
        // 指定CopyObject操作时是否覆盖同名目标Object。此处设置为true，表示禁止覆盖同名Object。
        // meta.setHeader("x-oss-forbid-overwrite", "true");
        // 指定拷贝的源地址。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_SOURCE, "/examplebucket/recode-test.txt");
        // 如果源Object的ETag值和您提供的ETag相等，则执行拷贝操作，并返回200 OK。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_SOURCE_IF_MATCH, "5B3C1A2E053D763E1B002CC607C5****");
        // 如果源Object的ETag值和您提供的ETag不相等，则执行拷贝操作，并返回200 OK。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_SOURCE_IF_NONE_MATCH, "5B3C1A2E053D763E1B002CC607C5****");
        // 如果指定的时间等于或者晚于文件实际修改时间，则正常拷贝文件，并返回200 OK。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_SOURCE_IF_UNMODIFIED_SINCE, "2021-12-09T07:01:56.000Z");
        // 如果源Object在指定时间后被修改过，则执行拷贝操作。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_SOURCE_IF_MODIFIED_SINCE, "2021-12-09T07:01:56.000Z");
        // 指定设置目标Object元信息的方式。此处设置为COPY，表示复制源Object的元数据到目标Object。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_METADATA_DIRECTIVE, "COPY");
        // 指定OSS创建目标Object时使用的服务器端加密算法。
        // meta.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
        // 表示KMS托管的用户主密钥，该参数仅在x-oss-server-side-encryption为KMS时有效。
        // meta.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION_KEY_ID, "9468da86-3509-4f8d-a61e-6eab1eac****");
        // 指定OSS创建目标Object时的访问权限，此处设置为Private，表示只有Object的拥有者和授权用户有该Object的读写权限，其他用户没有权限操作该Object。
        // meta.setHeader(OSSHeaders.OSS_OBJECT_ACL, CannedAccessControlList.Private);
        // 指定Object的存储类型。此处设置为Standard，表示标准存储类型。
        // meta.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard);
        // 指定Object的对象标签，可同时设置多个标签。
        // meta.setHeader(OSSHeaders.OSS_TAGGING, "a:1");
        // 指定设置目标Object对象标签的方式。此处设置为COPY，表示复制源Object的对象标签到目标Object。
        // meta.setHeader(OSSHeaders.COPY_OBJECT_TAGGING_DIRECTIVE, "COPY");
        copyObjectRequest.setNewObjectMetadata(meta);

        // 复制文件。
        CopyObjectResult result = client.copyObject(copyObjectRequest);
        System.out.println("ETag: " + result.getETag() + " LastModified: " + result.getLastModified());

        // 关闭OSSClient。
        //ossClient.shutdown();
    }

    @Override
    public void splitCopyFile(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "<yourAccessKeyId>";
        String accessKeySecret = "<yourAccessKeySecret>";

        //String sourceBucketName = "<yourSourceBucketName>";
        //String sourceObjectName = "<yourSourceObjectName>";
        //String destinationBucketName = "<yourDestinationBucketName>";
        //String destinationObjectName = "<yourDestinationObjectName>";

        ObjectMetadata objectMetadata = client.getObjectMetadata(sourceBucketName, sourceKey);
        // 获取被拷贝文件的大小。
        long contentLength = objectMetadata.getContentLength();

        // 设置分片大小为10MB。
        long partSize = 1024 * 1024 * 10;

        // 计算分片总数。
        int partCount = (int) (contentLength / partSize);
        if (contentLength % partSize != 0) {
            partCount++;
        }
        System.out.println("total part count:" + partCount);

        // 初始化拷贝任务。可以通过InitiateMultipartUploadRequest指定目标文件元信息。
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(destinationBucketName, destinationKey);
        // 指定拷贝文件操作时是否覆盖同名Object。
        // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object。
        // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
        // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-forbid-overwrite", "true");
        initiateMultipartUploadRequest.setObjectMetadata(metadata);

        InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);
        String uploadId = initiateMultipartUploadResult.getUploadId();

        // 分片拷贝。
        List<PartETag> partETags = new ArrayList<PartETag>();
        for (int i = 0; i < partCount; i++) {
            // 计算每个分片的大小。
            long skipBytes = partSize * i;
            long size = partSize < contentLength - skipBytes ? partSize : contentLength - skipBytes;

            // 创建UploadPartCopyRequest。可以通过UploadPartCopyRequest指定限定条件。
            UploadPartCopyRequest uploadPartCopyRequest =
                    new UploadPartCopyRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
            uploadPartCopyRequest.setUploadId(uploadId);
            uploadPartCopyRequest.setPartSize(size);
            uploadPartCopyRequest.setBeginIndex(skipBytes);
            uploadPartCopyRequest.setPartNumber(i + 1);
            UploadPartCopyResult uploadPartCopyResult = client.uploadPartCopy(uploadPartCopyRequest);

            // 将返回的分片ETag保存到partETags中。
            partETags.add(uploadPartCopyResult.getPartETag());
        }

        // 完成分片拷贝任务。
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
                destinationBucketName, destinationKey, uploadId, partETags);

        // 指定拷贝文件操作时是否覆盖同名Object。
        // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object
        // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
        // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
        completeMultipartUploadRequest.addHeader("x-oss-forbid-overwrite", "true");

        client.completeMultipartUpload(completeMultipartUploadRequest);

        // 关闭OSSClient。
        //ossClient.shutdown();
    }

    @Override
    public void splitUpload(String bucketName,String objectName) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
//        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
//        String accessKeyId = "<yourAccessKeyId>";
//        String accessKeySecret = "<yourAccessKeySecret>";
//        String bucketName = "<yourBucketName>";
//        String objectName = "<yourObjectName>";

        // 创建OSSClient实例。
        //OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

        // 指定分片上传操作时是否覆盖同名Object。
        // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object。
        // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
        // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("x-oss-forbid-overwrite", "true");
        request.setObjectMetadata(metadata);

        // 初始化分片。
        InitiateMultipartUploadResult upresult = client.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags =  new ArrayList<PartETag>();
        // 计算文件有多少个分片。
        final long partSize = 1 * 1024 * 1024L;   // 1MB
        final File sampleFile = new File("<localFile>");
        long fileLength = sampleFile.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = null;
            try {
                instream = new FileInputStream(sampleFile);
                // 跳过已经上传的分片。
                instream.skip(startPos);
            } catch (Exception e) {
                e.printStackTrace();
            }

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(objectName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(instream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber( i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }


        // 创建CompleteMultipartUploadRequest对象。
        // 完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

        // 指定分片上传操作时是否覆盖同名Object。
        // 不指定x-oss-forbid-overwrite时，默认覆盖同名Object。
        // 指定x-oss-forbid-overwrite为false时，表示允许覆盖同名Object。
        // 指定x-oss-forbid-overwrite为true时，表示禁止覆盖同名Object，如果同名Object已存在，程序将报错。
        completeMultipartUploadRequest.addHeader("x-oss-forbid-overwrite", "true");

        // 完成上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = client.completeMultipartUpload(completeMultipartUploadRequest);

        // 关闭OSSClient。
        //ossClient.shutdown();
    }
}
