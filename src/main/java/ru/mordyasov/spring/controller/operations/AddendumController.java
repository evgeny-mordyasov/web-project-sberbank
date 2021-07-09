package ru.mordyasov.spring.controller.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

@Controller
@RequestMapping("/catalog/operations/add")
public class AddendumController {
    private CounterpartyService service;

    @Autowired
    public AddendumController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping
    public String add(Model model) {
        model.addAttribute("object", new Counterparty());

        return "catalog/operations/add";
    }

    @PostMapping
    public String add(@ModelAttribute("object") Counterparty counterparty) {
        service.add(counterparty);

        return "redirect:/catalog";
    }
}
