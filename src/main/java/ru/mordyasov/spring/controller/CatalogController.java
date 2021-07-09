package ru.mordyasov.spring.controller;

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

    @GetMapping
    public String catalogs(Model model) {
        model.addAttribute("counterparties", service.findAll());

        return "catalog/catalog";
    }

    @GetMapping("/operations")
    public String operations() {
        return "catalog/operations/operations";
    }

    @GetMapping("/filters")
    public String filters(Model model) {
        model.addAttribute("filter", "true");

        return "catalog/filters/filters";
    }
}
