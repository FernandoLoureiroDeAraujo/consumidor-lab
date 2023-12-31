package br.com.flallaca.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

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
public class ResponseSkeletonVO {

    @JsonProperty("meta")
    private String metadata;
    @JsonProperty("links")
    private String links;
    @JsonProperty("data")
    private List<TransactionVO> data;

    public ResponseSkeletonVO(final List<TransactionVO> data) {
        this.data = data;
    }
}
