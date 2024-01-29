package br.com.flallaca.processor.subscriber;



import br.com.flallaca.processor.enums.MessageFormatType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class MessageDeserializerFactory {

    private static final Map<MessageFormatType, Supplier<MessageDeserializer>> deserializers = new HashMap<>();

    static {
        deserializers.put(MessageFormatType.JSON, JsonJacksonMessageDeserializer::new);
        deserializers.put(MessageFormatType.PROTOBUF, ProtobufMessageDeserializer::new);
        deserializers.put(MessageFormatType.PROTOSTUFF, ProtostuffMessageDeserializer::new);
        deserializers.put(MessageFormatType.MSGPACK, MsgPackMessageDeserializer::new);
        deserializers.put(MessageFormatType.KRYO, KryoMessageDeserializer::new);
    }

    public static MessageDeserializer createDeserializer(MessageFormatType messageFormatType) {

        var deserializer = deserializers.get(messageFormatType);
        if (Objects.isNull(deserializer)) {
            throw new IllegalArgumentException("Invalid Message Format: " + messageFormatType);
        }

        return deserializer.get();
    }
}
