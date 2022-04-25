package com.hz.dao;

import com.hz.entity.SysOss;

import java.util.List;

public interface SysConfigDao {
    int createSysConfig(SysOss sysOss);

    SysOss selectOss(String sysName);

    List<SysOss> selectAllOss();

    int updateOss(SysOss sysOss);

    int deleteOss(String sysName);
}
