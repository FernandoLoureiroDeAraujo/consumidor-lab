package br.com.flallaca.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.msgpack.annotation.Message;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Message
public class TransactionAmountDTO {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("currency")
    private String currency;

}
