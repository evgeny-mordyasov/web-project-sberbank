package ru.mordyasov.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ru.mordyasov.domain.Counterparty;
import ru.mordyasov.service.interfaces.CounterpartyService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-counterparty-before-each.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CounterpartyServiceTest {
    @Autowired
    private CounterpartyService service;

    private final long objectIdInDb = 1L;

    private final String emptyData = "";

    private final Counterparty objectFromDB = new Counterparty(objectIdInDb,
            "Сбербанк",
            "7707083893",
            "773601001",
            "30301810000006000001",
            "044525225");

    @Test
    public void findWhereIdIsReal() {
        assertThat(service.find(objectFromDB.getId())).isEqualTo(Optional.of(objectFromDB));
    }

    @Test
    public void findWhereIdIsFake() {
        long fakeId = 99L;
        assertThat(service.find(fakeId)).isEqualTo(Optional.empty());
    }

    @Test
    public void findAllWhereWillOneObjectBeFound() {
        assertThat(service.findAll()).isEqualTo(List.of(objectFromDB));
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.OVERRIDE)
    @Sql(value = "/sql/delete-from-counterparty.sql")
    public void findAllWhereNothingWillBeFound() {
        assertThat(service.findAll()).isEqualTo(List.of());
    }

    @Test
    public void findByNameWhereNameIsRealAndWillOneObjectBeFound() {
        assertThat(service.findByName(objectFromDB.getName())).isEqualTo(Optional.of(objectFromDB));
    }

    @Test
    public void findByNameWhereNameIsFakeAndNothingWillBeFound() {
        assertThat(service.findByName(emptyData)).isEqualTo(Optional.empty());
    }

    @Test
    public void findByAccountNumberAndBICWhereDataIsRealAndWillOneObjectBeFound() {
        assertThat(service.findByAccountNumberAndBIC(
                objectFromDB.getAccountNumber(), objectFromDB.getBIC()))
                .isEqualTo(List.of(objectFromDB));
    }

    @Test
    public void findByAccountNumberAndBICWhereDataIsFakeAndNothingWillBeFound() {
        assertThat(service.findByAccountNumberAndBIC(emptyData, emptyData)).isEqualTo(List.of());
    }

    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(value = "/sql/create-counterparty.sql")
    public void findByAccountNumberAndBICWhereDataIsRealAndTwoObjectWillBeFound() {
        long objectTempIdInDb = 2L;
        Counterparty testObjectTemp = new Counterparty(objectTempIdInDb,
                "Еще один Сбербанк",
                "7707083893",
                "773601001",
                "30301810000006000001",
                "044525225");

        assertThat(service.findByAccountNumberAndBIC(
                objectFromDB.getAccountNumber(), objectFromDB.getBIC()))
                .isEqualTo(List.of(objectFromDB, testObjectTemp));
    }

    // Зависимость от .find
    @Test
    @SqlMergeMode(SqlMergeMode.MergeMode.OVERRIDE)
    @Sql(value = "/sql/delete-from-counterparty.sql")
    public void add() {
        long defaultIdWhenAdding = 1L;
        Counterparty objectTemp = new Counterparty(emptyData, emptyData, emptyData, emptyData, emptyData);

        service.add(objectTemp);

        assertThat(service.find(defaultIdWhenAdding)).isEqualTo(Optional.of(objectTemp));
    }

    // Зависимость от .find
    @Test
    public void delete() {
        service.delete(objectFromDB);

        assertThat(service.find(objectFromDB.getId())).isEqualTo(Optional.empty());
    }

    // Зависимость от .find
    @Test
    public void update() {
        Optional<Counterparty> object = service.find(objectFromDB.getId());
        object.ifPresent(counterparty -> {
            counterparty.setName("Сбербанк измененный");
            service.update(counterparty);

            assertThat(service.find(counterparty.getId())).isEqualTo(Optional.of(counterparty));
        });
    }
}