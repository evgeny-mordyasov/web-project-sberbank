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
import ru.mordyasov.utils.MyStringUtils;

import java.util.Map;

/**
 * Класс UpdateController, позволяющий совершить обновление данных контрагента из справочника, предварительно отыскав его
 * по выбранному фильтру: по наименованию или идентификатору.
 */
@Controller
@RequestMapping("/catalog/operations/update")
public class UpdateController {
    private CounterpartyService service;
    private Validator validator;

    @Autowired
    public UpdateController(CounterpartyService service, @Qualifier("counterpartyValidator") Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @Operation(summary = "Выбор обновления контрагента",
            description = "Предоставляется возможность, каким способом будет найден контрагент для обновления.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @GetMapping
    public String update() {
        return "catalog/operations/update";
    }

    @Operation(summary = "Выбор обновления контрагента: по наименованию",
            description = "Предоставляется поле: наименование, по которому можно найти и обновить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @GetMapping("/search_by_name")
    public String updateByName(Model model) {
        model.addAllAttributes(Map.of(
                "activeN", "active",
                "none", "d-none"
        ));

        return "catalog/operations/update";
    }

    @Operation(summary = "Выбор обновления контрагента: по наименованию (отправлено значение)",
            description = "Отправленное значение проходит валидацию. Если все корректно, будет найден контрагент.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_name")
    public String updateByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("activeN", "active");
        model.addAttribute("counterparty",  service.findByName(name).orElse(null));

        return "catalog/operations/update";

    }

    @Operation(summary = "Выбор обновления контрагента по наименованию (значение корректное)",
            description = "Введенное наименование прошло, будет выведена информация о найденном контрагенте.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_name/{id}")
    public String updateByName(@PathVariable("id") Long id, Model model) {
        model.addAllAttributes(Map.of(
                "modalName", "#update",
                "counterparty",  service.find(id).get()
        ));

        return "catalog/operations/update";

    }

    @Operation(summary = "Выбор обновления контрагента: по идентификатору",
            description = "Предоставляется поле: идентификатор, по которому можно найти и обновить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @GetMapping("/search_by_id")
    public String updateById(Model model) {
        model.addAllAttributes(Map.of(
                "activeID", "active",
                "none", "d-none"
        ));

        return "catalog/operations/update";
    }

    @Operation(summary = "Выбор обновления контрагента: по идентификатору (отправлено значение)",
            description = "Отправленное значение проходит валидацию. Если все корректно, будет найден контрагент.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_id")
    public String updateById(@RequestParam("id") String id, Model model) {
        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addAttribute("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addAttribute("activeID", "active");

        return "catalog/operations/update";
    }

    @Operation(summary = "Выбор обновления контрагента по идентификатору (значение корректное)",
            description = "Введенный идентфикатор прошел, будет выведена информация о найденном контрагенте.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_id/{id}")
    public String updateById(@PathVariable("id") Long id, Model model) {
        model.addAllAttributes(Map.of(
                "modalName", "#update",
                "counterparty",  service.find(id).get()
        ));

        return "catalog/operations/update";
    }

    @Operation(summary = "Изменения отправлены на проверку",
            description = "Если изменения корректны, они вступят силу. Иначе понадобится ввести данные в связи с указанными критериями.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping
    public String update(@Validated @ModelAttribute("counterparty") Counterparty counterparty, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAllAttributes(Map.of(
                    "activeValidator", "true",
                    "modalName", "#update"
            ));

            return "catalog/operations/update";
        }

        service.update(counterparty);

        return "catalog/operations/update";
    }
}
