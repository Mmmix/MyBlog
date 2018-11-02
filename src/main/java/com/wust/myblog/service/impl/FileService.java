package com.wust.myblog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wust.myblog.service.IFileService;
import com.wust.myblog.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService implements IFileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //获取拓展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFilename = UUID.randomUUID().toString().replace("-","")+"."+fileExtensionName;
        logger.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}",fileName,path,uploadFilename);
        //目录
        File fileDir = new File(path);
        if (!fileDir.exists()){
            //赋予写权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFilename);
        try {
            file.transferTo(targetFile);
            //文件已经上传成功
            boolean result = FtpUtil.uploadFile(CollectionUtil.newArrayList(targetFile));
            //已经上传到ftp服务器上
            targetFile.delete();
            if (!result)
                return null;
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }

}
