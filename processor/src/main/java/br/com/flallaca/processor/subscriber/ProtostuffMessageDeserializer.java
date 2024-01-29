package br.com.flallaca.processor.subscriber;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffMessageDeserializer implements MessageDeserializer<ResponseSkeletonDTO> {
    @Override
    public ResponseSkeletonDTO deserialize(byte[] data) {
        try {
            var schema = RuntimeSchema.createFrom(ResponseSkeletonDTO.class);
            var response = schema.newMessage();
            ProtobufIOUtil.mergeFrom(data, response, schema);
            return response;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
