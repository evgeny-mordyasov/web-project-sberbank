package ru.mordyasov.controller.operations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

@RestController
@RequestMapping("/catalog/operations/delete")
public class RemovalController {
    private CounterpartyService service;

    @Autowired
    public RemovalController(CounterpartyService service) {
        this.service = service;
    }

    @Operation(summary = "Выбор удаления",
            description = "Предоставляется возможность выбрать способ нахождения контрагента, который можно будет удалить.")
    @ApiResponse(responseCode = "200", description = "Выбор удаления успешно открылся")
    @GetMapping
    public ModelAndView delete() {
        return new ModelAndView("catalog/operations/delete")
                .addObject("none", "d-none");
    }

    @Operation(summary = "Выбор удаления: по наименованию",
            description = "Предоставляется поле: наименование, по которому можно найти и удалить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор удаления по наименованию успешно открылся")
    @PostMapping("/search_by_name")
    public ModelAndView deleteByName(@RequestParam("name") String name) {
        return new ModelAndView("catalog/operations/delete")
                .addObject("counterparty", service.findByName(name).orElse(null))
                .addObject("modalName", "#deleteByName");
    }

    @Operation(summary = "Найден контрагент по наименованию",
            description = "Найденного контрагента по наименованию можно удалить.")
    @ApiResponse(responseCode = "200", description = "Контрагент успешно найден")
    @PostMapping("/search_by_name/{id}")
    public ModelAndView deleteByName(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return new ModelAndView("redirect:/catalog/operations/delete");
    }

    @Operation(summary = "Выбор удаления: по идентификатору",
            description = "Предоставляется поле: идентификатор, по которому можно найти и удалить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор удаления по идентификатору успешно открылся")
    @PostMapping("/search_by_id")
    public ModelAndView deleteById(@RequestParam("id") String id) {
        ModelAndView model = new ModelAndView("catalog/operations/delete");

        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addObject("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addObject("modalName", "#deleteById");

        return model;
    }

    @Operation(summary = "Найден контрагент по идентификатору",
            description = "Найденного контрагента по наименованию можно удалить.")
    @ApiResponse(responseCode = "200", description = "Контрагент успешно найден")
    @PostMapping("/search_by_id/{id}")
    public ModelAndView deleteById(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return new ModelAndView("redirect:/catalog/operations/delete");
    }
}
