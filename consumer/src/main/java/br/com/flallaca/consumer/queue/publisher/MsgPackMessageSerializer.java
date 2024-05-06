package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

public class MsgPackMessageSerializer implements MessageSerializer {
    @Override
    public byte[] serialize(ResponseSkeletonDTO responseDTO) {
        try {
            return new ObjectMapper(new MessagePackFactory()).writeValueAsBytes(responseDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
