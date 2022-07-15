package com.hz.article.dao;

import com.hz.article.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDao {

    Integer addArticle(Article article);

    Integer updateArticle(Article article);

    Article selectArticle(Long id);

    Article selectArticleByTitle(String title);

    Article selectArticleByAuthor(String author);

    List<Article> getArticlesBYAuthor(String author);

    Integer deleteArticle(Long id);

    Integer deleteArticleByName(String title);

    Integer deleteArticleByAuthor(String author);



}
