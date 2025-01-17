package io.github.dunwu.springboot;

import io.github.dunwu.springboot.data.User;
import io.github.dunwu.springboot.data.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

@SpringBootApplication
public class SpringBootDataJdbcApplication implements CommandLineRunner {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final UserDao userDao;

	public SpringBootDataJdbcApplication(UserDao userDao) {
		this.userDao = userDao;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJdbcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (userDao != null) {
			printDataSourceInfo(userDao.getJdbcTemplate());
			log.info("连接数据源成功！");
		} else {
			log.error("连接数据源失败！");
			return;
		}

		List<User> list = userDao.list();
		list.forEach(item -> {
			log.info(item.toString());
		});
	}

	public void printDataSourceInfo(JdbcTemplate jdbcTemplate) throws SQLException {

		DataSource dataSource = jdbcTemplate.getDataSource();

		Connection connection;
		if (dataSource != null) {
			connection = dataSource.getConnection();
		} else {
			log.error("获取 DataSource 失败");
			return;
		}

		if (connection != null) {
			log.info("DB URL: {}", connection.getMetaData().getURL());
		} else {
			log.error("获取 Connection 失败");
		}
	}

}
