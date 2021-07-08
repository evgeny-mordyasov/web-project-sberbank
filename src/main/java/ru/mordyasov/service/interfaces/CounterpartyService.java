package ru.mordyasov.service.interfaces;

import ru.mordyasov.domain.Counterparty;

import java.util.Optional;

public interface CounterpartyService extends AbstractService<Counterparty, Long> {
    Optional<Counterparty> findByName(String name);
}
