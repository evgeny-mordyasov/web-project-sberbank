package ru.mordyasov.controller.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.List;

@RestController
@RequestMapping("/catalog/filters/search_by_BIC_and_aN")
public class SearchByBicAndAccountNumberController {
    private CounterpartyService service;

    @Autowired
    public SearchByBicAndAccountNumberController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping
    public ModelAndView search() {
        return new ModelAndView("catalog/filters/filters")
                .addObject("activeBic", "active")
                .addObject("none", "d-none");
    }

    @PostMapping
    public ModelAndView search(@RequestParam("account_number") String aN, @RequestParam("bic") String bic) {
        List<Counterparty> list = service.findByAccountNumberAndBIC(aN, bic);

        return new ModelAndView("catalog/filters/filters")
                .addObject("activeBic", "active")
                .addObject("counterparties",  list.isEmpty() ? null : list);
    }
}
