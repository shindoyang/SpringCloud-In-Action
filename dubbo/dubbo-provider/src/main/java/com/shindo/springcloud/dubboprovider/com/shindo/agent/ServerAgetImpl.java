package com.shindo.springcloud.dubboprovider.com.shindo.agent;

import com.alibaba.dubbo.config.annotation.Service;
import com.shindo.dubboapi.ServerAgent;
import org.springframework.stereotype.Component;

/**
 * @Author: 杨耿
 * @Description:
 * @Date: Create in 2020/8/4
 * @Modified By:
 * @Modified Date:
 */
@Service(interfaceClass = ServerAgent.class)
@Component
public class ServerAgetImpl implements ServerAgent {

	@Override
	public String formatUUID(String uuid) {
		System.out.println("come in provider");
		return uuid.replace("-", "!@#");
	}
}
