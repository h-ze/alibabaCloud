package com.hz.service.impl;

import com.common.entity.UserFile;
import com.hz.dao.UserFileDao;
import com.hz.service.UserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileDao userFileDao;
    @Override
    public List<UserFile> findUSerFileById(Integer id) {
        List<UserFile> byUserId = userFileDao.findByUserId(id);

        return byUserId;
    }

    @Override
    public void saveFileMessage(UserFile userFile) {
        String image = userFile.getType().endsWith("image") ? "是" : "否";
        userFile.setIsImg(image);
        userFile.setUploadTime(new Date());
        userFile.setDowncounts(0);

        userFileDao.savaFileMessage(userFile);
    }

    @Override
    public UserFile findFileById(String id) {
        UserFile file = userFileDao.findFileById(id);
        return file;
    }

    @Override
    public void updateFileCount(UserFile fileById) {
        userFileDao.updateFile(fileById);
    }
}
