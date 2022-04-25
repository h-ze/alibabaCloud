package com.hz.common.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleDto implements Serializable {
    private Long id;
    private String code;
    private String name;
    private List<String> urls;

}
