package ru.mordyasov.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Класс - контрагент, являющийся неотъемлемой частью в работе данного приложения.
 * Класс, содержащий в себе всю необходимую информацию о контрагенте и являющимся POJO-классом.
 * Представляет также сущность в базе данных.
 */
@Entity
public class Counterparty {
    /**
     * Поле id - идентификатор, использующийся в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Поле name - наименование контрагента.
     */
    private String name;

    /**
     * Поле TIN - ИНН контрагента.
     */
    private String TIN;

    /**
     * Поле TRR - КПП контрагента.
     */
    private String TRR;

    /**
     * Поле accountNumber - номер счета контрагента.
     */
    private String accountNumber;

    /**
     * Поле BIC - БИК контрагента.
     */
    private String BIC;

    public Counterparty(Long id, String name, String TIN, String TRR, String accountNumber, String BIC) {
        this.id = id;
        this.name = name;
        this.TIN = TIN;
        this.TRR = TRR;
        this.accountNumber = accountNumber;
        this.BIC = BIC;
    }

    public Counterparty(String name, String TIN, String TRR, String accountNumber, String BIC) {
        this.name = name;
        this.TIN = TIN;
        this.TRR = TRR;
        this.accountNumber = accountNumber;
        this.BIC = BIC;
    }

    public Counterparty() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTIN() {
        return TIN;
    }

    public void setTIN(String TIN) {
        this.TIN = TIN;
    }

    public String getTRR() {
        return TRR;
    }

    public void setTRR(String TRR) {
        this.TRR = TRR;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counterparty that = (Counterparty) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(TIN, that.TIN) &&
                Objects.equals(TRR, that.TRR) &&
                Objects.equals(accountNumber, that.accountNumber) &&
                Objects.equals(BIC, that.BIC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, TIN, TRR, accountNumber, BIC);
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", TIN='" + TIN + '\'' +
                ", TRR='" + TRR + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", BIC='" + BIC + '\'' +
                '}';
    }
}
