package ru.mordyasov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.service.interfaces.CounterpartyService;

@RestController
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
    public ModelAndView catalog() {
        return new ModelAndView("catalog/catalog")
                .addObject("counterparties", service.findAll());
    }

    @Operation(summary = "Возможные операции над каталогом",
            description = "Операции: создание, удаление и обновление контрагентов.")
    @ApiResponse(responseCode = "200", description = "Операции успешно открылись")
    @GetMapping("/operations")
    public ModelAndView operations() {
        return new ModelAndView("catalog/operations/operations");
    }

    @Operation(summary = "Поиск по каталогу",
            description = "Поиск по наименованию, по БИК и номеру счёта.")
    @ApiResponse(responseCode = "200", description = "Фильтры успешно открылись")
    @GetMapping("/filters")
    public ModelAndView filters() {
        return new ModelAndView("catalog/filters/filters");
    }
}
