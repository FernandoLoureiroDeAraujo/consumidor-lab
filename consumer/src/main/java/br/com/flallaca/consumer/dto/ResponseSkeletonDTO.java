package br.com.flallaca.consumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 *
 * @author Arquitetura de TI
 */
@JsonPropertyOrder({"data", "links", "meta"})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Message
public class ResponseSkeletonDTO {

    @JsonProperty("meta")
    private String metadata;
    @JsonProperty("links")
    private String links;
    @JsonProperty("data")
    private List<TransactionDTO> data;

    public ResponseSkeletonDTO(final List<TransactionDTO> data) {
        this.data = data;
    }
}
