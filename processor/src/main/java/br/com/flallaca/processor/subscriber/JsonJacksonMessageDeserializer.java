package br.com.flallaca.processor.subscriber;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonJacksonMessageDeserializer implements MessageDeserializer<ResponseSkeletonDTO> {
    @Override
    public ResponseSkeletonDTO deserialize(byte[] data) {
        try {
            return new ObjectMapper().readValue(data, ResponseSkeletonDTO.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
