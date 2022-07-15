package com.hz.book.controller;


import com.common.entity.Book;
import com.common.entity.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/book")
public class BookController {

    @GetMapping("/getBook")
    public ResponseResult getBook(String name){
        Book book = new Book();
        book.setBookName(name);
        book.setBookId(1);
        book.setCreateDate(new Date());
        return ResponseResult.successResult(100000,book);
    }
}
