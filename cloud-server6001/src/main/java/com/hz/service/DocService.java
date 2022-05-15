package com.hz.service;

import com.common.entity.Document;
import com.common.entity.PageRequest;
import com.common.entity.PageResult;
import com.hz.entity.DocumentMongo;

import java.util.List;

public interface DocService {
    int createDocument(Document document);
    int deleteDocument(String docId);
    int updateDocument(Document document);
    Document getDocument(String docId);
    List<Document> getDocumentList(String userId);

    String uploadDoc(Document document,DocumentMongo documentMongo,byte[] bytes,Long size,String suffix);

    /**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @param pageRequest 自定义，统一分页查询请求
     * @return PageResult 自定义，统一分页查询结果
     */
    PageResult getDocsPage(PageRequest pageRequest,String userId);

    /**
     * 新方法分页查询 根据继承basecontroller方法获取
     * @param userId
     * @return
     */
    List<Document> getDocByPage(String userId);

    String convertDoc();

    String convertTaskDoc();


    /**
     * mybatis批量操作 批处理
     * Mybatis内置的ExecutorType有3种，默认为simple,该模式下它为每个语句的执行创建一个新的预处理语句，单条提交sql；
     * 而batch模式重复使用已经预处理的语句，并且批量执行所有更新语句，显然batch性能将更优；
     * 但batch模式也有自己的问题，比如在Insert操作时，在事务没有提交之前，是没有办法获取到自增的id，这在某型情形下是不符合业务要求的
     * @param documents
     * @return
     */
    int createDocuments(List<Document> documents);


    /**
     * 拼接批量操作
     * @param documents
     * @return
     */
    int createDocumentsSeparator(List<Document> documents);

}
