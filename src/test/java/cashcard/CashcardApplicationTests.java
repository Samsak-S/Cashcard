package cashcard;

import static org.assertj.core.api.Assertions.assertThat;

import java.beans.Transient;
import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import net.minidev.json.JSONArray;

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

	@Test 
	@DirtiesContext
	void shouldCreateNewCashCard() {
		Cashcard newCashcard = new Cashcard(null, 250.00);
		ResponseEntity<Void> response = restTemplate.postForEntity("/cashcards", newCashcard, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		URI locationOfNewCashcard = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashcard, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext context = JsonPath.parse(getResponse.getBody());
		Number id = context.read("$.id");
		Number amount = context.read("$.amount");
		assertThat(id).isNotNull();
		assertThat(amount).isEqualTo(250.00);
	}

	@Test
	void shouldReturnAllCashcardsAsList() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext context = JsonPath.parse(response.getBody());
		Number size = context.read("$.size()");
		assertThat(size).isEqualTo(3);

		JSONArray ids = context.read("$..id");
		assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);
	
		JSONArray amounts = context.read("$..amount");
		assertThat(amounts).containsExactlyInAnyOrder(123.45, 1.00, 150.00);
	}

	@Test
	void shouldReturnAPageOfCashcards() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards?page=01&size=1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldReturnASortedPageOfCashCards() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards?page=0&size=1&sort=amount,desc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$[*]");
		assertThat(read.size()).isEqualTo(1);

		double amount = documentContext.read("$[0].amount");
		assertThat(amount).isEqualTo(150.00);
	}

	@Test
	void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext context = JsonPath.parse(response.getBody());
		JSONArray page = context.read("$.[*]");
		assertThat(page.size()).isEqualTo(3);

		JSONArray amounts = context.read("$..amount");
		assertThat(amounts).containsExactly(1.00, 123.45, 150.00);
	}

}
