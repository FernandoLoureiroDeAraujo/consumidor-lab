package br.com.flallaca.processor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

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
public class ResponseObject {

    @JsonProperty("meta")
    private String metadata;
    @JsonProperty("links")
    private String links;
    @JsonProperty("data")
    private Object data;

    public ResponseObject(final Object data) {
        this.data = data;
    }
}
