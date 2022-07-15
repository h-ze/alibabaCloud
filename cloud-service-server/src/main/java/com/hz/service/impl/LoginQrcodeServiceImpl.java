package com.hz.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.common.entity.ResponseResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hz.service.ILoginQrcodeService;
import com.hz.service.RedisService;
import com.hz.vo.LoginQrcodeVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

import static com.hz.constant.Constant.APP_TOKEN;


/**
 * 二维码登录操作
 * 使用此功能时默认访问 ocalhost:6001/ 就可以跳转到这个页面 因为在WebMvcConfig中addViewControllers方法中已经设置里默认的跳转页面
 * 此页面使用的时freemarker页面
 */
@Service("LoginQrcodeService")
public class LoginQrcodeServiceImpl implements ILoginQrcodeService {

    private final static Logger logger = LoggerFactory.getLogger(LoginQrcodeServiceImpl.class);


    public static final Integer QRCODE_WIDTH = 230;
    public static final Integer QRCODE_HEIGHT = 230;

    @Autowired
    private RedisService redisService;


    @Override
    public ResponseResult<LoginQrcodeVO>  createLoginQrcode() {
        String qrcodeId = UUID.randomUUID().toString().replace("-","");
        String resultImage = "";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("rawtypes")
        HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 指定字符编码为“utf-8”
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 指定二维码的纠错等级为中级
        hints.put(EncodeHintType.MARGIN, 2); // 设置图片的边距
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrcodeId, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, "png", os);
            /**
             * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
             */
            resultImage = "data:image/png;base64," + Base64.encode(os.toByteArray());
            redisService.addLoginRedis(qrcodeId,"");

            LoginQrcodeVO loginQrcode = new LoginQrcodeVO();
            loginQrcode.setQrcodeId(qrcodeId);
            loginQrcode.setQrcodeImgUrl(resultImage);
            return ResponseResult.successResult(100000,loginQrcode);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.successResult(100001,"生成二维码失败");
        }

        /*try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrcodeContent, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT, hints);
            BufferedImage image = new BufferedImage(QRCODE_WIDTH, QRCODE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(image, qrcodeFormat, qrcodeFile);
            MatrixToImageWriter.writeToFile(bitMatrix, qrcodeFormat, qrcodeFile);

            *//*BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, "png", os);*//*

            //LoginQrcodeVO loginQrcode = new LoginQrcodeVO();
            //loginQrcode.setQrcodeId(qrcodeId);
            //loginQrcode.setQrcodeImgUrl(qrcodeImgUrl);
            // 4.写入Redis 缓存
            //String loginQrcodeKey = RedisKeyBuilder.getLoginqrcodeKey(qrcodeId);
            // 设置登录二维码的value为空表示没有登录，3分钟有效;
            //redisService.cacheValue(loginQrcodeKey, "", 180);
            //return ResponseResult.successResult("生成二维码成功", loginQrcode);
        } catch (Exception e) {
            logger.error("生成二维码发生异常，异常信息：{}", e);
            //return Response.failResult("生成二维码失败");
        }*/

    }

    @Override
    public ResponseResult<Boolean> qrcodeLogin(String qrcodeId, String userId) {
        boolean b = redisService.addLoginRedis(qrcodeId, userId);
        if (b){
            return ResponseResult.successResult(100000, true);

        }
        return ResponseResult.successResult(100001, false);


//        String loginRedis = redisService.getLoginRedis(qrcodeId);
//        if (!StringUtils.isEmpty(loginRedis)){
//            JSONObject jsonObject = JSONObject.parseObject(loginRedis);
//            if (!StringUtils.isEmpty(jsonObject.getString(APP_TOKEN))){
//                return ResponseResult.successResult(100000,"登录成功");
//            }else {
//                return ResponseResult.successResult(100001,"登录失败");
//
//            }
//        }
//        return ResponseResult.successResult(100001,"登录失败");

        /*String loginQrcodeKey = RedisKeyBuilder.getLoginqrcodeKey(qrcodeId);
        if (redisService.containsValueKey(loginQrcodeKey)) {
            boolean flag = redisService.cacheValue(loginQrcodeKey, userId, 180);
            if (flag) {
                return Response.successResult("二维码登录成功", flag);
            } else {
                logger.info("二维码登录失败，qrcodeId={}", qrcodeId);
                return Response.successResult("二维码登录失败", flag);
            }
        } else {
            logger.info("二维码已不存在，qrcodeId={}", qrcodeId);
            return Response.failResult("二维码登录失败", false);
        }*/
    }

    @Override
    public ResponseResult getLoginQrcodeStatus(String qrcodeId) {
        String loginRedis = redisService.getLoginRedis(qrcodeId);
        logger.info("redis:{}",loginRedis);
        if (!StringUtils.isEmpty(loginRedis)){
            JSONObject jsonObject = JSONObject.parseObject(loginRedis);
            if (!StringUtils.isEmpty(jsonObject.getString(APP_TOKEN))){
                return ResponseResult.successResult(100000,jsonObject.getString(APP_TOKEN));
            }else {
                return ResponseResult.successResult(100001,"二维码未登录");

            }
        }
        return ResponseResult.successResult(100001,"二维码未登录");
        /*String loginQrcodeKey = RedisKeyBuilder.getLoginqrcodeKey(qrcodeId);
        String userId = redisService.getValue(loginQrcodeKey);
        if (StringUtils.isBlank(userId)) {
            logger.info("二维码还未登录,qrcodeId={}", qrcodeId);
            return Response.failResult("二维码还未登录");
        }
        // 认证通过后，生成访问令牌
        Response<AccessToken> response = tokenService.createAccessToken(userId);
        if (ResponseCode.SUCCESS.equals(response.getCode())) {
            AccessToken accessTokenObj = response.getData();
            return Response.successResult("二维码已登录", accessTokenObj);
        } else {
            logger.error("二维码已登录，但生成访问令牌失败");
            return Response.failResult("二维码已登录，但是生成访问令牌失败");
        }*/

    }
}