package com.hz.article.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;


public class Article implements Serializable {

    private static final long serialVersionUID = 7650924055218994426L;

    private Long id;

    private String title;

    private String author;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createBir;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateBir;

    private String description;


    public Article() {
    }

    public Article(Long id, String title, String author, Date createBir, Date updateBir, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createBir = createBir;
        this.updateBir = updateBir;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateBir() {
        return createBir;
    }

    public void setCreateBir(Date createBir) {
        this.createBir = createBir;
    }

    public Date getUpdateBir() {
        return updateBir;
    }

    public void setUpdateBir(Date updateBir) {
        this.updateBir = updateBir;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", createBir=" + createBir +
                ", updateBir=" + updateBir +
                ", description='" + description + '\'' +
                '}';
    }
}
