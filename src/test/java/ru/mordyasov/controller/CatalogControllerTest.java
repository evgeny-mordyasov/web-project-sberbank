package ru.mordyasov.controller;

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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CatalogController.class)
public class CatalogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CounterpartyService service;

    @Test
    public void catalog() throws Exception {
        List<Counterparty> database = List.of(
                new Counterparty(1L, "Сбербанк", "7707083893", "773601001", "30301810000006000001", "044525225"),
                new Counterparty(2L, "ВТБ", "7702070139", "770943002", "30232810700002000004", "044525411"),
                new Counterparty(3L, "Газпромбанк", "7744001497", "771301001", "30101810200000000823", "044525823")
        );

        when(service.findAll()).thenReturn(database);

        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/catalog"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("counterparties"))
                .andExpect(model().attribute("counterparties", is(database)));
    }

    @Test
    public void operations() throws Exception {
        mockMvc.perform(get("/catalog/operations"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/operations"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    public void filters() throws Exception {
        mockMvc.perform(get("/catalog/filters"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/filters/filters"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
