package com.shindo.springcloud.eurekaclient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author:
 * @Description:
 * @Date: Create in 2020/7/8
 * @Modified By:
 * @Modified Date:
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

	@RequestMapping("/rateLimit")
	public String hello(HttpServletRequest request) {
		System.out.println("come in client");
		Object appKey_attr = request.getAttribute("appkey");
		System.out.println("request attribute = " + appKey_attr);
		String appkey = request.getHeader("appkey");
		System.out.println("request header appkey = " + appkey);
		String appKey = request.getHeader("appKey");
		System.out.println("request header appKey = " + appKey);

		return "hello, spring cloud Gateway";
	}
}
