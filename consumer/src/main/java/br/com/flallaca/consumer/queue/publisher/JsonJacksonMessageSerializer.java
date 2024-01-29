package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonJacksonMessageSerializer implements MessageSerializer {
    @Override
    public byte[] serialize(ResponseSkeletonDTO responseDTO) {
        try {
            return new ObjectMapper().writeValueAsBytes(responseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
