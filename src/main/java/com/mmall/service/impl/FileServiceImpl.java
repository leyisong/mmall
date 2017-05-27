package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        //获得原始文件名
        String fileName = file.getOriginalFilename();
        //拓展名
        //后缀(不要点)
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        //用uuid构建新的文件名
        String uoloadFileName = UUID.randomUUID() + "." + fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uoloadFileName);
        //创建文件夹
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);//赋予可写权限
            fileDir.mkdirs();
        }
        //创建文件
        File targetFile = new File(path, uoloadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传成功
            //// TODO: 2017/5/24  将 targetFile上传带ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // TODO: 2017/5/24  上传完成之后 将upload下的文件删除
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常");
            return null;
        }
        return targetFile.getName();
    }
}

