package ru.mordyasov.controller.operations;

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
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RemovalController.class)
public class RemovalControllerTest {
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
    public void delete() throws Exception {
        mockMvc.perform(get("/catalog/operations/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/delete"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("none"));
    }

    @Test
    public void deleteByNameWhereNameIsExists() throws Exception {
        when(service.findByName(validObject.getName())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/operations/delete/search_by_name").param("name", validObject.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/delete"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("counterparty", "modalName"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void deleteByNameWhereNameDoesIsExists() throws Exception {
        String emptyName = "";
        when(service.findByName(emptyName)).thenReturn(Optional.empty());

        mockMvc.perform(post("/catalog/operations/delete/search_by_name").param("name", emptyName))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/delete"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("modalName"))
                .andExpect(model().attributeDoesNotExist("counterparty"));
    }

    @Test
    public void deleteByName() throws Exception {
        List<Counterparty> database = new ArrayList<>(List.of(validObject));
        when(service.find(validObject.getId())).thenReturn(Optional.of(validObject));

        doAnswer(i -> {
            database.remove(validObject);
            return null;
        }).when(service).delete(validObject);

        mockMvc.perform(post("/catalog/operations/delete/search_by_name/" + validObject.getId())
                .param("id", String.valueOf(validObject.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/operations/delete"))
                .andExpect(view().name("redirect:/catalog/operations/delete"));

        assertThat(database).isEqualTo(List.of());
    }

    @Test
    public void deleteByIdWhereIdIsExists() throws Exception {
        when(service.find(validObject.getId())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/operations/delete/search_by_id").param("id", String.valueOf(validObject.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/delete"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("counterparty", "modalName"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void deleteByIdWhereIdDoesIsExists() throws Exception {
        long fakeId = 99L;
        when(service.find(fakeId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/catalog/operations/delete/search_by_id").param("id", String.valueOf(fakeId)))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/delete"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("modalName"))
                .andExpect(model().attributeDoesNotExist("counterparty"));
    }

    @Test
    public void deleteById() throws Exception {
        List<Counterparty> database = new ArrayList<>(List.of(validObject));
        when(service.find(validObject.getId())).thenReturn(Optional.of(validObject));

        doAnswer(i -> {
            database.remove(validObject);
            return null;
        }).when(service).delete(validObject);

        mockMvc.perform(post("/catalog/operations/delete/search_by_id/" + validObject.getId())
                .param("id", String.valueOf(validObject.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog/operations/delete"))
                .andExpect(view().name("redirect:/catalog/operations/delete"));

        assertThat(database).isEqualTo(List.of());
    }
}
