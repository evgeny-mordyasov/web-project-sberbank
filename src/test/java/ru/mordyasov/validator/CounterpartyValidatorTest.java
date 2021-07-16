package ru.mordyasov.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CounterpartyValidatorTest {
    private final CounterpartyService service =
            mock(CounterpartyService.class);

    private Counterparty validObject;

    @MockBean
    private Errors errors;

    private final Validator validator =
            new CounterpartyValidator(service);

    private final int calledOnce = 1;

    @BeforeEach
    public void initObject() {
        validObject = new Counterparty(1L,
                "Сбербанк",
                "7707083893",
                "773601001",
                "30301810000006000001",
                "044525225");

        when(service.findAll()).thenReturn(List.of(validObject));
    }

    @Test
    public void testWhereAllDataIsValid() {
        validator.validate(validObject, errors);

        verify(errors, never()).rejectValue(any(), any(), any());
    }

    @Test
    public void testWhereNameIsEmpty() {
        validObject.setName("");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("name"), any(), any());
    }

    @Test
    public void testWhereNameAlreadyExists() {
        Counterparty objectInDB =
                new Counterparty(2L, "Сбербанк", "", "", "", "");

        when(service.findAll()).thenReturn(List.of(objectInDB));
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("name"), any(), any());
    }

    @Test
    public void testWhereNameContainsSpacesOrInvisibleSymbols() {
        validObject.setName("   ");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("name"), any(), any());
    }

    @Test
    public void testWhereNameContainsMoreThan20Symbols() {
        validObject.setName("AAAAA_AAAAA_AAAAA_AAAAA");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("name"), any(), any());
    }

    @Test
    public void testWhereTinIsEmpty() {
        validObject.setTIN("");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TIN"), any(), any());
    }

    @Test
    public void testWhereTinContainsSpacesOrInvisibleSymbols() {
        validObject.setTIN("   ");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TIN"), any(), any());
    }

    @Test
    public void testWhereTinIsNotEqualTo12Or10Digits() {
        validObject.setTIN("123");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TIN"), any(), any());
    }

    @Test
    public void testWhereTinContainsOtherSymbols() {
        validObject.setTIN("abcd");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TIN"), any(), any());
    }

    @Test
    public void testWhereTinIsInvalid() {
        validObject.setTIN("7707083892");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TIN"), any(), any());
    }

    @Test
    public void testWhereTrrIsEmpty() {
        validObject.setTRR("");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TRR"), any(), any());
    }

    @Test
    public void testWhereTrrContainsSpacesOrInvisibleSymbols() {
        validObject.setTRR("   ");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TRR"), any(), any());
    }

    @Test
    public void testWhenTrrIsNotEqualTo9Symbols() {
        validObject.setTRR("123");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TRR"), any(), any());
    }

    @Test
    public void testWhenTrrDoesNotConsistOfNumbers() {
        validObject.setTRR("123b12332");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TRR"), any(), any());
    }

    @Test
    public void testWhenTrrContainsNonLatinLettersOn5thAnd6thPositions() {
        validObject.setTRR("1234авб12");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("TRR"), any(), any());
    }

    @Test
    public void testWhereAccountNumberIsEmpty() {
        validObject.setAccountNumber("");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("accountNumber"), any(), any());
    }

    @Test
    public void testWhereAccountNumberContainsSpacesOrInvisibleSymbols() {
        validObject.setAccountNumber("   ");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("accountNumber"), any(), any());
    }

    @Test
    public void testWhenAccountNumberIsNotEqualTo20Digits() {
        validObject.setAccountNumber("123");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("accountNumber"), any(), any());
    }

    @Test
    public void testWhereAccountNumberContainsOtherSymbols() {
        validObject.setAccountNumber("3030181000000600000a");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("accountNumber"), any(), any());
    }

    @Test
    public void testWhereAccountNumberNotCheckedBecauseBicIsInvalid() {
        validObject.setBIC("");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("accountNumber"), any(), any());
    }

    @Test
    public void testWhereAccountNumberIsInvalid() {
        validObject.setAccountNumber("30301810000006000002");
        validator.validate(validObject, errors);

        verify(errors, times(calledOnce)).rejectValue(eq("accountNumber"), any(), any());
    }

    @Test
    public void testWhereBicIsEmpty() {
        validObject.setBIC("");
        validator.validate(validObject, errors);

       verify(errors, times(calledOnce)).rejectValue(eq("BIC"), any(), any());
    }

    @Test
    public void testWhereBicContainsSpacesOrInvisibleSymbols() {
        validObject.setBIC("   ");
        validator.validate(validObject, errors);

       verify(errors, times(calledOnce)).rejectValue(eq("BIC"), any(), any());
    }

    @Test
    public void testWhenBicIsNotEqualTo9Digits() {
        validObject.setBIC("123");
        validator.validate(validObject, errors);

       verify(errors, times(calledOnce)).rejectValue(eq("BIC"), any(), any());
    }

    @Test
    public void testWhereBicContainsOtherSymbols() {
        validObject.setBIC("123asd431");
        validator.validate(validObject, errors);

       verify(errors, times(calledOnce)).rejectValue(eq("BIC"), any(), any());
    }
}