package ru.mordyasov.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

import java.util.Arrays;

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
        Counterparty counterparty = (Counterparty) o;

        validationTheNameField(counterparty, errors);
        validationTheTinField(counterparty, errors);
        validationTheTrrField(counterparty, errors);
        validationTheAccountNumberField(counterparty, errors);
        validationTheBicField(counterparty, errors);
    }

    private void validationTheNameField(Counterparty c, Errors errors) {
        if (c.getName().isBlank()) {
            errors.rejectValue("name", "", "Наименование не должно быть пустым.");
        } else if (!c.getName().strip().equals(c.getName())) {
            errors.rejectValue("name", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного наименования.");
        } else if (c.getName().length() > 20) {
            errors.rejectValue("name", "", "Наименование не должно превышать 20 символов.");
        } else {
            boolean isDuplicateName = service.findAll()
                    .stream()
                    .anyMatch(obj ->
                            obj.getName().equals(c.getName())
                    );

            if (isDuplicateName) {
                errors.rejectValue("name", "", "Введенное наименование уже существует в справочнике.");
            }
        }
    }

    private void validationTheTinField(Counterparty c, Errors errors) {
        if (c.getTIN().isBlank()) {
            errors.rejectValue("TIN", "", "ИНН не должен быть пустым.");
        } else if (!c.getTIN().strip().equals(c.getTIN())) {
            errors.rejectValue("TIN", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного ИНН.");
        } else if (c.getTIN().length() < 10 || c.getTIN().length() > 12 || c.getTIN().length() == 11) {
            errors.rejectValue("TIN", "", "ИНН должен состоять из 10 или 12 цифр.");
        } else if (!MyStringUtils.isNumeric(c.getTIN())) {
            errors.rejectValue("TIN", "", "ИНН должен состоять только из цифр.");
        } else if (!checkingTheTin(c.getTIN())) {
            errors.rejectValue("TIN", "", "ИНН является некорректным.");
        }
    }

    private void validationTheTrrField(Counterparty c, Errors errors) {
        if (c.getTRR().isBlank()) {
            errors.rejectValue("TRR", "", "КПП не должен быть пустым.");
        } else if (!c.getTRR().strip().equals(c.getTRR())) {
            errors.rejectValue("TRR", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного КПП.");
        } else if (c.getTRR().length() != 9) {
            errors.rejectValue("TRR", "", "КПП должен состоять из 9 знаков.");
        }
    }

    private void validationTheAccountNumberField(Counterparty c, Errors errors) {
        if (c.getAccountNumber().isBlank()) {
            errors.rejectValue("accountNumber", "", "Номер счёта не должен быть пустым.");
        } else if (!c.getAccountNumber().strip().equals(c.getAccountNumber())) {
            errors.rejectValue("accountNumber", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного номера счёта.");
        } else if (c.getAccountNumber().length() != 20) {
            errors.rejectValue("accountNumber", "", "Номер счёта должен состоять из 20 цифр.");
        } else if (!MyStringUtils.isNumeric(c.getAccountNumber())) {
            errors.rejectValue("accountNumber", "", "Номер счёта должен состоять только из цифр.");
        } else if (c.getBIC().length() != 9) {
            errors.rejectValue("accountNumber", "", "Не удалось проверить правильность номера счёта. Введите корректно БИК.");
        } else if (!checkingTheAccountNumber(c.getAccountNumber(), c.getBIC())) {
            errors.rejectValue("accountNumber", "", "Номер счёта является некорректным.");
        }
    }

    private void validationTheBicField(Counterparty c, Errors errors) {
        if (c.getBIC().isBlank()) {
            errors.rejectValue("BIC", "", "БИК не должен быть пустым.");
        } else if (!c.getBIC().strip().equals(c.getBIC())) {
            errors.rejectValue("BIC", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного БИК.");
        } else if (c.getBIC().length() != 9) {
            errors.rejectValue("BIC", "", "БИК должен состоять из 9 цифр.");
        } else if (!MyStringUtils.isNumeric(c.getBIC())) {
            errors.rejectValue("BIC", "", "БИК должен состоять только из цифр.");
        }
    }

    private boolean checkingTheTin(String TIN) {
        int [] coefficients = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

        if (TIN.length() == 10) {
            return getControlNumberForTin(TIN, Arrays.copyOfRange(coefficients, 2, coefficients.length)) ==
                    Integer.parseInt(TIN.charAt(9) + "");
        } else {
            int first = getControlNumberForTin(TIN, Arrays.copyOfRange(coefficients, 1, coefficients.length));
            int second = getControlNumberForTin(TIN, coefficients);

            return first == Integer.parseInt(TIN.charAt(10) + "") &&
                    second == Integer.parseInt(TIN.charAt(11) + "");
        }
    }

    private boolean checkingTheAccountNumber(String an, String BIC) {
        int [] coefficients = {7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1};

        if (BIC.startsWith("00", 6)) {
            return getChecksum('0' + BIC.substring(4, 6) + an, coefficients) % 10 == 0;
        } else {
            return getChecksum(BIC.substring(0, 4) + an, coefficients) % 10 == 0;
        }
    }

    private int getControlNumberForTin(String TIN, int [] coefficients) {
        int controlNumber = getChecksum(TIN, coefficients) % 11;

        if (controlNumber > 9) {
            controlNumber %= 10;
        }

        return controlNumber;
    }

    private int getChecksum(String input, int [] coefficients) {
        int checksum = 0;

        for (int i = 0; i < coefficients.length; i++) {
            checksum += coefficients[i] * Integer.parseInt(
                    String.valueOf(
                            input.charAt(i)));
        }

        return checksum;
    }
}