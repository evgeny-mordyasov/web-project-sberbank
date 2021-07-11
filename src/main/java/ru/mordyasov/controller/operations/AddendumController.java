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
        if (result.hasFieldErrors("name")) {
            model.addAttribute("validOrInvalidName", "is-invalid");
        } else {
            model.addAttribute("validOrInvalidName", "is-valid");
        }

        if (result.hasFieldErrors("TIN")) {
            model.addAttribute("validOrInvalidTin", "is-invalid");
        } else {
            model.addAttribute("validOrInvalidTin", "is-valid");
        }

        if (result.hasFieldErrors("TRR")) {
            model.addAttribute("validOrInvalidTrr", "is-invalid");
        } else {
            model.addAttribute("validOrInvalidTrr", "is-valid");
        }

        if (result.hasFieldErrors("accountNumber")) {
            model.addAttribute("validOrInvalidAc", "is-invalid");
        } else {
            model.addAttribute("validOrInvalidAc", "is-valid");
        }

        if (result.hasFieldErrors("BIC")) {
            model.addAttribute("validOrInvalidBic", "is-invalid");
        } else {
            model.addAttribute("validOrInvalidBic", "is-valid");
        }
    }
}
