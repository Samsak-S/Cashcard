package cashcard;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate 
class CashcardApplicationTests {
	@Autowired 
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnACashcardWhenSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext context = JsonPath.parse(response.getBody());
		Number id = context.read("$.id");
		Number amount = context.read("$.amount");
		assertThat(id).isEqualTo(99);
		assertThat(amount).isEqualTo(123.45);
	}

	@Test 
	void shouldNotReturnACashcardWithUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/1000", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
