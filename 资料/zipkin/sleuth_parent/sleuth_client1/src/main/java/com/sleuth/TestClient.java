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
 * trace-1 调用trace-2
 */
@RestController
public class TestClient {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/trace-1")
	@ResponseBody
	public String test(HttpServletRequest request) {
		/*logger.info("===<call trace-1, TraceId={}, SpanId={}>===", request.getHeader("X-B3-TraceId"),
				request.getHeader("X-B3-SpanId"));*/
		return restTemplate.getForEntity("http://localhost:9102/trace-2", String.class).getBody();
	}

}
