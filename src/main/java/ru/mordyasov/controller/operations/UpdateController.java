package ru.mordyasov.controller.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

import java.util.stream.Stream;

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

    @GetMapping
    public ModelAndView update() {
        return new ModelAndView("catalog/operations/update");
    }

    @GetMapping("/search_by_name")
    public ModelAndView updateByName() {
        return new ModelAndView("catalog/operations/update")
                .addObject("activeN", "active")
                .addObject("none", "d-none");
    }

    @PostMapping("/search_by_name")
    public ModelAndView updateByName(@RequestParam("name") String name) {
        return new ModelAndView("catalog/operations/update")
                .addObject("activeN", "active")
                .addObject("counterparty",  service.findByName(name).orElse(null));
    }

    @PostMapping("/search_by_name/{id}")
    public ModelAndView updateByName(@PathVariable("id") Long id) {
        return new ModelAndView("catalog/operations/update")
                .addObject("modalName", "#update")
                .addObject("counterparty",  service.find(id).get());
    }

    @GetMapping("/search_by_id")
    public ModelAndView updateById() {
        return new ModelAndView("catalog/operations/update")
                .addObject("activeID", "active")
                .addObject("none", "d-none");
    }

    @PostMapping("/search_by_id")
    public ModelAndView updateById(@RequestParam("id") String id) {
        ModelAndView model = new ModelAndView("catalog/operations/update");

        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addObject("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addObject("activeID", "active");

        return model;
    }

    @PostMapping("/search_by_id/{id}")
    public ModelAndView updateById(@PathVariable("id") Long id) {
        return new ModelAndView("catalog/operations/update")
                .addObject("modalName", "#update")
                .addObject("counterparty",  service.find(id).get());
    }

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
