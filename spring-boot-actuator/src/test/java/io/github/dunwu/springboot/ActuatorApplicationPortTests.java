package io.github.dunwu.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
	"management.server.port:0" })
public class ActuatorApplicationPortTests {

	@LocalServerPort
	private int port = 9010;

	@LocalManagementPort
	private int managementPort = 9011;

	@Test
	public void testHealth() {
		ResponseEntity<String> entity = new TestRestTemplate().withBasicAuth("user", getPassword())
			.getForEntity("http://localhost:" + this.managementPort + "/actuator/health",
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).contains("\"status\":\"UP\"");
	}

	private String getPassword() {
		return "password";
	}

	@Test
	public void testHome() {
		ResponseEntity<String> entity =
			new TestRestTemplate().getForEntity("http://localhost:" + this.port,
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void testMetrics() {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = new TestRestTemplate()
			.getForEntity("http://localhost:" + this.managementPort + "/actuator/metrics",
				Map.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

}
