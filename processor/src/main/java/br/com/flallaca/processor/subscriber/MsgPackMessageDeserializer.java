package br.com.flallaca.processor.subscriber;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import br.com.flallaca.processor.dto.TransactionAmountDTO;
import br.com.flallaca.processor.dto.TransactionDTO;
import org.msgpack.MessagePack;

import java.util.ArrayList;

public class MsgPackMessageDeserializer implements MessageDeserializer<ResponseSkeletonDTO> {
    @Override
    public ResponseSkeletonDTO deserialize(byte[] data) {
        try {
            var msgpack = new MessagePack();
            msgpack.register(ResponseSkeletonDTO.class);
            msgpack.register(ArrayList.class);
            msgpack.register(TransactionDTO.class);
            msgpack.register(TransactionAmountDTO.class);

            return msgpack.read(data, ResponseSkeletonDTO.class);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
