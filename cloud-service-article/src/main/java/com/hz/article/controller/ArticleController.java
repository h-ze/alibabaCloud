package com.hz.article.controller;


import com.common.entity.ResponseResult;
import com.hz.article.entity.Article;
import com.hz.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;


    @PostMapping("article")
    public ResponseResult addArticle(@RequestBody Article article){

        Article selectArticleByTitle = articleService.selectArticleByTitle(article.getTitle());

        if (selectArticleByTitle!=null){
            return ResponseResult.successResult(100001,"已存在");
        }

        Integer integer = articleService.addArticle(article);
        if (integer>0){
            return ResponseResult.successResult(100000,"成功");
        }

        return ResponseResult.successResult(999999,"未知错误");
    }

    @GetMapping("getArticleById")
    public ResponseResult getArticleById(Long id){
        Article article = articleService.selectArticle(id);
        return ResponseResult.successResult(100000,article);
    }

    @GetMapping("getArticleByTitle")
    public ResponseResult getArticleByTitle(String title){
        Article article = articleService.selectArticleByTitle(title);
        return ResponseResult.successResult(100000,article);
    }

    @GetMapping("getArticleByAuthor")
    public ResponseResult getArticleByAuthor(String author){
        Article article = articleService.selectArticleByAuthor(author);
        return ResponseResult.successResult(100000,article);
    }

    @GetMapping("getArticles")
    public ResponseResult getArticles(String author){
        List<Article> articlesBYAuthor = articleService.getArticlesBYAuthor(author);
        return ResponseResult.successResult(100000,articlesBYAuthor);
    }


    @PutMapping("article")
    public ResponseResult updateArticle(@RequestBody Article article){
        Integer integer = articleService.updateArticle(article);
        return ResponseResult.successResult(100000,integer);
    }

    @DeleteMapping("article")
    public ResponseResult deleteArticle(Long id){
        Integer integer = articleService.deleteArticle(id);
        return ResponseResult.successResult(100000,integer);
    }

    @DeleteMapping("deleteArticleByName")
    public ResponseResult deleteArticleByName(String title){
        Integer integer = articleService.deleteArticleByName(title);
        return ResponseResult.successResult(100000,integer);
    }

    @DeleteMapping("deleteArticleByAuthor")
    public ResponseResult deleteArticleByAuthor(String author){
        Integer integer = articleService.deleteArticleByAuthor(author);
        return ResponseResult.successResult(100000,integer);
    }



}
