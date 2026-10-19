package cashcard;

import java.net.URI;
import java.util.*;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    @PostMapping 
    public ResponseEntity<Void> createCashcard(@RequestBody Cashcard newCashcard, UriComponentsBuilder ucb) {
        Cashcard savedCashcard = repository.save(newCashcard);
        URI locationOfNewCashcard = ucb
            .path("/cashcards/{id}")
            .buildAndExpand(savedCashcard.id())
            .toUri();
        return ResponseEntity.created(locationOfNewCashcard).build();
    }

    @GetMapping()
    private ResponseEntity<Iterable<Cashcard>> findAll(Pageable pageable) {
        Page<Cashcard> page = repository.findAll(
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
            )
        );
        return ResponseEntity.ok(page.getContent());
    }
}
