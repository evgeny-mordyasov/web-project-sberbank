package ru.mordyasov.controller.filters;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.List;

@RestController
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
    public ModelAndView search() {
        return new ModelAndView("catalog/filters/filters")
                .addObject("activeBic", "active")
                .addObject("none", "d-none");
    }

    @Operation(summary = "Выбран фильтр: по БИК и номеру счёта (отправлены значения)",
            description = "По введенным полям будет совершен поиск контрагента.")
    @ApiResponse(responseCode = "200", description = "Поиск успешно загрузился")
    @PostMapping
    public ModelAndView search(@RequestParam("account_number") String aN, @RequestParam("bic") String bic) {
        List<Counterparty> list = service.findByAccountNumberAndBIC(aN, bic);

        return new ModelAndView("catalog/filters/filters")
                .addObject("activeBic", "active")
                .addObject("counterparties",  list.isEmpty() ? null : list);
    }
}
