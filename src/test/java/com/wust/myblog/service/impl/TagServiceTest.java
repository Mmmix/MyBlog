package com.wust.myblog.service.impl;

import com.wust.myblog.modal.Tag;
import com.wust.myblog.service.ITagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceTest {

    @Autowired
    ITagService iTagService;

    @Test
    public void addTag() {
        Tag tag = new Tag();
        tag.setName(".net");
        tag.setDescribe("aaaaa");
        System.out.println(iTagService.addTag(tag));

    }

    @Test
    public void deleteTag() {
    }

    @Test
    public void selectTagById() {
    }

    @Test
    public void selectAll() {
    }

    @Test
    public void updateTag() {
    }
}