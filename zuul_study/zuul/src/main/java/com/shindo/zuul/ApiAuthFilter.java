package com.shindo.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试网关api鉴权
 * 在zuul里面，服务间的路由是yml中的route-->serviceId指定的
 * 在Filter中的处理更多的是针对URI，即接口地址的转发。
 * 换句话说：
 * yml配置处理了请求路径到指定服务的路由，
 * Filter可以处理请求接口间的跳转。
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
		urlMap.put("eurekB", "");
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
		//粗暴替换
		//需添加前置判断条件
//		request.setAttribute("appKey", "shindo");
//		ctx.setRequest(request);
		ctx.addZuulRequestHeader("appKey", "shindo");
		//注意，上面的方法无法实现路由转发，但是可以在request中新增参数，只有下面这个方法可以
		ctx.put(FilterConstants.REQUEST_URI_KEY, "/hello/rateLimit");
		//精准替换
		/*String[] split = url.split("/", 3);    // 这里切割一下,好让下面判断是否是需要修改url的.
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
		}*/

		//在连贯逻辑中，如果某一步出现异常，需要终止转发，响应前端异常信息，可以采用下面的方法：
		/*ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(409);
		ctx.setResponseBody("测试终止服务！");
		ctx.getResponse().setContentType("text/html;charset=utf-8");*/
		return null;
	}

}
