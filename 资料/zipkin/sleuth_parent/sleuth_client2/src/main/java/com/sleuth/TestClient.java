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
 * trace-2 调用 trace-3
 */
@RestController
public class TestClient {

	final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/trace-2")
	@ResponseBody
	public String test(HttpServletRequest request) {
		/*logger.info("===<call trace-2, TraceId={}, SpanId={}>===", request.getHeader("X-B3-TraceId"),
				request.getHeader("X-B3-SpanId"));*/
		return restTemplate.getForEntity("http://localhost:9103/trace-3", String.class).getBody();
	}

}
