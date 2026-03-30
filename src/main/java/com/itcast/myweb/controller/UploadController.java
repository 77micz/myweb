package com.itcast.myweb.controller;


import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.utils.OSS;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/myweb/common/upload")
@Slf4j
@Api(tags = "上传接口")
public class UploadController {// 上传


    // 上传
    @PostMapping
    @ApiOperation(value = "上传文件")
    public Result upload(MultipartFile file) {


        //检查文件是否存在
        if (file == null || file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }


        log.info("上传文件:{}", file.getOriginalFilename());

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
            log.error("上传文件错误信息:{}", e.getMessage());
            e.printStackTrace();
            return Result.error("上传文件错误");
        }

    }


}
