package ru.mordyasov.controller.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.stream.Stream;

@Controller
@RequestMapping("/catalog/operations/add")
public class AddendumController {
    private CounterpartyService service;
    private Validator validator;

    @Autowired
    public AddendumController(CounterpartyService service, @Qualifier("counterpartyValidator") Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String add(Model model) {
        model.addAttribute("object", new Counterparty());

        return "catalog/operations/add";
    }

    @PostMapping
    public String add(@Validated @ModelAttribute("object") Counterparty counterparty, BindingResult result, Model model) {
        if (result.hasErrors()) {
            checkingFields(result, model);

            return "catalog/operations/add";
        }

        service.add(counterparty);

        return "redirect:/catalog";
    }

    private void checkingFields(BindingResult result, Model model) {
        Stream.of("name", "TIN", "TRR", "accountNumber", "BIC")
                .forEach(f ->
                        checking(f, result, model));
    }

    private void checking(String field, BindingResult result, Model model) {
        model.addAttribute("validOrInvalid" + field,
                result.hasFieldErrors(field) ?
                        "is-invalid" :
                        "is-valid");
    }
}
