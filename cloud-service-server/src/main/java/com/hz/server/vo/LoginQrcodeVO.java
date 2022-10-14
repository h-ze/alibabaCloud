package com.hz.server.vo;

import java.io.Serializable;

/**
 * 登录二维码信息
 * @author wangck
 * @date 2019/7/10
 */
public class LoginQrcodeVO implements Serializable {

    /**
     * 二维码图片地址
     */
    private String qrcodeImgUrl;

    /**
     * 二维码Id
     */
    private String qrcodeId;

    public String getQrcodeImgUrl() {
        return qrcodeImgUrl;
    }

    public void setQrcodeImgUrl(String qrcodeImgUrl) {
        this.qrcodeImgUrl = qrcodeImgUrl;
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    @Override
    public String toString() {
        return "LoginQrcodeVO{" +
                "qrcodeImgUrl='" + qrcodeImgUrl + '\'' +
                ", qrcodeId='" + qrcodeId + '\'' +
                '}';
    }
}
