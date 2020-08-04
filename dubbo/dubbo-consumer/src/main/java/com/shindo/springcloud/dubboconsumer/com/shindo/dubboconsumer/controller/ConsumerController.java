package com.shindo.springcloud.dubboconsumer.com.shindo.dubboconsumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shindo.dubboapi.ServerAgent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 *
 */
@RestController
public class ConsumerController {
	@Reference(check = false)
	public ServerAgent serverAgent;

	@RequestMapping(value = "/uuid", method = RequestMethod.GET)
	public String uuid() {
		return serverAgent.formatUUID(UUID.randomUUID().toString());
	}
}
