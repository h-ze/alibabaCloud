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
public class EnterpriseInfo implements Serializable {
    private String enterpriseId;
    private String enterpriseName;
    private String oddEvenFactor;
    private String documentTotal;
    private String alreadyUploadDocument;
    //@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    //private Date createTime;
}
