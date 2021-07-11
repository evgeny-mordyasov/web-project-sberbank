package ru.mordyasov.controller.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

@Controller
@RequestMapping("/catalog/operations/update")
public class UpdateController {
    private CounterpartyService service;

    @Autowired
    public UpdateController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping
    public String update() {
        return "catalog/operations/update";
    }

    @GetMapping("/search_by_name")
    public String updateByName(Model model) {
        model.addAttribute("activeN", "active");
        model.addAttribute("none", "d-none");

        return "catalog/operations/update";
    }

    @PostMapping("/search_by_name")
    public String updateByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("activeN", "active");
        model.addAttribute("counterparty",  service.findByName(name).orElse(null));

        return "catalog/operations/update";
    }

    @PostMapping("/search_by_name/{id}")
    public String updateByName(@PathVariable("id") Long id, Model model) {
        model.addAttribute("modalName", "#update");
        model.addAttribute("counterparty",  service.find(id).get());

        return "catalog/operations/update";
    }

    @GetMapping("/search_by_id")
    public String updateById(Model model) {
        model.addAttribute("activeID", "active");
        model.addAttribute("none", "d-none");

        return "catalog/operations/update";
    }

    @PostMapping("/search_by_id")
    public String updateById(@RequestParam("id") String id, Model model) {
        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addAttribute("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addAttribute("activeID", "active");

        return "catalog/operations/update";
    }

    @PostMapping("/search_by_id/{id}")
    public String updateById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("modalName", "#update");
        model.addAttribute("counterparty",  service.find(id).get());

        return "catalog/operations/update";
    }

    @PostMapping
    public String update(@ModelAttribute("counterparty") Counterparty counterparty) {
        service.update(counterparty);

        return "catalog/operations/update";
    }
}
