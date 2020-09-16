package com.shindo.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试网关api鉴权
 */
@Component
public class ApiAuthFilter extends ZuulFilter {
	/**
	 * pre：可以在请求被路由之前调用
	 * route：在路由请求时候被调用
	 * post：在route和error过滤器之后被调用
	 * error：处理请求时发生错误时被调用
	 */
	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 重定向的规则,根据key来重定向到val.
	 */
	private static Map<String, String> urlMap = new HashMap<>();

	static {
		urlMap.put("eurekB", "/eurekA/");
	}

	/**
	 * 修改转发url
	 */
	@Override
	public Object run() {
		System.out.println("========== come in apiFilter ==========");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String url = request.getRequestURI(); // 列子 [/user/login/loginWx]
		String[] split = url.split("/", 3);    // 这里切割一下,好让下面判断是否是需要修改url的.
		for (int i = 0; i < split.length; i++) {
			System.out.println(i + "-----------" + split[i]);
		}
		if (split.length >= 2) {
			String val = urlMap.get(split[1]);
			if (StringUtils.isNotEmpty(val)) {
				url = url.replaceFirst("/" + split[1] + "/", val);// 根据配置好的去将url替换掉,这里可以写自己的转换url的规则
				System.out.println("==============" + url);
//				ctx.put(FilterConstants.REQUEST_URI_KEY, url); // 将替换掉的url set进去,在对应的转发请求的url就会使用这个url
				request.setAttribute("requestURI", url);
				ctx.setRequest(request);
			}
		}
		return null;
	}

}
