package com.common.web.controller;


import com.common.entity.PageRequest;
import com.common.entity.PageResult;
import com.common.utils.DateUtils;
import com.common.utils.PageUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.xml.crypto.Data;
import java.beans.PropertyEditorSupport;
import java.util.List;

public class BaseController {
    protected final Logger logger =LoggerFactory.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){

        // Date 类型转换
        webDataBinder.registerCustomEditor(Data.class,
                new PropertyEditorSupport()
                {
                    @Override
                    public void setAsText(String text)
                    {
                        setValue(DateUtils.convertParseDate(text));
                    }
                });

    }


    /**
     * 设置请求分页数据
     * @param pageRequest
     * @param list
     */
    protected PageInfo<?> startPage(PageRequest pageRequest, List<?> list){
        PageInfo<?> pageInfo = PageUtils.startPage(pageRequest, list);
        return pageInfo;
    }


    /**
     *
     * @param pageInfo
     * @return
     */
    protected PageResult getPageResult(PageInfo<?> pageInfo){
        PageResult pageResult = PageUtils.getPageResult(pageInfo);
        return pageResult;
    }


    /**
     * 清理分页的线程变量
     */
    protected void clearPage(){
        PageUtils.clearPage();
    }

}
