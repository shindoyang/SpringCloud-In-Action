package com.shindo.springcloud.eurekaclient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public String hello() {
		System.out.println("come in client");
		return "hello, spring cloud Gateway";
	}
}
