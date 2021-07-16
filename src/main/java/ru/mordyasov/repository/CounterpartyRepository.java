package ru.mordyasov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mordyasov.domain.Counterparty;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounterpartyRepository extends JpaRepository<Counterparty, Long> {
    Optional<Counterparty> findByNameIgnoreCase(String name);
    List<Counterparty> findByAccountNumberIgnoreCaseAndBIC(String accountNumber, String BIC);
}
