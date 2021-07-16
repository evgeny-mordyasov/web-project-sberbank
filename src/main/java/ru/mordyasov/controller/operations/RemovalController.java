package ru.mordyasov.controller.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping
    public ModelAndView delete() {
        return new ModelAndView("catalog/operations/delete")
                .addObject("none", "d-none");
    }

    @PostMapping("/search_by_name")
    public ModelAndView deleteByName(@RequestParam("name") String name) {
        return new ModelAndView("catalog/operations/delete")
                .addObject("counterparty", service.findByName(name).orElse(null))
                .addObject("modalName", "#deleteByName");
    }

    @PostMapping("/search_by_name/{id}")
    public ModelAndView deleteByName(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return new ModelAndView("redirect:/catalog/operations/delete");
    }

    @PostMapping("/search_by_id")
    public ModelAndView deleteById(@RequestParam("id") String id) {
        ModelAndView model = new ModelAndView("catalog/operations/delete");

        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addObject("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addObject("modalName", "#deleteById");

        return model;
    }

    @PostMapping("/search_by_id/{id}")
    public ModelAndView deleteById(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return new ModelAndView("redirect:/catalog/operations/delete");
    }
}
