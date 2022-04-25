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
public class Document implements Serializable {
    private static final long serialVersionUID = -5539252535252251777L;

    private String userId;
    private String docId;
    private String cAppId;
    private String cmisId;
    private String docName;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private String encryptConfig;

}
