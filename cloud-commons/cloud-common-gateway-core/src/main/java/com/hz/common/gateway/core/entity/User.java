package com.hz.common.gateway.core.entity;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户对象,添加swagger属性描述支持
 * 参考资料:https://jakubstas.com/spring-jersey-swagger-create-documentation/
 * @author admin
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable{

    @NotEmpty(message = "姓名不能为空")
	private String name;
	private String password;
	private Integer sex;
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄大于0")
    @Max(value = 300, message = "年龄不大于120")
	private Integer age;
	private String token;
	private String freshToken;


}
