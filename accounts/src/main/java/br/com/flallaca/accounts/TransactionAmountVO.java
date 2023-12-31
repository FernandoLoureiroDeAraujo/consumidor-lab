package br.com.flallaca.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAmountVO {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;

    public TransactionAmountVO createTransactionAmount(Faker faker) {

        this.setAmount(faker.number().digits(4));
        this.setCurrency(faker.currency().code());

        return this;
    }
}
