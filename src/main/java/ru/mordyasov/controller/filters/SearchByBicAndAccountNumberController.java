package ru.mordyasov.controller.filters;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.List;
import java.util.Map;

/**
 * Класс SearchByBicAndAccountNumberController, необходимый для поиска контрагента из справочника по номеру счета и БИК.
 */
@Controller
@RequestMapping("/catalog/filters/search_by_BIC_and_aN")
public class SearchByBicAndAccountNumberController {
    private CounterpartyService service;

    @Autowired
    public SearchByBicAndAccountNumberController(CounterpartyService service) {
        this.service = service;
    }

    @Operation(summary = "Выбран фильтр: по БИК и номеру счёта",
            description = "Предоставляются два поля для ввода данных.")
    @ApiResponse(responseCode = "200", description = "Поиск успешно загрузился")
    @GetMapping
    public String search(Model model) {
        model.addAllAttributes(Map.of(
                "activeBic", "active",
                "none", "d-none"
        ));

        return "catalog/filters/filters";
    }

    @Operation(summary = "Выбран фильтр: по БИК и номеру счёта (отправлены значения)",
            description = "По введенным полям будет совершен поиск контрагента.")
    @ApiResponse(responseCode = "200", description = "Поиск успешно загрузился")
    @PostMapping
    public String search(@RequestParam("account_number") String aN, @RequestParam("bic") String bic, Model model) {
        List<Counterparty> list = service.findByAccountNumberAndBIC(aN, bic);

        model.addAttribute("activeBic", "active");
        model.addAttribute("counterparties",  list.isEmpty() ? null : list);

        return "catalog/filters/filters";
    }
}
