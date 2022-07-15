package com.hz.article.service.impl;

import com.hz.article.dao.ArticleDao;
import com.hz.article.entity.Article;
import com.hz.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Integer addArticle(Article article) {
        return articleDao.addArticle(article);
    }

    @Override
    public Integer updateArticle(Article article) {
        return articleDao.updateArticle(article);
    }

    @Override
    public Article selectArticle(Long id) {
        return articleDao.selectArticle(id);
    }

    @Override
    public Article selectArticleByAuthor(String author) {
        return articleDao.selectArticleByAuthor(author);
    }

    @Override
    public Article selectArticleByTitle(String title) {
        return articleDao.selectArticleByTitle(title);
    }

    @Override
    public List<Article> getArticlesBYAuthor(String author) {
        return articleDao.getArticlesBYAuthor(author);
    }

    @Override
    public Integer deleteArticle(Long id) {
        return articleDao.deleteArticle(id);
    }

    @Override
    public Integer deleteArticleByName(String title) {
        return articleDao.deleteArticleByName(title);
    }

    @Override
    public Integer deleteArticleByAuthor(String author) {
        return articleDao.deleteArticleByAuthor(author);
    }
}
