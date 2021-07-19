package ru.mordyasov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mordyasov.service.interfaces.CounterpartyService;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    private CounterpartyService service;

    @Autowired
    public CatalogController(CounterpartyService service) {
        this.service = service;
    }

    @Operation(summary = "Список всех контрагентов",
            description = "Предоставляется возможность ознакомиться со всей информацией о контрагенте.")
    @ApiResponse(responseCode = "200", description = "Каталог найден")
    @GetMapping()
    public String catalog(Model model) {
        model.addAttribute("counterparties", service.findAll());

        return "catalog/catalog";
    }

    @Operation(summary = "Возможные операции над каталогом",
            description = "Операции: создание, удаление и обновление контрагентов.")
    @ApiResponse(responseCode = "200", description = "Операции успешно открылись")
    @GetMapping("/operations")
    public String operations() {
        return "catalog/operations/operations";
    }

    @Operation(summary = "Поиск по каталогу",
            description = "Поиск по наименованию, по БИК и номеру счёта.")
    @ApiResponse(responseCode = "200", description = "Фильтры успешно открылись")
    @GetMapping("/filters")
    public String filters() {
        return "catalog/filters/filters";
    }
}
