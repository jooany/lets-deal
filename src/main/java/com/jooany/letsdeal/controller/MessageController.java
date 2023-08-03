package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    @Autowired
    private final MessageService messageService;

//    @GetMapping
//    public Response<List<CategoryRes>> getCategoryList() {
//        return Response.success(categoryService.getCategoryList());
//    }
}
