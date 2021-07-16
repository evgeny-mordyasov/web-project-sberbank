package ru.mordyasov.controller.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SearchByBicAndAccountNumberController.class)
public class SearchByBicAndAccountNumberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CounterpartyService service;

    private final Counterparty validObject = new Counterparty(1L,
            "Сбербанк",
            "7707083893",
            "773601001",
            "30301810000006000001",
            "044525225");

    @Test
    public void search() throws Exception {
        mockMvc.perform(get("/catalog/filters/search_by_BIC_and_aN"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("activeBic", "none"));
    }

    @Test
    public void searchWhereOneObjectWasFound() throws Exception {
        List<Counterparty> database = new ArrayList<>(List.of(validObject));
        when(service.findByAccountNumberAndBIC(validObject.getAccountNumber(), validObject.getBIC())).thenReturn(database);

        mockMvc.perform(post("/catalog/filters/search_by_BIC_and_aN")
                .param("account_number", validObject.getAccountNumber())
                .param("bic", validObject.getBIC()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("counterparties", "activeBic"))
                .andExpect(model().attribute("counterparties", is(database)));
    }

    @Test
    public void searchWhereMoreThanOneObjectWasFound() throws Exception {
        Counterparty objectInDB = new Counterparty("", "", "", validObject.getAccountNumber(), validObject.getBIC());
        List<Counterparty> database = new ArrayList<>(List.of(validObject, objectInDB));
        when(service.findByAccountNumberAndBIC(validObject.getAccountNumber(), validObject.getBIC())).thenReturn(database);

        mockMvc.perform(post("/catalog/filters/search_by_BIC_and_aN")
                .param("account_number", validObject.getAccountNumber())
                .param("bic", validObject.getBIC()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("counterparties", "activeBic"))
                .andExpect(model().attribute("counterparties", is(database)));
    }

    @Test
    public void searchWhereNoObjectsWereFound() throws Exception {
        when(service.findByAccountNumberAndBIC(validObject.getAccountNumber(), validObject.getBIC())).thenReturn(List.of());

        mockMvc.perform(post("/catalog/filters/search_by_BIC_and_aN")
                .param("account_number", validObject.getAccountNumber())
                .param("bic", validObject.getBIC()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("activeBic"))
                .andExpect(model().attributeDoesNotExist("counterparties"));
    }
}
