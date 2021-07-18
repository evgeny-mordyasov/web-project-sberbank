package ru.mordyasov.controller.operations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

@RestController
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
    public ModelAndView update() {
        return new ModelAndView("catalog/operations/update");
    }

    @Operation(summary = "Выбор обновления контрагента: по наименованию",
            description = "Предоставляется поле: наименование, по которому можно найти и обновить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @GetMapping("/search_by_name")
    public ModelAndView updateByName() {
        return new ModelAndView("catalog/operations/update")
                .addObject("activeN", "active")
                .addObject("none", "d-none");
    }

    @Operation(summary = "Выбор обновления контрагента: по наименованию (отправлено значение)",
            description = "Отправленное значение проходит валидацию. Если все корректно, будет найден контрагент.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_name")
    public ModelAndView updateByName(@RequestParam("name") String name) {
        return new ModelAndView("catalog/operations/update")
                .addObject("activeN", "active")
                .addObject("counterparty",  service.findByName(name).orElse(null));
    }

    @Operation(summary = "Выбор обновления контрагента по наименованию (значение корректное)",
            description = "Введенное наименование прошло, будет выведена информация о найденном контрагенте.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_name/{id}")
    public ModelAndView updateByName(@PathVariable("id") Long id) {
        return new ModelAndView("catalog/operations/update")
                .addObject("modalName", "#update")
                .addObject("counterparty",  service.find(id).get());
    }

    @Operation(summary = "Выбор обновления контрагента: по идентификатору",
            description = "Предоставляется поле: идентификатор, по которому можно найти и обновить контрагента.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @GetMapping("/search_by_id")
    public ModelAndView updateById() {
        return new ModelAndView("catalog/operations/update")
                .addObject("activeID", "active")
                .addObject("none", "d-none");
    }

    @Operation(summary = "Выбор обновления контрагента: по идентификатору (отправлено значение)",
            description = "Отправленное значение проходит валидацию. Если все корректно, будет найден контрагент.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_id")
    public ModelAndView updateById(@RequestParam("id") String id) {
        ModelAndView model = new ModelAndView("catalog/operations/update");

        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addObject("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addObject("activeID", "active");

        return model;
    }

    @Operation(summary = "Выбор обновления контрагента по идентификатору (значение корректное)",
            description = "Введенный идентфикатор прошел, будет выведена информация о найденном контрагенте.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping("/search_by_id/{id}")
    public ModelAndView updateById(@PathVariable("id") Long id) {
        return new ModelAndView("catalog/operations/update")
                .addObject("modalName", "#update")
                .addObject("counterparty",  service.find(id).get());
    }

    @Operation(summary = "Изменения отправлены на проверку",
            description = "Если изменения корректны, они вступят силу. Иначе понадобится ввести данные в связи с указанными критериями.")
    @ApiResponse(responseCode = "200", description = "Выбор обновления успешно загрузился")
    @PostMapping
    public ModelAndView update(@Validated @ModelAttribute("counterparty") Counterparty counterparty, BindingResult result) {
        ModelAndView model = new ModelAndView("catalog/operations/update");

        if (result.hasErrors()) {
            model.addObject("activeValidator", "true")
                    .addObject("modalName", "#update");

            return model;
        }

        service.update(counterparty);

        return model;
    }
}
