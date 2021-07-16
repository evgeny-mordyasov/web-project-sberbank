package ru.mordyasov.controller.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;
import ru.mordyasov.validator.CounterpartyValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UpdateController.class)
@ComponentScan(basePackageClasses = {CounterpartyValidator.class})
public class UpdateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CounterpartyService service;

    private  Counterparty validObject;

    private  MultiValueMap<String, String> params;

    private final Supplier<MvcResult> lambdaForCheckingCase = () -> {
        try {
            return mockMvc.perform(post("/catalog/operations/update").params(params))
                    .andExpect(status().isOk())
                    .andExpect(view().name("catalog/operations/update"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andExpect(model().size(3))
                    .andExpect(model().attributeExists("counterparty", "activeValidator", "modalName"))
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };

    @BeforeEach
    public void initObjects() {
        validObject = new Counterparty(1L,
                "Сбербанк",
                "7707083893",
                "773601001",
                "30301810000006000001",
                "044525225");

        params = new LinkedMultiValueMap<>();

        params.add("id", String.valueOf(validObject.getId()));
        params.add("name", validObject.getName());
        params.add("TIN", validObject.getTIN());
        params.add("TRR", validObject.getTRR());
        params.add("accountNumber", validObject.getAccountNumber());
        params.add("BIC", validObject.getBIC());
    }

    @Test
    public void updateGetMethod() throws Exception {
        mockMvc.perform(get("/catalog/operations/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    public void updateByName() throws Exception {
        mockMvc.perform(get("/catalog/operations/update/search_by_name"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("activeN", "none"));
    }

    @Test
    public void updateByNameWhereNameIsExists() throws Exception {
        when(service.findByName(validObject.getName())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/operations/update/search_by_name").param("name", validObject.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("activeN", "counterparty"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void updateByNameWhereNameDoesNotExists() throws Exception {
        String emptyName = "";
        when(service.findByName(emptyName)).thenReturn(Optional.empty());

        mockMvc.perform(post("/catalog/operations/update/search_by_name").param("name", emptyName))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("activeN"))
                .andExpect(model().attributeDoesNotExist("counterparty"));
    }

    @Test
    public void updateByNameWhereObjectIsFound() throws Exception {
        when(service.find(validObject.getId())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/operations/update/search_by_name/" + validObject.getId())
                .param("id", String.valueOf(validObject.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("modalName", "counterparty"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void updateById() throws Exception {
        mockMvc.perform(get("/catalog/operations/update/search_by_id"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("activeID", "none"));
    }

    @Test
    public void updateByIdWhereIdIsExists() throws Exception {
        when(service.find(validObject.getId())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/operations/update/search_by_id").param("id", String.valueOf(validObject.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("activeID", "counterparty"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void updateByIdWhereIdDoesNotExists() throws Exception {
        long fakeId = 99L;
        when(service.find(fakeId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/catalog/operations/update/search_by_id").param("id", String.valueOf(fakeId)))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("activeID"))
                .andExpect(model().attributeDoesNotExist("counterparty"));
    }

    @Test
    public void updateByIdWhereObjectIsFound() throws Exception {
        when(service.find(validObject.getId())).thenReturn(Optional.of(validObject));

        mockMvc.perform(post("/catalog/operations/update/search_by_id/" + validObject.getId())
                .param("id", String.valueOf(validObject.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("modalName", "counterparty"))
                .andExpect(model().attribute("counterparty", is(validObject)));
    }

    @Test
    public void updatePostMethodWhereAllDataIsValid() throws Exception {
        List<Counterparty> database = new ArrayList<>(List.of(validObject));

        doAnswer(i -> {
            database.set(database.indexOf(validObject), validObject);
            return null;
        }).when(service).update(validObject);

        mockMvc.perform(post("/catalog/operations/update").params(params))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/update"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

        assertThat(database).isEqualTo(List.of(validObject));
    }

    @Test
    public void updatePostMethodWhereNameIsEmpty() {
        params.set("name", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereNameAlreadyExists() {
        Counterparty objectInDB = new Counterparty(2L,
                validObject.getName(),
                validObject.getTIN(),
                validObject.getTRR(),
                validObject.getAccountNumber(),
                validObject.getBIC());

        when(service.findByName(validObject.getName())).thenReturn(Optional.of(objectInDB));

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereNameContainsSpacesOrInvisibleSymbols() {
        params.set("name", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereNameContainsMoreThan20Symbols() {
        params.set("name", "AAAAA_AAAAA_AAAAA_AAAAA");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTinIsEmpty() {
        params.set("TIN", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTinContainsSpacesOrInvisibleSymbols() {
        params.set("TIN", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTinIsNotEqualTo12Or10Digits() {
        params.set("TIN", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTinContainsOtherSymbols() {
        params.set("TIN", "abcd");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTinIsInvalid() {
        params.set("TIN", "7707083892");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTrrIsEmpty() {
        params.set("TRR", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereTrrContainsSpacesOrInvisibleSymbols() {
        params.set("TRR", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhenTrrIsNotEqualTo9Symbols() {
        params.set("TRR", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereAccountNumberIsEmpty() {
        params.set("accountNumber", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereAccountNumberContainsSpacesOrInvisibleSymbols() {
        params.set("accountNumber", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhenAccountNumberIsNotEqualTo20Digits() {
        params.set("accountNumber", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereAccountNumberContainsOtherSymbols() {
        params.set("accountNumber", "3030181000000600000a");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereAccountNumberNotCheckedBecauseBicIsInvalid() {
        params.set("BIC", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereAccountNumberIsInvalid() {
        params.set("accountNumber", "30301810000006000002");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereBicIsEmpty() {
        params.set("BIC", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereBicContainsSpacesOrInvisibleSymbols() {
        params.set("BIC", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhenBicIsNotEqualTo9Digits() {
        params.set("BIC", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void updatePostMethodWhereBicContainsOtherSymbols() {
        params.set("BIC", "123asd431");

        lambdaForCheckingCase.get();
    }
}