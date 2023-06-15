package br.com.flallaca.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 *
 * @author Arquitetura de TI
 */
@JsonPropertyOrder({"data", "links", "meta"})
public class ResponseObject {

    @JsonProperty("meta")
    protected String metadata;
    @JsonProperty("links")
    protected String links;
    @JsonProperty("data")
    protected Object data;

    public ResponseObject(final Object data) {
        this.data = data;
    }
}
