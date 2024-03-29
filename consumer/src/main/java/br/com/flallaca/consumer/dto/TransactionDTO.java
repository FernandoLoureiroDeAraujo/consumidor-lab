package br.com.flallaca.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.msgpack.annotation.Message;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Message
public class TransactionDTO {

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
    private TransactionAmountDTO transactionAmount;

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

}
