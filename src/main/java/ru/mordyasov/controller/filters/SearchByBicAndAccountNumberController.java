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
@RequestMapping("/catalog/filters/search_by_BIC_and_aN")
public class SearchByBicAndAccountNumberController {
    private CounterpartyService service;

    @Autowired
    public SearchByBicAndAccountNumberController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping
    public String search(Model model) {
        model.addAttribute("activeBic", "active");
        model.addAttribute("none", "d-none");

        return "catalog/filters/filters";
    }

    @PostMapping
    public String search(@RequestParam("account_number") String aN, @RequestParam("bic") String bic, Model model) {
        model.addAttribute("activeBic", "active");
        model.addAttribute("counterparty",  service.findByAccountNumberAndBIC(aN, bic).orElse(null));

        return "catalog/filters/filters";
    }
}
