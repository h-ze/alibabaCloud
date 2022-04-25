package com.hz.common.entity;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 该代码由付为地的编码机器人自动生成 时间：2017-09-21 13:44:39
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Solr对象", description = "数据库实体")
public class Solr implements Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	private String id;
	@NotNull(message = "价格不能为空")
	@Min(value = 0, message = "价格大于0")
	@Max(value = 300, message = "价格不大于300")
	private Integer price;// 价格
	@NotEmpty(message = "标题不能为空")
	private String title;// 标题
	private String name;// 称呼
	private Date createtime;// 创建时间


}