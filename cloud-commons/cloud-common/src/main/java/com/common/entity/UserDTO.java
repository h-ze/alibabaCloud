package com.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@EqualsAndHashCode(callSuper = false)
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private List<String> roles;

    public <T> UserDTO(Long l, String macro, String password, int i, ArrayList<T> admin) {
    }
}
