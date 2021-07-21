package ru.mordyasov.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

/**
 * Класс CounterpartyValidator, позволяющий проверить введенные поля контрагента (объекта класса Counterparty) на корректность.
 */
@Component
public class CounterpartyValidator implements Validator {
    /**
     * Сервис, позволяющий обращаться к базе данных для получения необходимой информации.
     */
    private CounterpartyService service;

    @Autowired
    public CounterpartyValidator(CounterpartyService service) {
        this.service = service;
    }

    /**
     * Функция, позволяющая определить, по отношению к какому объекту будет применяться текущая валидация.
     * В данном случае определено, что валидация поддерживает только класс Counterparty. Соотвественно,
     * валидация полей будет проходить только над объектами класса Counterparty.
     * @param aClass - какой класс пытаются валидировать.
     * @returns:
     * 1. false, если aClass не является Counterparty.class;
     * 2. true, если aClass есть Counterparty.class.
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return Counterparty.class.isAssignableFrom(aClass);
    }

    /**
     * Процедура, выполняющая процесс валидации (содержит логику проверки полей на корректность).
     * Валидация полей инкапсулирована в класс CounterpartyVerification.
     * @param o - объект, который проверяют на валидность. В данном случае определено, что класс CounterpartyValidator
     *          проводит проверку полей только объектов класса Counterparty. Соотвественно, приведение объекта Object o к классу
     *          Counterparty всегда является допустимым.
     * @param errors - объект, в который записываются все возникшие ошибки в ходе проверки на корректность.
     * @see CounterpartyVerification#perform().
     */
    @Override
    public void validate(Object o, Errors errors) {
        new CounterpartyVerification((Counterparty) o, errors, service)
                .perform();
    }
}