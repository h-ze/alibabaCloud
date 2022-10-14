package com.hz.server.controller;

import com.common.ConfigConstant;
import com.common.entity.ResponseResult;
import com.hz.server.cloud.CloudStorageConfig;
import com.hz.server.entity.SysOss;
import com.hz.server.service.SysConfigService;
import com.hz.server.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("oss-config")
@Api(tags = "云存储器配置接口")
public class SysOssController {

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private JWTUtil jwtUtil;

	private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@RequiresRoles("superAdmin")
	@ApiOperation(value ="获取配置列表",notes="超级管理员获取配置列表",response = ResponseResult.class)
	public ResponseResult list(){
		List<SysOss> sysOssList = sysConfigService.getSysOssList();
		return ResponseResult.successResult(100000,sysOssList);
	}


	/**
	 * 云存储配置信息
	 */
	@GetMapping("/config")
	//@RequiresPermissions("sys:oss:all")
	@RequiresRoles("superAdmin")
	@ApiOperation(value ="获取配置",notes="超级管理员获取配置",response = ResponseResult.class)
	@ApiImplicitParam(name = "sysName",value = "服务名称",dataType = "String",paramType = "query",required = true)
	public ResponseResult config(@RequestParam("sysName") String sysName){
		CloudStorageConfig config = sysConfigService.getConfigObject(sysName/*, CloudStorageConfig.class*/);

		return ResponseResult.successResult(100000,config);
	}


	/**
	 * 保存云存储配置信息
	 */
	@PostMapping("/config")
	//@RequiresPermissions("sys:oss:all")
	@RequiresRoles("superAdmin")
	@ApiOperation(value ="创建配置",notes="超级管理员创建配置",response = ResponseResult.class)
	//@ApiImplicitParam(name = "sysName",value = "服务名称",dataType = "com.hz.entity.SysOss",paramType = "body")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="endpoint",value = "用户的endpoint",dataType = "String",paramType = "form",required = true),
			@ApiImplicitParam(name ="accessKeyId",value = "用户的accessKeyId",dataType = "String",paramType = "form",required = true),
			@ApiImplicitParam(name ="accessKeySecret",value = "用户的accessKeySecret",dataType = "String",paramType = "form",required = true),
			@ApiImplicitParam(name ="bucketName",value = "用户的bucketName",dataType = "String",paramType = "form",required = true)
	})
	public ResponseResult saveConfig(@RequestParam("endpoint") String endpoint,
									 @RequestParam("accessKeyId")String accessKeyId,
									 @RequestParam("accessKeySecret")String accessKeySecret,
									 @RequestParam("bucketName")String bucketName){

		String principal = (String) SecurityUtils.getSubject().getPrincipal();
		Claims claims = jwtUtil.parseJWT(principal);
		String userId = (String)claims.get("userId");
		SysOss sysOss = new SysOss(endpoint,
				accessKeyId,
				accessKeySecret,
				bucketName,
				"",
				new Date(),
				new Date(),
				userId,
				"aliyun",
				"aliyun");
		sysConfigService.saveConfig(sysOss);
		return ResponseResult.successResult(100000,"保存成功");
		//校验类型
		/*ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

		sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();*/
	}

	@DeleteMapping("config")
	@ApiOperation(value ="删除配置",notes="超级管理员删除配置",response = ResponseResult.class)
	@RequiresRoles("superAdmin")
	@ApiImplicitParam(name = "sysName",value = "服务名称",dataType = "String",paramType = "query")
	public ResponseResult deleteConfig(@RequestParam("sysName")String sysName){
		sysConfigService.deleteBatch(sysName);
		return ResponseResult.successResult(100000,"删除成功");

	}

	@PutMapping("config")
	@ApiOperation(value ="修改配置",notes="超级管理员修改配置",response = ResponseResult.class)
	@RequiresRoles("superAdmin")
	public ResponseResult updateConfig(@RequestBody @ApiParam(value = "服务对象",required = true) SysOss sysOss){
		sysConfigService.update(sysOss);
		return ResponseResult.successResult(100000,"更新成功");

	}

	/**
	 * 上传文件
	 */
	@RequestMapping("/upload")
	@RequiresPermissions("sys:oss:all")
	public ResponseResult upload(@RequestParam("file") MultipartFile file){
		return ResponseResult.successResult(100000,"保存成功");

		/*if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}

		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);

		return R.ok().put("url", url);*/
	}


	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:oss:all")
	public ResponseResult delete(@RequestBody Long[] ids){
		return ResponseResult.successResult(100000,"保存成功");

		/*sysOssService.removeByIds(Arrays.asList(ids));

		return R.ok();*/
	}

}
