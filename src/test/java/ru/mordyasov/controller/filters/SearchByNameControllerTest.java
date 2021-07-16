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

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SearchByNameController.class)
public class SearchByNameControllerTest {
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
        mockMvc.perform(get("/catalog/filters/search_by_name"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("activeN", "none"));
    }

    @Test
    public void searchWhereObjectWasFound() throws Exception {
        when(service.findByName(validObject.getName())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/filters/search_by_name").param("name", validObject.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("counterparty", "activeN"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void searchWhereObjectWasNotFound() throws Exception {
        when(service.findByName(validObject.getName())).thenReturn(Optional.empty());

        mockMvc.perform(post("/catalog/filters/search_by_name").param("name", validObject.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("activeN"))
                .andExpect(model().attributeDoesNotExist("counterparty"));
    }
}
