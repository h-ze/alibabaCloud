package com.hz.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
//@Document(collection = "hz")
public class DocumentMongo implements Serializable {
    //@Id
    private Long id;

    private String docId;

    private String docName;

    private String docOwner;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createDate;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date updateDate;

    private Long clickNumber;
}
