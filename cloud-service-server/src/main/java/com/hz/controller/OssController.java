package com.hz.controller;


import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.common.entity.ResponseResult;
import com.common.exception.RRException;
import com.hz.cloud.OSSFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

import static com.hz.cloud.OSSFactory.build;


@RestController
@RequestMapping("oss")
@Api(tags = "云存储器接口")
public class OssController {

    /**
     * 上传文件
     */
    //@RequiresPermissions("sys:oss:all")
    @ApiOperation(value = "上传文件",notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "docName",value = "文档名",paramType = "query",dataType = "String",required = false),
            //@ApiImplicitParam(name = "encryptConfig",value = "加密策略",paramType = "query",dataType ="String",required = false) ,
            //@ApiImplicitParam(name = "docId",value = "文档id",paramType = "query",dataType = "String",required = true),
            @ApiImplicitParam(name ="file",value = "文件",paramType = "form",dataType = "__file",required = true)
    })
    @PostMapping("uploadFile")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = build().uploadSuffix(file.getBytes(),file.getSize(), suffix);
        //OSSFactory.build().uploadObject2OSS(file,"","test/");

		//保存文件信息
		/*SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);*/

		//存储的信息需要入库

        return ResponseResult.successResult(100000,url);

    }


    @ApiOperation(value = "删除文件",notes = "删除文件")
    @DeleteMapping("deleteFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="bucketName",value = "bucket名称",paramType = "query",dataType = "String",required = true),
            @ApiImplicitParam(name ="fileName",value = "bucket名称",paramType = "query",dataType = "String",required = true)

    })
    public ResponseResult deleteFile(@RequestParam("bucketName")String bucketName,@RequestParam("fileName")String fileName) throws Exception {
        //存储的信息需要入库
        String deleteFile = build().deleteFile(bucketName,"", fileName);
        return ResponseResult.successResult(100000,deleteFile);
    }

    @ApiOperation(value = "查询文件",notes = "查询文件")
    @GetMapping("fileList")
    @ApiImplicitParam(name ="bucketName",value = "bucket名称",paramType = "query",dataType = "String",required = true)
    public ResponseResult selectFile(@RequestParam("bucketName") String bucketName){
        List fileList = build().getFileList(bucketName);
        return ResponseResult.successResult(100000,fileList);
    }

    @GetMapping("bucketList")
    @ApiOperation(value = "获取bucket列表",notes = "获取bucket列表")
    public ResponseResult getBucketList(){
        List<Bucket> allBucketList = build().getAllBucketList();
        return ResponseResult.successResult(100000,allBucketList);
    }

    @GetMapping("bucketInfo")
    @ApiOperation(value = "获取bucket信息",notes = "获取bucket信息")
    public ResponseResult getBucketInfo(@RequestParam("bucketName")String bucketName){
        BucketInfo bucketInfo = build().getBucketInfo(bucketName);
        return ResponseResult.successResult(100000,bucketInfo);
    }


    @GetMapping("fileUrl")
    @ApiOperation(value = "获取文件的url",notes = "获取文件的url")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="bucketName",value = "bucket名称",paramType = "query",dataType = "String",required = true),
            @ApiImplicitParam(name ="key",value = "关键字",paramType = "query",dataType = "String",required = true)

    })
    public ResponseResult getFileURL(@RequestParam("bucketName")String bucketName,@RequestParam("key")String key){
        URL downloadPath = build().getDownloadPath(bucketName, key);

        return ResponseResult.successResult(100000,downloadPath);
    }



}
