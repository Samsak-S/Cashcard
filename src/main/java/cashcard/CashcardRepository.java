package cashcard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CashcardRepository extends PagingAndSortingRepository<Cashcard, Long>, CrudRepository<Cashcard, Long>{
    
}
