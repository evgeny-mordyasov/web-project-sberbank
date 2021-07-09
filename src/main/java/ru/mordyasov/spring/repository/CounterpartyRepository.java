package ru.mordyasov.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mordyasov.domain.Counterparty;

import java.util.List;

@Repository
public interface CounterpartyRepository extends JpaRepository<Counterparty, Long> {
    List<Counterparty> findByNameIgnoreCase(String name);
    List<Counterparty> findByAccountNumberIgnoreCaseAndBIC(String accountNumber, String BIC);
}
