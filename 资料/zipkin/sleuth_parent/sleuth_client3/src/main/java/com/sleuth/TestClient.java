package com.sleuth;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *trace-3 直接返回
 */
@RestController
public class TestClient {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/trace-3")
	@ResponseBody
	public String test(HttpServletRequest request) {
		/*logger.info("===<call trace-3, TraceId={}, SpanId={}>===", request.getHeader("X-B3-TraceId"),
				request.getHeader("X-B3-SpanId"));*/
		//链路异常测试
		//System.out.println(1/0);
		return "Trace";
	}

}
