package com.hz.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.common.entity.ResponseResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hz.annotation.AccessLimit;
import com.hz.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 二维码相关接口
 */

@RestController
@RequestMapping("code")
@Api("二维码接口")
@Slf4j
public class CodeController {

    @Autowired
    private UserService userService;

    /**
     * 生成二维码
     * @param request
     * @param response
     */
    //@AccessLimit(seconds = 5, maxCount = 5, needLogin = false)
    @GetMapping(value = "/getLoginQr")
    @ApiOperation("生成二维码")
    public ResponseResult createCodeImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Controller","no-cache");

        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");

        String uuid = userService.createQrImg();
        log.info(uuid);
        log.info(uuid);
        //String qrCode = createQrCode(uuid,200,200);
        response.setHeader("uuid",uuid);

        try {
            QrCodeUtil.generate(uuid,300,300,"jpg",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.successResult(100000,uuid);

    }

    @GetMapping("query")
    public ResponseResult query(){
        return ResponseResult.successResult(100000,"success");
    }

    public String createQrCode(String content, int width, int height) throws IOException {
        String resultImage = "";
        if (!StringUtils.isEmpty(content)) {
            ServletOutputStream stream = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 指定字符编码为“utf-8”
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.MARGIN, 2); // 设置图片的边距
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                /**
                 * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
                 */
                resultImage = new String("data:image/png;base64," + Base64.encode(os.toByteArray()));

                return resultImage;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
        return null;
    }
}
