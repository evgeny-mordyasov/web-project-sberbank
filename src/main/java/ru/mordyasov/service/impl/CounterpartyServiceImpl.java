package ru.mordyasov.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.repository.CounterpartyRepository;

import java.util.List;
import java.util.Optional;

/**
 * Класс CounterpartyServiceImpl, являющийся сервисом для работы с объектом класса Counterparty.
 * Данный класс делегирует CounterpartyRepository все необходимые операции для работы с базой данных.
 */
@Service
public class CounterpartyServiceImpl implements CounterpartyService {
    /**
     * Объект repository, совершающий всю работу с базой данных.
     */
    private CounterpartyRepository repository;

    @Autowired
    public CounterpartyServiceImpl(CounterpartyRepository repository) {
        this.repository = repository;
    }

    /**
     * Функция, позволяющая получить с базы данных контрагента с указанным идентификатором.
     * @param id - переданное значение идентификатора.
     * @returns возвращает объект с указанным идентификатором, если он есть. В ином случае будет возвращено Optional.empty().
     * @see CounterpartyRepository#findById(Object).
     */
    @Override
    public Optional<Counterparty> find(Long id) {
        return repository.findById(id);
    }

    /**
     * Процедура, позволяющая добавить контрагента в базу данных.
     * @param counterparty - контрагент, которого необходимо добавить в бд.
     * @see CounterpartyRepository#saveAndFlush(Object).
     */
    @Override
    @Transactional
    public void add(Counterparty counterparty) {
        repository.saveAndFlush(counterparty);
    }

    /**
     * Процедура, позволяющая удалить контрагента с базы данных.
     * Для удаления контрагента достаточно передать пустой объект с указанным идентификатором (поле id не null).
     * @param counterparty - переданный объект, который необходимо удалить.
     * @see CounterpartyRepository#delete(Object).
     */
    @Override
    @Transactional
    public void delete(Counterparty counterparty) {
        repository.delete(counterparty);
    }

    /**
     * Процедура, позволяющая обновить контрагента в базе данных.
     * Для обновления достаточно передать объект с указанным идентификатором и измененными полями.
     * @param counterparty - переданный объект, который содержится в базе данных и хранящий в себе изменения.
     * @see CounterpartyRepository#saveAndFlush(Object).
     */
    @Override
    @Transactional
    public void update(Counterparty counterparty) {
        repository.saveAndFlush(counterparty);
    }

    /**
     * Функция, позволяющая получить всех контрагентов с базы данных.
     * @returns возвращает список всех контрагентов, если они есть в базе данных.
     * В ином случае будет возвращен пустой список.
     * @see CounterpartyRepository#findAll().
     */
    @Override
    public List<Counterparty> findAll() {
        return repository.findAll();
    }

    /**
     * Функция, позволяющая получить контрагента по указанному наименованию.
     * @param name - наименование контрагента.
     * @returns возвращает контрагента с указанным наименованием, если он есть.
     * В ином случае будет возвращен Optional.empty().
     * @see CounterpartyRepository#findByNameIgnoreCase(String).
     */
    @Override
    public Optional<Counterparty> findByName(String name) {
        return repository.findByNameIgnoreCase(name);
    }

    /**
     * Функция, позволяющая получить всех контрагентов с указанными номер счетом и БИК.
     * @param accountNumber - номер счета контрагента.
     * @param BIC - БИК контрагента.
     * @returns список контрагентов с указанными номером счетом и БИК.
     * В ином случае, если найти в БД объекты с указанными параметрами не удалось, будет возвращен пустой список.
     * @see CounterpartyRepository#findByAccountNumberIgnoreCaseAndBIC(String, String).
     */
    @Override
    public List<Counterparty> findByAccountNumberAndBIC(String accountNumber, String BIC) {
        return repository.findByAccountNumberIgnoreCaseAndBIC(accountNumber, BIC);
    }
}