package com.hz.server.service.impl;

import com.common.entity.DocTask;
import com.common.entity.Document;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hz.server.cloud.OSSFactory;
import com.hz.server.dao.DocDao;
import com.hz.server.entity.DocumentMongo;
import com.hz.server.mongo.MongoService;
import com.hz.server.service.AsyncService;
import com.hz.server.service.DocService;
import com.hz.server.task.TaskListenListenner1;
import com.hz.server.task.TaskManager;
import com.hz.server.task.TaskParam;
import com.hz.web.entity.PageRequest;
import com.hz.web.entity.PageResult;
import com.hz.web.utils.PageUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("docService")
@Transactional
public class DocServiceImpl implements DocService {

    private static final Logger logger = LoggerFactory.getLogger(DocServiceImpl.class);

    @Autowired
    private DocDao docDao;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private MongoService mongoService;

    @Autowired
    private TaskManager taskManager;

    @Override
    public int createDocument(Document document) {
        return docDao.addDoc(document);
    }

    @Override
    public int deleteDocument(String docId) {
        return docDao.deleteDoc(docId);
    }

    @Override
    public int updateDocument(Document document) {
        return docDao.updateDoc(document);
    }

    @Override
    public Document getDocument(String docId) {
        return docDao.getDoc(docId);
    }

    @Override
    public List<Document> getDocumentList(String userId) {
        return docDao.getDocs(userId);
    }

    @Override
    public String uploadDoc(Document document,DocumentMongo documentMongo,byte[] bytes,Long size,String suffix){

        //String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = OSSFactory.build().uploadSuffix(bytes,size, suffix);



        docDao.addDoc(document);
        ArrayList<String> objects = new ArrayList<>();
        objects.add("1");

        //objects.get(2);
        mongoService.insert(documentMongo,"hz");

        return url;
    }

    @Override
    public PageResult getDocsPage(PageRequest pageRequest, String userId) {
        return PageUtils.getPageResult(getPageInfo(pageRequest,userId));
    }

    @Override
    public List<Document> getDocByPage(String userId) {
        List<Document> docsPage = docDao.getDocsPage(userId);
        return docsPage;
    }

    @Override
    public String convertDoc() {
        String id = UUID.randomUUID().toString().replace("-", "");
        DocTask docTask = new DocTask();
        docTask.setTaskData("convert task");
        docTask.setDocId("");
        docTask.setDocName("");
        docTask.setTaskId(id);
        int task = asyncService.createTask(docTask);
        logger.info("taskid:{}",task);
        if (task>0){
            asyncService.convert(docTask);
        }
        return id;
    }

    @Override
    public String convertTaskDoc() {
        String id = UUID.randomUUID().toString().replace("-", "");
        DocTask docTask = new DocTask();
        docTask.setTaskData("convert task");
        docTask.setDocId("");
        docTask.setDocName("");
        docTask.setTaskId(id);
        int task = asyncService.createTask(docTask);
        logger.info("taskid:{}",task);
        if (task>0){
            TaskParam taskParam = new TaskParam(TaskListenListenner1.class);
            taskParam.put("param1", "test1");
            taskParam.put("param2", "test2");
            taskParam.put("id",id);
            taskManager.pushTask(taskParam);
            //asyncService.convert(docTask);
        }
        return id;
    }

    @Override
    public int createDocuments(List<Document> documents) {

        //设置true的话以后的增删改就不用提交事务
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false)) {

            DocDao mapper = sqlSession.getMapper(DocDao.class);
            for (int i=0;i<documents.size();i++){
                mapper.addDoc(documents.get(i));
                if (i%1000 ==999){
                    sqlSession.commit();
                    sqlSession.clearCache();
                }
            }
            sqlSession.commit();
            sqlSession.clearCache();
        }

        return 0;
    }

    @Override
    public int createDocumentsSeparator(List<Document> documents) {
        return docDao.addDocs(documents);
    }

    /**
     * 调用分页插件完成分页
     * @return
     */
    private PageInfo<Document> getPageInfo(PageRequest pageRequest,String userId) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Document> docsPage = docDao.getDocsPage(userId);
        return new PageInfo<>(docsPage);
    }
}
