package ru.mordyasov.controller;

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

    @GetMapping()
    public ModelAndView catalog() {
        return new ModelAndView("catalog/catalog")
                .addObject("counterparties", service.findAll());
    }

    @GetMapping("/operations")
    public ModelAndView operations() {
        return new ModelAndView("catalog/operations/operations");
    }

    @GetMapping("/filters")
    public ModelAndView filters() {
        return new ModelAndView("catalog/filters/filters");
    }
}
