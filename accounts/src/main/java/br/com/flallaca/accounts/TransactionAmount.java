package br.com.flallaca.accounts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAmount {

    @JsonProperty("amount")
    private Object amount;
    @JsonProperty("currency")
    private Object currency;

    public TransactionAmount createTransactionAmount(Faker faker) {
        this.setAmount(faker.number().digits(4));
        this.setCurrency(faker.currency().code());
        return this;
    }
}
