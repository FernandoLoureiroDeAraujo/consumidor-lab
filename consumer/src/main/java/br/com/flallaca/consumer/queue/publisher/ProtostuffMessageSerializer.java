package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffMessageSerializer implements MessageSerializer {
    @Override
    public byte[] serialize(ResponseSkeletonDTO responseDTO) {

        var buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        try {
            return ProtobufIOUtil.toByteArray(responseDTO,
                    RuntimeSchema.createFrom(ResponseSkeletonDTO.class),
                    buffer);
        } finally {
            buffer.clear();
        }
    }
}
