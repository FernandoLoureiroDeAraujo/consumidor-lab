package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.dto.TransactionAmountDTO;
import br.com.flallaca.consumer.dto.TransactionDTO;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;

public class KryoMessageSerializer implements MessageSerializer {

    @Override
    public byte[] serialize(ResponseSkeletonDTO responseDTO) {

        var kryo = new Kryo();
        kryo.register(ArrayList.class);
        kryo.register(ResponseSkeletonDTO.class);
        kryo.register(TransactionDTO.class);
        kryo.register(TransactionAmountDTO.class);

        try (Output output = new Output(4096, -1)) {
            kryo.writeClassAndObject(output, responseDTO);
            return output.toBytes();
        }
    }
}
