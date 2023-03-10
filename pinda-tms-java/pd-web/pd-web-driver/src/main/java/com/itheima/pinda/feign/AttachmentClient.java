package com.itheima.pinda.feign;

import com.itheima.pinda.authority.api.hystrix.UserApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "${pinda.feign.authority-server:pd-file-server}",
        fallback = UserApiFallback.class,
        path = "/attachment"
)
public interface AttachmentClient {


    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    Object upload(
            @RequestPart(value = "file") MultipartFile file,
            @RequestParam(value = "isSingle", required = false, defaultValue = "false") Boolean isSingle,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "bizId", required = false) String bizId,
            @RequestParam(value = "bizType", required = false) String bizType);

}
