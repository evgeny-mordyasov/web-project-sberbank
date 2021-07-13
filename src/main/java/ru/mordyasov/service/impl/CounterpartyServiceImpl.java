package ru.mordyasov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.repository.CounterpartyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CounterpartyServiceImpl implements CounterpartyService {
    private CounterpartyRepository repository;

    @Autowired
    public CounterpartyServiceImpl(CounterpartyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Counterparty> find(Long id) {
        return repository.findById(id);
    }

    @Override
    public void add(Counterparty counterparty) {
        repository.saveAndFlush(counterparty);
    }

    @Override
    public void delete(Counterparty counterparty) {
        repository.delete(counterparty);
    }

    @Override
    public void update(Counterparty counterparty) {
        repository.saveAndFlush(counterparty);
    }

    @Override
    public List<Counterparty> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Counterparty> findByName(String name) {
        List<Counterparty> list = repository.findByNameIgnoreCase(name);

        return list.size() == 1 ?
                Optional.of(list.get(0)) :
                Optional.empty();
    }

    @Override
    public Optional<Counterparty> findByAccountNumberAndBIC(String accountNumber, String BIC) {
        List<Counterparty> list = repository.findByAccountNumberIgnoreCaseAndBIC(accountNumber, BIC);

        return list.size() == 1 ?
                Optional.of(list.get(0)) :
                Optional.empty();
    }
}
