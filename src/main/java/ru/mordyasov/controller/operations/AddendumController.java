package ru.mordyasov.controller.operations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

/**
 * Класс AddendumController, содержащий информацию по заполнению данных, а также форму для создания контрагента.
 */
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

    @Operation(summary = "Форма для заполнения", description = "Предоставляется небольшая формочка с полями, которые необходимо заполнить для добавления.")
    @ApiResponse(responseCode = "200", description = "Форма успешно открылась")
    @GetMapping
    public String add(Model model) {
        model.addAttribute("object", new Counterparty());

        return "catalog/operations/add";
    }

    @Operation(summary = "Отправка данных", description = "Отправление данных на проверку заполненных полей")
    @ApiResponse(responseCode = "200", description = "Все введенно корректно")
    @PostMapping
    public String add(@Validated @ModelAttribute("object") Counterparty counterparty, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("activeValidator", "true");

            return "catalog/operations/add";
        }

        service.add(counterparty);

        return "redirect:/catalog";
    }
}
