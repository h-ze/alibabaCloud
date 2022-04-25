package com.common.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Configuration
public class Email implements Serializable {

    private static final long serialVersionUID = 7309164702195939420L;
    private Integer emailId;
    private String email;
    private Integer status;
}
