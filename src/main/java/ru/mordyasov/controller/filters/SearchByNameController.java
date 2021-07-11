package ru.mordyasov.controller.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mordyasov.service.interfaces.CounterpartyService;

@Controller
@RequestMapping("/catalog/filters/search_by_name")
public class SearchByNameController {
    private CounterpartyService service;

    @Autowired
    public SearchByNameController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping
    public String search(Model model) {
        model.addAttribute("activeN", "active");
        model.addAttribute("none", "d-none");

        return "catalog/filters/filters";
    }

    @PostMapping
    public String search(@RequestParam("name") String name, Model model) {
        model.addAttribute("activeN", "active");
        model.addAttribute("counterparty",  service.findByName(name).orElse(null));

        return "catalog/filters/filters";
    }
}
