package com.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Configuration
public class Book implements Serializable {
    private static final long serialVersionUID = -5539252535252251772L;
    private Integer bookId;
    private String bookName;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
}
