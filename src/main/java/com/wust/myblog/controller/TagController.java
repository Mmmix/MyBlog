package com.wust.myblog.controller;

import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.Tag;
import com.wust.myblog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("tag")
public class TagController {

    @Autowired
    ITagService iTagService;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Result addTag(Tag tag){
        return iTagService.addTag(tag);
    }
    @RequestMapping(value = "delete",method = RequestMethod.DELETE)
    public Result deleteTag(Tag tag){
        return iTagService.deleteTag(tag);
    }
    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public Result getTag(@PathVariable Integer id){
        return iTagService.selectTagById(id);
    }
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public Result addTag(){
        return iTagService.selectAll();
    }
    @RequestMapping(value = "update",method = RequestMethod.PUT)
    public Result updateTag(Tag tag){
        return iTagService.updateTag(tag);
    }




}
