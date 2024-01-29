package br.com.flallaca.consumer.queue.publisher;

import br.com.flallaca.consumer.dto.ResponseSkeletonDTO;

public interface MessageSerializer {
    byte[] serialize(ResponseSkeletonDTO responseDTO);
}
