package com.aptc.configuration;

import com.aptc.etc.JacksonObjectMapper;
import com.aptc.intercepter.JwtUserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

	private JwtUserInterceptor jwtUserInterceptor;

	public WebMvcConfiguration(JwtUserInterceptor jwtUserInterceptor) {
		this.jwtUserInterceptor = jwtUserInterceptor;
	}

	/**
	 * 消息转化器
	 * 用于时间等的格式化
	 * @param converters the list of configured converters to extend
	 */
	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		log.info("拓展消息转换器");
		//创建一个消息转换器对象
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		//为新的消息转换器设置映射
		converter.setObjectMapper(new JacksonObjectMapper());
		//将新的消息转换器加入到消息转换器容器中，并将其放在首位
		converters.add(0, converter);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*") // 允许任何来源
				.allowedMethods("*") // 允许任何HTTP方法
				.allowedHeaders("*") // 允许任何请求头
				.allowCredentials(false) // 不支持携带身份验证信息
				.maxAge(3600); // 预检请求的缓存时间
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		log.info("开始注册拦截器");
		registry.addInterceptor(jwtUserInterceptor)
				.addPathPatterns("/user/**")
				.excludePathPatterns("/user/user/login")
				.excludePathPatterns("/user/user/count");
	}
}
