package com.hz.server.service;



import com.common.entity.UserFile;

import java.util.List;

public interface UserFileService {

    List<UserFile> findUSerFileById(Integer id);
    void saveFileMessage(UserFile userFile);

    UserFile findFileById(String id);

    void updateFileCount(UserFile fileById);
}
