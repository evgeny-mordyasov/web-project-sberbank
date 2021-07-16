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

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AddendumController.class)
@ComponentScan(basePackageClasses = {CounterpartyValidator.class})
public class AddendumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CounterpartyService service;

    private Counterparty validObject;

    private MultiValueMap<String, String> params;

    private final Supplier<MvcResult> lambdaForCheckingCase = () -> {
        try {
            return mockMvc.perform(post("/catalog/operations/add").params(params))
                    .andExpect(status().isOk())
                    .andExpect(view().name("catalog/operations/add"))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andExpect(model().size(2))
                    .andExpect(model().attributeExists("object", "activeValidator"))
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };

    @BeforeEach
    public void initObjects() {
        validObject = new Counterparty("Сбербанк",
                "7707083893",
                "773601001",
                "30301810000006000001",
                "044525225");

        params = new LinkedMultiValueMap<>();

        params.add("name", validObject.getName());
        params.add("TIN", validObject.getTIN());
        params.add("TRR", validObject.getTRR());
        params.add("accountNumber", validObject.getAccountNumber());
        params.add("BIC", validObject.getBIC());
    }

    @Test
    public void addGetMethod() throws Exception {
        Counterparty emptyObject = new Counterparty();

        mockMvc.perform(get("/catalog/operations/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/operations/add"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().size(1))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("object", is(emptyObject)));
    }

    @Test
    public void addPostMethodWhereAllDataIsValid() throws Exception {
        List<Counterparty> database = new ArrayList<>();

        doAnswer(i -> {
            database.add(validObject);
            return null;
        }).when(service).add(validObject);

        mockMvc.perform(post("/catalog/operations/add").params(params))
                .andExpect(view().name("redirect:/catalog"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalog"));

        assertThat(database).isEqualTo(List.of(validObject));
    }

    @Test
    public void addPostMethodWhereNameIsEmpty() {
        params.set("name", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereNameAlreadyExists() {
        Counterparty objectInDB = new Counterparty(2L, "Сбербанк", "", "", "", "");
        when(service.findByName(validObject.getName())).thenReturn(Optional.of(objectInDB));

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereNameContainsSpacesOrInvisibleSymbols() {
        params.set("name", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereNameContainsMoreThan20Symbols() {
        params.set("name", "AAAAA_AAAAA_AAAAA_AAAAA");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTinIsEmpty() {
        params.set("TIN", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTinContainsSpacesOrInvisibleSymbols() {
        params.set("TIN", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTinIsNotEqualTo12Or10Digits() {
        params.set("TIN", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTinContainsOtherSymbols() {
        params.set("TIN", "abcd");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTinIsInvalid() {
        params.set("TIN", "7707083892");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTrrIsEmpty() {
        params.set("TRR", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereTrrContainsSpacesOrInvisibleSymbols() {
        params.set("TRR", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhenTrrIsNotEqualTo9Symbols() {
        params.set("TRR", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereAccountNumberIsEmpty() {
        params.set("accountNumber", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereAccountNumberContainsSpacesOrInvisibleSymbols() {
        params.set("accountNumber", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhenAccountNumberIsNotEqualTo20Digits() {
        params.set("accountNumber", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereAccountNumberContainsOtherSymbols() {
        params.set("accountNumber", "3030181000000600000a");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereAccountNumberNotCheckedBecauseBicIsInvalid() {
        params.set("BIC", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereAccountNumberIsInvalid() {
        params.set("accountNumber", "30301810000006000002");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereBicIsEmpty() {
        params.set("BIC", "");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereBicContainsSpacesOrInvisibleSymbols() {
        params.set("BIC", "   ");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhenBicIsNotEqualTo9Digits() {
        params.set("BIC", "123");

        lambdaForCheckingCase.get();
    }

    @Test
    public void addPostMethodWhereBicContainsOtherSymbols() {
        params.set("BIC", "123asd431");

        lambdaForCheckingCase.get();
    }
}
