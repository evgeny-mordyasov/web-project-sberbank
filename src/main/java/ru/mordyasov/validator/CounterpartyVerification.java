package ru.mordyasov.validator;

import org.springframework.validation.Errors;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

import java.util.Arrays;

/**
 * Класс CounterpartyVerification, необходимый для верификации объектов класса Counterparty.
 * Он позволяет контролировать за состоянием объектов класса Counterparty, совершая определенные
 * манипуляции над проверками полей. Таким образом, этот класс способен создавать объекты класса Counterparty
 * только по необходимому шаблону, избегая наличие некорректных данных.
 */
class CounterpartyVerification implements Verification {
    /**
     * Объект, состояние которого пройдет верификацию.
     */
    private final Counterparty object;

    /**
     * Объект, фиксирующий все возникшие ошибки при верификации.
     */
    private final Errors errors;

    /**
     * Объект, необходимый для получения определенной информации из справочника контрагентов.
     */
    private final CounterpartyService service;

    public CounterpartyVerification(Counterparty object, Errors errors, CounterpartyService service) {
        this.object = object;
        this.errors = errors;
        this.service = service;
    }

    /**
     * Процедура, выполняющая основной процесс верификации полей объекта Counterparty.
     * @see #verificationTheNameField(Counterparty, Errors).
     * @see #verificationTheTinField(Counterparty, Errors).
     * @see #verificationTheTrrField(Counterparty, Errors).
     * @see #verificationTheAccountNumberField(Counterparty, Errors).
     * @see #verificationTheBicField(Counterparty, Errors).
     */
    @Override
    public void perform() {
        verificationTheNameField(object, errors);
        verificationTheTinField(object, errors);
        verificationTheTrrField(object, errors);
        verificationTheAccountNumberField(object, errors);
        verificationTheBicField(object, errors);
    }

    /**
     * Процедура, совершающая верификацию поля name.
     * Объект, проходящий верификацию, является валидным (поля являются корректными), если:
     * 1) Наименование контрагента является не пустым;
     * 2) Наименование контрагента не содержит невидимые символы, пробелы в начале или конце;
     * 3) Наименование контрагента не превышает 20 символов;
     * 4) Наименование контрагента является уникальным (не существует уже в справочнике).
     * Если не выполнено хотя бы одно условие, то считается, что объект не прошел верификацию.
     * @param c - объект, который проходит верификацию.
     * @param errors - объект, необходимый для фиксирования ошибок.
     */
    private void verificationTheNameField(Counterparty c, Errors errors) {
        if (c.getName().isBlank()) {
            errors.rejectValue("name", "", "Наименование не должно быть пустым.");
        } else if (!c.getName().strip().equals(c.getName())) {
            errors.rejectValue("name", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного наименования.");
        } else if (c.getName().length() > 20) {
            errors.rejectValue("name", "", "Наименование не должно превышать 20 символов.");
        } else {
            service.findByName(c.getName()).ifPresent(o -> {
                if (!o.getId().equals(c.getId())) {
                    errors.rejectValue("name", "", "Введенное наименование уже существует в справочнике.");
                }
            });
        }
    }

    /**
     * Процедура, совершающая верификацию поля TIN.
     * Объект, проходящий верификацию, является валидным (поля являются корректными), если:
     * 1) ИНН контрагента является не пустым;
     * 2) ИНН контрагента не содержит невидимые символы, пробелы в начале или конце;
     * 3) ИНН контрагента состоит из 10 или 12 цифр;
     * 4) ИНН контрагента содержит только цифры;
     * 5) ИНН контрагента является действительным (корректным).
     * Если не выполнено хотя бы одно условие, то считается, что объект не прошел верификацию.
     * @param c - объект, который проходит верификацию.
     * @param errors - объект, необходимый для фиксирования ошибок.
     * @see MyStringUtils#isNumeric(String).
     * @see #checkingTheTin(String).
     */
    private void verificationTheTinField(Counterparty c, Errors errors) {
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


    /**
     * Процедура, совершающая верификацию поля TRR.
     * Объект, проходящий верификацию, является валидным (поля являются корректными), если:
     * 1) КПП контрагента является не пустым;
     * 2) КПП контрагента не содержит невидимые символы, пробелы в начале или конце;
     * 3) КПП контрагента состоит из 9 знаков;
     * 4) КПП контрагента содержит только цифры (не считая 5 и 6 знаки, т.к они могут быть буквами);
     * 5) КПП контрагента на 5 и 6 позициях состоит либо только из цифр, либо только из латинских букв.
     * Если не выполнено хотя бы одно условие, то считается, что объект не прошел верификацию.
     * @param c - объект, который проходит верификацию.
     * @param errors - объект, необходимый для фиксирования ошибок.
     * @see MyStringUtils#isLatinLetters(String).
     * @see MyStringUtils#isNumeric(String).
     */
    private void verificationTheTrrField(Counterparty c, Errors errors) {
        if (c.getTRR().isBlank()) {
            errors.rejectValue("TRR", "", "КПП не должен быть пустым.");
        } else if (!c.getTRR().strip().equals(c.getTRR())) {
            errors.rejectValue("TRR", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного КПП.");
        } else if (c.getTRR().length() != 9) {
            errors.rejectValue("TRR", "", "КПП должен состоять из 9 знаков.");
        } else if (!MyStringUtils.isNumeric(c.getTRR().substring(0, 4).concat(c.getTRR().substring(6)))) {
            errors.rejectValue("TRR", "", "КПП должен состоять из цифр (5 и 6 знаки могут быть буквами).");
        } else if (!MyStringUtils.isLatinLetters(c.getTRR().substring(4, 6)) && !MyStringUtils.isNumeric(c.getTRR().substring(4, 6))) {
            errors.rejectValue("TRR", "", "5 и 6 знаки КПП должны быть латинскими буквами.");
        }
    }

    /**
     * Процедура, совершающая верификацию поля accountNumber.
     * Объект, проходящий верификацию, является валидным (поля являются корректными), если:
     * 1) Номер счета контрагента является не пустым;
     * 2) Номер счета контрагента не содержит невидимые символы, пробелы в начале или конце;
     * 3) Номер счета контрагента состоит из 20 цифр;
     * 4) Номер счета контрагента содержит только цифры;
     * 5) БИК контрагента введен корректно;
     * 6) Номер счета контрагента является действительным (корректным).
     * Если не выполнено хотя бы одно условие, то считается, что объект не прошел верификацию.
     * @param c - объект, который проходит верификацию.
     * @param errors - объект, необходимый для фиксирования ошибок.
     * @see MyStringUtils#isNumeric(String).
     * @see #checkingTheAccountNumber(String, String).
     */
    private void verificationTheAccountNumberField(Counterparty c, Errors errors) {
        if (c.getAccountNumber().isBlank()) {
            errors.rejectValue("accountNumber", "", "Номер счёта не должен быть пустым.");
        } else if (!c.getAccountNumber().strip().equals(c.getAccountNumber())) {
            errors.rejectValue("accountNumber", "", "Уберите лишние проблемы/невидимые символы в начале или в конце введенного номера счёта.");
        } else if (c.getAccountNumber().length() != 20) {
            errors.rejectValue("accountNumber", "", "Номер счёта должен состоять из 20 цифр.");
        } else if (!MyStringUtils.isNumeric(c.getAccountNumber())) {
            errors.rejectValue("accountNumber", "", "Номер счёта должен состоять только из цифр.");
        } else if (c.getBIC().length() != 9 || !MyStringUtils.isNumeric(c.getBIC())) {
            errors.rejectValue("accountNumber", "", "Не удалось проверить правильность номера счёта. Введите корректно БИК.");
        } else if (!checkingTheAccountNumber(c.getAccountNumber(), c.getBIC())) {
            errors.rejectValue("accountNumber", "", "Номер счёта является некорректным.");
        }
    }

    /**
     * Процедура, совершающая верификацию поля BIC.
     * Объект, проходящий верификацию, является валидным (поля являются корректными), если:
     * 1) БИК контрагента является не пустым;
     * 2) БИК контрагента не содержит невидимые символы, пробелы в начале или конце;
     * 3) БИК контрагента состоит из 9 цифр;
     * 4) БИК контрагента содержит только цифры.
     * Если не выполнено хотя бы одно условие, то считается, что объект не прошел верификацию.
     * @param c - объект, который проходит верификацию.
     * @param errors - объект, необходимый для фиксирования ошибок.
     * @see MyStringUtils#isNumeric(String).
     */
    private void verificationTheBicField(Counterparty c, Errors errors) {
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

    /**
     * Функция, необходимая для того, чтобы проверить корректность ИНН.
     * Если ИНН состоит из 10 цифр, то вычисляется контрольное число, справнивающееся с последней цифрой ИНН. В случае
     * совпадения ИНН считается корректным.
     * Если ИНН состоит из 12 цифр, то вычисляются два контрольных числа, которые сравниваются с последними цифрами ИНН.
     * В случае двух совпадений ИНН считается корректным.
     * @param TIN - ИНН, проходящий проверку.
     * @returns:
     * 1) true, если ИНН прошел проверку на корректность;
     * 2) false в ином случае.
     * @see #getControlNumberForTin(String, int[]).
     */
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

    /**
     * Функция, необходимая для проверки номера счета на корректность.
     * Контрольная сумма для рассчетного и корреспондентского счета выполяются по-разному, но их результат должен быть
     * равен нулю по модулю 10.
     * @param an - номер счета контрагента.
     * @param BIC - БИК контрагента.
     * @returns:
     * 1) true, если номер счета прошел проверку на корректность;
     * 2) false в ином случае.
     * @see #getChecksum(String, int[]).
     */
    private boolean checkingTheAccountNumber(String an, String BIC) {
        int [] coefficients = {7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1};
        String lastThreeDigitsOfBIC = BIC.substring(BIC.length() - 3);

        if (an.startsWith("301") && an.endsWith(lastThreeDigitsOfBIC)) {
            return getChecksum('0' + BIC.substring(4, 6) + an, coefficients) % 10 == 0;
        } else {
            return getChecksum(lastThreeDigitsOfBIC + an, coefficients) % 10 == 0;
        }
    }

    /**
     * Функция, возвращающая контрольное число для ИНН путем вычисления контрольной суммы, преобразованной по модулю 11.
     * Если все же контрольная сумма является двузначной, то происходит преобразование по модулю 10.
     * @param TIN - ИНН, проходящий проверку.
     * @param coefficients - необходимые коэффициенты для вычисления контрольной суммы.
     * @return возвращает контрольное число.
     * @see #getChecksum(String, int[]).
     */
    private int getControlNumberForTin(String TIN, int [] coefficients) {
        int controlNumber = getChecksum(TIN, coefficients) % 11;

        if (controlNumber > 9) {
            controlNumber %= 10;
        }

        return controlNumber;
    }

    /**
     * Функция, вычисляющая контрольную сумму для определенного ИНН при постоянных коэффициентов.
     * @param input - ИНН, проходящий проверку.
     * @param coefficients - необходимые коэффициенты для вычисления контрольной суммы.
     * @return возвращает контрольную сумму ИНН.
     */
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
