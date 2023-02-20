package com.itheima.pinda.controller;


import com.itheima.pinda.feign.AttachmentClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 运单表 前端控制器
 * </p>
 *
 * @author diesel
 * @since 2020-03-19
 */
@Slf4j
@Api(tags = "文件上传")
@Controller
@RequestMapping("attachment")
public class AttachmentController {

    private final AttachmentClient attachmentClient;


    public AttachmentController(AttachmentClient attachmentClient) {
        this.attachmentClient = attachmentClient;
    }

    @ApiOperation(value = "文件上传")
    @ResponseBody
    @PostMapping("upload")
    public Object upload(@RequestParam(value = "file") MultipartFile file) {
        log.info("上传附件");
        return attachmentClient.upload(file, false, null, null, "customer");
    }
}
