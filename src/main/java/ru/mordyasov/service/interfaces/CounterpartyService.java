package ru.mordyasov.service.interfaces;

import ru.mordyasov.domain.Counterparty;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс CounterpartyService, являющийся дополнительным функционалом интерфейса AbstractService для работы с контрагентами.
 */
public interface CounterpartyService extends AbstractService<Counterparty, Long> {
    /**
     * Функция, позволяющая найти контрагента по указанному наименованию.
     * @param name - наименование контрагента.
     * @returns возвращает контрагента с указанным наименованием (name), если он есть. В ином случае Optional.empty().
     */
    Optional<Counterparty> findByName(String name);

    /**
     * Функция, позволяющая найти контрагента по указанному номеру счету и БИК.
     * @param accountNumber - номер счета контрагента.
     * @param BIC - БИК контрагента.
     * @returns возращает контрагента с указанными номером счета (accountNumber) и БИК (BIC), если он есть.
     * В ином случае будет возвращено Optional.empty().
     */
    List<Counterparty> findByAccountNumberAndBIC(String accountNumber, String BIC);
}
