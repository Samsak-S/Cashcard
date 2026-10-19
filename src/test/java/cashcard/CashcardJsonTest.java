package cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.junit.jupiter.api.BeforeEach;


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest 
public class CashcardJsonTest {

    @Autowired
    private JacksonTester<Cashcard> json;
    
    @Autowired
    private JacksonTester<Cashcard[]> jsonList;

    Cashcard[] cashcards;

    @BeforeEach
    private void setCashCards() {
        cashcards = Arrays.array(
            new Cashcard(99L, 123.45),
            new Cashcard(100L, 1.00),
            new Cashcard(101L, 150.00)
        );
    }

    @Test 
    void cashcardSerializationTest() throws Exception {
        Cashcard cashcard = cashcards[0];
        assertThat(json.write(cashcard)).isStrictlyEqualToJson("single.json");
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

   @Test
   void cashcardSerializationListTest() throws Exception {
        assertThat(jsonList.write(cashcards)).isStrictlyEqualToJson("list.json");
        
   }
}