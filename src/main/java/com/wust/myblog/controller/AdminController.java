package com.wust.myblog.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.wust.myblog.common.FtpEnum;
import com.wust.myblog.modal.Blog;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.service.IBlogService;
import com.wust.myblog.service.IFileService;
import com.wust.myblog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@RequestMapping(value = "admin")
@RestController
public class AdminController {

    @Autowired
    IFileService iFileService;
    @Autowired
    IBlogService iBlogService;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Result addBlog(Blog blog){
        return iBlogService.addBlog(blog);
    }

    @RequestMapping(value = "upload",method = RequestMethod.POST)
    public Result upload(@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = FtpEnum.ftpEnum.getPrefixUrl()+targetFileName;
        Map fileMap = MapUtil.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);
        return ResultUtil.success(fileMap);
    }

    @RequestMapping(value = "richtext_img_upload",method = RequestMethod.POST)
    public Map richtextImgUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        Map resultMap = MapUtil.newHashMap();
        Map data = MapUtil.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = null;
        if (file!=null){
             targetFileName = iFileService.upload(file,path);
        }
        if (StrUtil.isBlank(targetFileName)){
            resultMap.put("code",-1);
            resultMap.put("mag","上传失败");
            return resultMap;
        }
        String url = FtpEnum.ftpEnum.getPrefixUrl()+targetFileName;
        resultMap.put("code",0);
        resultMap.put("msg","");
        data.put("src",url);
        data.put("title",targetFileName);
        resultMap.put("data",data);
        return resultMap;
    }
}
