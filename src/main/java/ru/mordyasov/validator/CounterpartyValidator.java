package ru.mordyasov.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

@Component
public class CounterpartyValidator implements Validator {
    private CounterpartyService service;

    @Autowired
    public CounterpartyValidator(CounterpartyService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Counterparty.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        new CounterpartyVerification((Counterparty) o, errors, service)
                .perform();
    }
}