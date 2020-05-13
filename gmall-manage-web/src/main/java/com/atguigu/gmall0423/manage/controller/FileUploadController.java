package com.atguigu.gmall0423.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
public class FileUploadController {
    //http://192.168.25.220服务器的IP地址作为一个配置文件放入项目中！称作软编码！
    //@Value注解使用的前提条件是，当前类必须在spring容器内
    @Value("${fileServer.url}")
    private String fileUrl;//fileUrl="http://192.168.25.220"
    //获取上传文件需要获取springMVC技术
    @RequestMapping("fileUpload")
    public  String fileUpload(MultipartFile file) throws IOException, MyException {
        String imgUrl = fileUrl;
        //当文件不为空得时候，进行上传！
        if(file != null){
            String configFile = this.getClass().getResource("/tracker.conf").getFile();
            ClientGlobal.init(configFile);
            TrackerClient trackerClient=new TrackerClient();
            TrackerServer trackerServer=trackerClient.getTrackerServer();
            StorageClient storageClient=new StorageClient(trackerServer,null);
            //获取上传文件名称
            String originalFilename = file.getOriginalFilename();

            //获取文件的后缀名
            String extName = StringUtils.substringAfterLast(originalFilename, ".");
            String[] upload_file = storageClient.upload_file(file.getBytes(), extName, null);

            //String orginalFilename="D:\\IMG_0157.jpg";
           // String[] upload_file = storageClient.upload_file(originalFilename, "jpg", null);
            for (int i = 0; i < upload_file.length; i++) {
                String path = upload_file[i];
                System.out.println("path = " + path);
                imgUrl+= "/"+path;
            }
        }
        return imgUrl;
    }
}
