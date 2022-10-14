package com.hz.web.utils;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hz.web.entity.PageRequest;
import com.hz.web.entity.PageResult;

import java.util.List;

public class PageUtils {

    /**
     * 将分页信息封装到统一的接口
     * @param pageInfo
     * @return
     */
    public static PageResult getPageResult(PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        pageResult.setCurrentSize(pageInfo.getList().size());
        return pageResult;
    }


    public static PageInfo<?> startPage(PageRequest pageRequest, List<?> list) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        //List<Document> docsPage = docDao.getDocsPage(userId);
        return new PageInfo<>(list);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
