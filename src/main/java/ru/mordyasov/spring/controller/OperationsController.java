package ru.mordyasov.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.utils.MyStringUtils;

@Controller
@RequestMapping("/catalog/operations")
public class OperationsController {
    private CounterpartyService service;

    @Autowired
    public OperationsController(CounterpartyService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("object", new Counterparty());

        return "catalog/operations/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("object") Counterparty counterparty) {
        service.add(counterparty);

        return "redirect:/catalog";
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        model.addAttribute("none", "d-none");

        return "catalog/operations/delete";
    }

    @PostMapping("/delete/search_by_name")
    public String deleteByName(@RequestParam("name") String name, Model model) {
        model.addAttribute("counterparty",  service.findByName(name).orElse(null));
        model.addAttribute("modalName", "#deleteByName");

        return "catalog/operations/delete";
    }

    @PostMapping("/delete/search_by_name/{id}")
    public String deleteByName(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return "redirect:/catalog/operations/delete";
    }

    @PostMapping("/delete/search_by_id")
    public String deleteById(@RequestParam("id") String id, Model model) {
        if (!id.isEmpty() && MyStringUtils.isNumeric(id)) {
            model.addAttribute("counterparty", service.find(Long.parseLong(id)).orElse(null));
        }

        model.addAttribute("modalName", "#deleteById");

        return "catalog/operations/delete";
    }

    @PostMapping("/delete/search_by_id/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        service.delete(service.find(id).get());

        return "redirect:/catalog/operations/delete";
    }
}