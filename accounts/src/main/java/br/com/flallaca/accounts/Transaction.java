package br.com.flallaca.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Size(min = 1, max = 100)
    private String transactionId;

    @Size(max = 4)
    private String partieBranchCode;

    @Size(max = 3)
    private String partieCompeCode;

    @Size(max = 14)
    private String partieCnpjCpf;

    @NotNull
    @NotEmpty
    @Size(max = 10)
    private String transactionDate;

    @Size(max = 1)
    private String partieCheckDigit;

    @NotNull
    private TransactionAmount transactionAmount;

    @NotNull
    @NotEmpty
    @Size(max = 60)
    private String transactionName;

    @Size(max = 20)
    private String partieNumber;

    @NotNull
    @NotEmpty
    private String creditDebitType;

    @NotNull
    @NotEmpty
    private String type;
    private String partiePersonType;

    @NotNull
    @NotEmpty
    private String completedAuthorisedPaymentType;

    public Transaction createTransaction(Faker faker, TransactionAmount transactionAmount) {
        this.setTransactionId(faker.idNumber().valid());
        this.setPartieBranchCode(faker.random().hex(4));
        this.setPartieCompeCode(faker.random().hex(3));
        this.setPartieCnpjCpf(faker.random().hex(14));
        this.setTransactionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        this.setPartieCheckDigit(faker.random().hex(1));
        this.setTransactionName(faker.name().lastName());
        this.setPartieNumber(faker.random().hex(20));
        this.setCreditDebitType(faker.random().hex());
        this.setType(faker.random().hex());
        this.setPartiePersonType(faker.random().hex());
        this.setCompletedAuthorisedPaymentType(faker.random().hex());
        this.setTransactionAmount(transactionAmount);
        return this;
    }
}
