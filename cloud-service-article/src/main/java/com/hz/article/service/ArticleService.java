package com.hz.article.service;

import com.hz.article.entity.Article;

import java.util.List;

public interface ArticleService {

    Integer addArticle(Article article);

    Integer updateArticle(Article article);

    Article selectArticle(Long id);

    Article selectArticleByAuthor(String author);

    Article selectArticleByTitle(String title);

    List<Article> getArticlesBYAuthor(String author);

    Integer deleteArticle(Long id);

    Integer deleteArticleByName(String title);

    Integer deleteArticleByAuthor(String author);
}
