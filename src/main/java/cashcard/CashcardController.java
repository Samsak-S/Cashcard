package cashcard;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/cashcards")
public class CashcardController {

    private CashcardRepository repository;

    private CashcardController(CashcardRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cashcard> getId(@PathVariable Long id) {
        Optional<Cashcard> cashcardOptional = repository.findById(id);
        if(cashcardOptional.isPresent()) {
            return ResponseEntity.ok(cashcardOptional.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
