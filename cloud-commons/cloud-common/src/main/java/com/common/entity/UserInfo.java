package com.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Configuration
@ApiModel(value = "UserMessage对象", description = "返回用户详细信息")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @NotNull(message = "userId不能为空")
    @NotBlank(message = "userId不能为空字符")
    private String userId;

    @ApiModelProperty(value = "企业id")
    @NotNull(message = "enterpriseId 不能为空")
    @NotBlank(message = "enterpriseId 不能为空字符")
    private String enterpriseId;

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "email不能为空")
    @NotBlank(message = "email不能为空字符")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "是否绑定qq 1为绑定")
    private Integer bindQQ;

    @ApiModelProperty(value = "是否绑定微信 1为绑定")
    private Integer bindWeChat;

    @ApiModelProperty(value = "是否绑定手机 1为绑定")
    private Integer bindPhone;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户状态")
    private String status;

    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "用户全名")
    private String fullName;

    @ApiModelProperty(value = "关于我")
    private String aboutMe;
}
