package ru.mordyasov.controller.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.service.interfaces.CounterpartyService;

@RestController
@RequestMapping("/catalog/filters/search_by_name")
public class SearchByNameController {
    private CounterpartyService service;

    @Autowired
    public SearchByNameController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping
    public ModelAndView search() {
        return new ModelAndView("catalog/filters/filters")
                .addObject("activeN", "active")
                .addObject("none", "d-none");
    }

    @PostMapping
    public ModelAndView search(@RequestParam("name") String name) {
        return new ModelAndView("catalog/filters/filters")
                .addObject("activeN", "active")
                .addObject("counterparty",  service.findByName(name).orElse(null));
    }
}
