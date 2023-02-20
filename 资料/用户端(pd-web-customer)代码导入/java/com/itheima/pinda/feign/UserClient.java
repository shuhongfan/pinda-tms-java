package com.itheima.pinda.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
        name = "userClient",
        url = "${client.user.url}"
)
public interface UserClient {

    @PostMapping("auth/query/id")
    Map findById(@RequestParam(value = "id", required = false) String id);

}
