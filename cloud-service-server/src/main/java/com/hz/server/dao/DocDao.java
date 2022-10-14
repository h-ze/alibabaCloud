package com.hz.server.dao;

import com.common.entity.Document;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DocDao {
    int addDoc(Document document);
    int deleteDoc(String docId);
    int updateDoc(Document document);
    Document getDoc(String docId);
    List<Document> getDocs(String userId);
    List<Document> getDocsPage(String userId);

    int addDocs(@Param("documents") List<Document> documents);
}
