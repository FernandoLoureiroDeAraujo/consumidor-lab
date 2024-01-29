package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.dto.TransactionAmountDTO;
import br.com.flallaca.consumer.dto.TransactionDTO;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.ArrayList;

public class MsgPackMessageSerializer implements MessageSerializer {
    @Override
    public byte[] serialize(ResponseSkeletonDTO responseDTO) {
        try {

            var msgpack = new MessagePack();
            msgpack.register(ArrayList.class);
            msgpack.register(ResponseSkeletonDTO.class);
            msgpack.register(TransactionDTO.class);
            msgpack.register(TransactionAmountDTO.class);

            return msgpack.write(responseDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
