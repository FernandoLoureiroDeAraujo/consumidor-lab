package br.com.flallaca.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAmount {

    @JsonProperty("amount")
    private Object amount;
    @JsonProperty("currency")
    private Object currency;

}
