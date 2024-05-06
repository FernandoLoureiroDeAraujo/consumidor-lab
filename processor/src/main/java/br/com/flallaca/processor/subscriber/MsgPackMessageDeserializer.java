package br.com.flallaca.processor.subscriber;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import org.msgpack.jackson.dataformat.MessagePackMapper;

import java.util.ArrayList;

public class MsgPackMessageDeserializer implements MessageDeserializer<ResponseSkeletonDTO> {
    @Override
    public ResponseSkeletonDTO deserialize(byte[] data) {
        try {
            return new MessagePackMapper().readValue(data, ResponseSkeletonDTO.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
