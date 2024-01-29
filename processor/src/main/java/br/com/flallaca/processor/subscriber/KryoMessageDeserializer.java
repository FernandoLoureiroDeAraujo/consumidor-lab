package br.com.flallaca.processor.subscriber;


import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import br.com.flallaca.processor.dto.TransactionAmountDTO;
import br.com.flallaca.processor.dto.TransactionDTO;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.util.ArrayList;

public class KryoMessageDeserializer implements MessageDeserializer<ResponseSkeletonDTO> {

    @Override
    public ResponseSkeletonDTO deserialize(byte[] data) {
        try {
            var kryo = new Kryo();
            kryo.register(ArrayList.class);
            kryo.register(ResponseSkeletonDTO.class);
            kryo.register(TransactionDTO.class);
            kryo.register(TransactionAmountDTO.class);

            try (Input input = new Input(data)) {
                return (ResponseSkeletonDTO) kryo.readClassAndObject(input);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
