package com.hz.controller;

import com.common.entity.ResponseResult;
import com.hz.service.ILoginQrcodeService;
import com.hz.vo.LoginQrcodeVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author wangck
 * @date 2019/7/9
 */
@Controller
@RequestMapping("/login/qrcode")
public class QrcodeLoginController {
    private final static Logger logger = LoggerFactory.getLogger(QrcodeLoginController.class);

    @Autowired
    private ILoginQrcodeService loginQrcodeService;


    /**
     * 生成登录的二维码
     *
     * @return
     */
    @PostMapping("create")
    @ResponseBody
    public ResponseResult<LoginQrcodeVO>/*Response<LoginQrcodeVO>*/ createLoginQrcode() throws IOException {
        return loginQrcodeService.createLoginQrcode();
    }

    /**
     * 扫码登录
     * 1、获取登录用户信息
     * 2、拼接登录二维码
     *
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResponseResult<Boolean>/*Response<Boolean>*/ qrcodeLogin(String qrcodeId, String userId) {
        logger.info("扫码登录方法");
        ResponseResult<Boolean> booleanResponseResult = loginQrcodeService.qrcodeLogin(qrcodeId, userId);
        return booleanResponseResult;
//        if (StringUtils.isBlank(qrcodeId)) {
//            logger.error("qrcodeId参数不能为空");
//            return Response.failResult("二维码Id不能为空");
//        }
//        if (StringUtils.isBlank(userId)) {
//            logger.error("userId参数不能为空");
//            return Response.failResult("用户Id不能为空");
//        }
//        return loginQrcodeService.qrcodeLogin(qrcodeId, userId);
    }

    /**
     * 验证二维码是否已登录
     *
     * @return
     */
    @PostMapping("isLogined")
    @ResponseBody
    public ResponseResult/*Response<AccessToken>*/ checkQrcodeIsLogined(String qrcodeId) {
        logger.info("验证登录");
        if (StringUtils.isBlank(qrcodeId)) {
            logger.error("qrcodeId参数不能为空");
            return ResponseResult.errorResult(999999,"二维码Id不能为空");
        }
        return loginQrcodeService.getLoginQrcodeStatus(qrcodeId);
    }
}
