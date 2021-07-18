package ru.mordyasov.controller.filters;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.service.interfaces.CounterpartyService;

@RestController
@RequestMapping("/catalog/filters/search_by_name")
public class SearchByNameController {
    private CounterpartyService service;

    @Autowired
    public SearchByNameController(CounterpartyService service) {
        this.service = service;
    }

    @Operation(summary = "Выбран фильтр: по наименованию",
            description = "Предоставлется поле для ввода данных.")
    @ApiResponse(responseCode = "200", description = "Поиск успешно загрузился")
    @GetMapping
    public ModelAndView search() {
        return new ModelAndView("catalog/filters/filters")
                .addObject("activeN", "active")
                .addObject("none", "d-none");
    }

    @Operation(summary = "Выбран фильтр: по наименованию (отправлено значение)",
            description = "По введенному полю будет совершен поиск контрагента.")
    @ApiResponse(responseCode = "200", description = "Поиск успешно загрузился")
    @PostMapping
    public ModelAndView search(@RequestParam("name") String name) {
        return new ModelAndView("catalog/filters/filters")
                .addObject("activeN", "active")
                .addObject("counterparty",  service.findByName(name).orElse(null));
    }
}
