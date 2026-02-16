package com.itcast.myweb.controller;


import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.utils.OSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/tianmao/b/common/upload")
@Slf4j
public class UploadController {// 上传


    // 上传
    @PostMapping
    public Result upload(MultipartFile file){

        log.info("上传文件:{}",file.getOriginalFilename());

        //检查文件是否存在
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件内容
        byte[] content = null;
        try {
            content = file.getBytes();

            //上传文件
            String fileURL = OSS.uploadFile(content, fileName);
            return Result.ok(fileURL);

        } catch (Exception e) {
            log.error("上传文件错误信息:{}",e.getMessage());
            e.printStackTrace();
            return Result.error("上传文件错误");
        }

    }


}
