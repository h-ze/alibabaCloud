package com.hz.server.dao;

import com.hz.server.entity.SysOss;

import java.util.List;

public interface SysConfigDao {
    int createSysConfig(SysOss sysOss);

    SysOss selectOss(String sysName);

    List<SysOss> selectAllOss();

    int updateOss(SysOss sysOss);

    int deleteOss(String sysName);
}
