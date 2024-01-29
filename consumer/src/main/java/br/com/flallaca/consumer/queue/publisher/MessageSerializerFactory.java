package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.enums.MessageFormatType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class MessageSerializerFactory {

    private static final Map<MessageFormatType, Supplier<MessageSerializer>> serializers = new HashMap<>();

    static {
        serializers.put(MessageFormatType.JSON, JsonJacksonMessageSerializer::new);
        serializers.put(MessageFormatType.PROTOBUF, ProtobufMessageSerializer::new);
        serializers.put(MessageFormatType.PROTOSTUFF, ProtostuffMessageSerializer::new);
        serializers.put(MessageFormatType.MSGPACK, MsgPackMessageSerializer::new);
        serializers.put(MessageFormatType.KRYO, KryoMessageSerializer::new);
    }

    public static MessageSerializer createSerializer(MessageFormatType messageFormatType) {

        var serializer = serializers.get(messageFormatType);
        if (Objects.isNull(serializer)) {
            throw new IllegalArgumentException("Invalid Message Format: " + messageFormatType);
        }

        return serializer.get();
    }
}
