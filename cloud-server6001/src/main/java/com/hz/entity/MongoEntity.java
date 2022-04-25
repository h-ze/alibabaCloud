package com.hz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Document(collection = "hz")
public class MongoEntity implements Serializable {

    @Id
    private Integer id;

    private String title;

    private String description;

    private String by;

    private String url;

    private List<DocumentMongo> documentMongos;

}
