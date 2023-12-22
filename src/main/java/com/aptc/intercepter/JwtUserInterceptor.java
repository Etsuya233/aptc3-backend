package com.aptc.intercepter;

import com.aptc.configuration.JwtProperties;
import com.aptc.utils.BaseContext;
import com.aptc.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtUserInterceptor implements HandlerInterceptor {
	private JwtProperties jwtProperties;

	public JwtUserInterceptor(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//假如请求的不是Controller方法，就直接放行
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}


		/**
		 * 参数中的handler参数是什么？
		 * 在 Spring 框架中，handler 通常指的是处理请求的对象，它可以是一个控制器（Controller）类中的处理方法。
		 * 在 Spring MVC 中，请求到达 DispatcherServlet 后，
		 * DispatcherServlet 会根据请求的 URL 查找匹配的处理方法（handler）。这个处理方法就是 handler。
		 */

		String token = "";
		token = request.getHeader("Authorization");
		if(token != null) token = token.substring(7);//去掉“Bearer ”

		try{
			log.info("jwt校验：{}", token);
			Claims claims = JwtUtils.parse(jwtProperties.getUserKeyObj(), token);
			String userIdStr = (String) claims.get("currentID");
			int userId = Integer.parseInt(userIdStr);
			log.info("当前用户ID：{}", userId);

			BaseContext.setCurrentId(userId);
		} catch (JwtException ex){
			response.setStatus(401);
			log.info("用户登录失败：{}", token);
			return false;
		}

		return true;
	}
}
