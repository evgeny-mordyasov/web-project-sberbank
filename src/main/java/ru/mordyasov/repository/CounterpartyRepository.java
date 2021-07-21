package ru.mordyasov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mordyasov.domain.Counterparty;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс CounterpartyRepository, являющийся своего рода DAO-слоем.
 * Благодаря данному интерфейсу происходит обращение к базе данных.
 */
@Repository
public interface CounterpartyRepository extends JpaRepository<Counterparty, Long> {
    /**
     * Функция, позволяющая найти в базе данных контрагента по наименованию.
     * @param name - наименование контрагента.
     * @returns - найденный контрагент с указанным наименованием (name).
     */
    Optional<Counterparty> findByNameIgnoreCase(String name);

    /**
     * Функция, позволяющая найти в базе данных контрагентов по номеру счёта и БИК.
     * @param accountNumber - номер счета контрагента.
     * @param BIC - БИК контрагента.
     * @returns список контрагентов с указанными номером счета (accountNumber) и БИК (BIC).
     */
    List<Counterparty> findByAccountNumberIgnoreCaseAndBIC(String accountNumber, String BIC);
}
