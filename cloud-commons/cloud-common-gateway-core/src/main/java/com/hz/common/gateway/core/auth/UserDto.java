package com.hz.common.gateway.core.auth;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 */
@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@EqualsAndHashCode(callSuper = false)
public class UserDto implements Serializable {
    private String userId;
    private String username;
    private String password;
    private Integer status;
    private List<RoleDto> roles;

}
