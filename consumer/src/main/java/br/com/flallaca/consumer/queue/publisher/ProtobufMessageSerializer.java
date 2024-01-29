package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;
import br.com.flallaca.consumer.mapper.AccountTransactionMapper;

public class ProtobufMessageSerializer implements MessageSerializer {
    @Override
    public byte[] serialize(ResponseSkeletonDTO responseDTO) {
        var map = AccountTransactionMapper.INSTANCE.map(responseDTO);
        return map.toByteArray();
    }
}
