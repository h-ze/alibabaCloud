package com.hz.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * 网点基础信息实体类
 * @author  付为地
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkEntity  implements Serializable {
    /**
     * 网点编码
     */
    private String code;

    /**
     * 网点经度
     */
    double lon;

    /**
     * 网点维度
     */
    double lat;

    /**
     * 创建时间
     */
    Instant createTime;

    /**
     * 检索结果
     * 对应距离
     */
    double distance;

}
