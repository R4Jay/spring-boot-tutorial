package io.github.dunwu.springboot.security.config;

import io.github.dunwu.springboot.security.filter.ValidateCodeFilter;
import io.github.dunwu.springboot.security.handler.CustomAuthenticationFailureHandler;
import io.github.dunwu.springboot.security.handler.CustomAuthenticationSucessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationSucessHandler authenticationSucessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private ValidateCodeFilter validateCodeFilter;

	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
			.formLogin() // 表单登录
			// http.httpBasic() // HTTP Basic
			.loginPage("/authentication/require") // 登录跳转 URL
			.loginProcessingUrl("/login") // 处理表单登录 URL
			.successHandler(authenticationSucessHandler) // 处理登录成功
			.failureHandler(authenticationFailureHandler) // 处理登录失败
			.and().rememberMe().tokenRepository(persistentTokenRepository()) // 配置
			// token
			// 持久化仓库
			.tokenValiditySeconds(3600) // remember 过期时间，单为秒
			.userDetailsService(userDetailService) // 处理自动登录逻辑
			.and().authorizeRequests() // 授权配置
			.antMatchers("/authentication/require", "/login.html", "/css/*.css", "/code/image").permitAll() // 无需认证的请求路径
			.anyRequest() // 所有请求
			.authenticated() // 都需要认证
			.and().csrf().disable();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setCreateTableOnStartup(false);
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
