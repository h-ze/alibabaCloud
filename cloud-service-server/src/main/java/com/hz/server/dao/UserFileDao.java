package com.hz.server.dao;



import com.common.entity.UserFile;

import java.util.List;

public interface UserFileDao {
    //根据用户id获取文件列表
    List<UserFile> findByUserId(Integer id);

    void savaFileMessage(UserFile userFile);

    UserFile findFileById(String id);

    void updateFile(UserFile userFile);
}
