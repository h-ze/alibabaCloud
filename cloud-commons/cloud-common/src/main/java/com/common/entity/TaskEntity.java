package com.common.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
@Configuration
public class TaskEntity implements Serializable {
    private static final long serialVersionUID = 7309164702195939427L;

    private String taskId;

    private String docName;

    private String docId;

    private String taskData;

}
