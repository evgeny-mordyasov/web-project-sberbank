package ru.mordyasov.controller.operations;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.stream.Stream;

class FieldValidator {
    public static void checkingFields(BindingResult result, Model model) {
        Stream.of("name", "TIN", "TRR", "accountNumber", "BIC")
                .forEach(f ->
                        checking(f, result, model));
    }

    private static void checking(String field, BindingResult result, Model model) {
        model.addAttribute("validOrInvalid" + field,
                result.hasFieldErrors(field) ?
                        "is-invalid" :
                        "is-valid");
    }
}
