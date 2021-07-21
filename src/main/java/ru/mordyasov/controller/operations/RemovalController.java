package ru.mordyasov.controller.operations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

/**
 * Класс RemovalController, позволяющий сначала отыскать контрагента из справочника по определенному фильтру, а после
 * совершить удаление при необходимости.
 */
@Controller
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
    public String delete(Model model) {
        model.addAttribute("none", "d-none");

        return "catalog/operations/delete";
    }

    @Operation(summary = "Выбор удаления: по наименованию",
            description = "Предоставляется поле: наименование, по которому можно найти и удалить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор удаления по наименованию успешно открылся")
    @PostMapping("/search_by_name")
    public String deleteByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("counterparty", service.findByName(name).orElse(null));
        model.addAttribute("modalName", "#deleteByName");

        return "catalog/operations/delete";
    }

    @Operation(summary = "Найден контрагент по наименованию",
            description = "Найденного контрагента по наименованию можно удалить.")
    @ApiResponse(responseCode = "200", description = "Контрагент успешно найден")
    @PostMapping("/search_by_name/{id}")
    public String deleteByName(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return "redirect:/catalog/operations/delete";
    }

    @Operation(summary = "Выбор удаления: по идентификатору",
            description = "Предоставляется поле: идентификатор, по которому можно найти и удалить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор удаления по идентификатору успешно открылся")
    @PostMapping("/search_by_id")
    public String deleteById(@RequestParam("id") String id, Model model) {
        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addAttribute("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addAttribute("modalName", "#deleteById");

        return "catalog/operations/delete";
    }

    @Operation(summary = "Найден контрагент по идентификатору",
            description = "Найденного контрагента по наименованию можно удалить.")
    @ApiResponse(responseCode = "200", description = "Контрагент успешно найден")
    @PostMapping("/search_by_id/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return "redirect:/catalog/operations/delete";
    }
}
