package cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest 
public class CashcardJsonTest {

    @Autowired
    private JacksonTester<Cashcard> json;

    @Test
    void firstTest() {
        assertThat(1).isEqualTo(1);
    }

    @Test 
    void cashcardSerializationTest() throws Exception {
        Cashcard cashcard = new Cashcard(99L, 123.45);
        assertThat(json.write(cashcard)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(cashcard)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(cashcard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(json.write(cashcard)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(cashcard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
   }

   @Test 
   void cashcardDeserializationTest() throws Exception{
        String expected = """
        {
            "id": 99,
            "amount": 123.45
        }
        """;
        assertThat(json.parse(expected)).isEqualTo(new Cashcard(99L, 123.45));
        assertThat(json.parseObject(expected).id()).isEqualTo(99);
        assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
   }
}