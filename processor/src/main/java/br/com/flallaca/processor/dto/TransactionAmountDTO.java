package br.com.flallaca.processor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionAmountDTO {

    @JsonProperty("amount")
    private Object amount;
    @JsonProperty("currency")
    private Object currency;

}
