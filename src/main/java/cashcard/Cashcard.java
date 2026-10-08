package cashcard;

import org.springframework.data.annotation.Id;

public record Cashcard(@Id Long id, Double amount) {}