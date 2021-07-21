package ru.mordyasov.controller.filters;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.Map;

/**
 * Класс SearchByNameController, необхоимый для поиска контрагента из справочника по наименованию.
 */
@Controller
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
    public String search(Model model) {
        model.addAllAttributes(Map.of(
                "activeN", "active",
                "none", "d-none"
        ));

        return "catalog/filters/filters";
    }

    @Operation(summary = "Выбран фильтр: по наименованию (отправлено значение)",
            description = "По введенному полю будет совершен поиск контрагента.")
    @ApiResponse(responseCode = "200", description = "Поиск успешно загрузился")
    @PostMapping
    public String search(@RequestParam("name") String name, Model model) {
        model.addAttribute("activeN", "active");
        model.addAttribute("counterparty",  service.findByName(name).orElse(null));

        return "catalog/filters/filters";
    }
}
