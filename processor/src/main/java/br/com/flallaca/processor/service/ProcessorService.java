package br.com.flallaca.processor.service;

import br.com.flallaca.processor.dto.ResponseSkeletonDTO;
import br.com.flallaca.processor.proto.AccountTransaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProcessorService {

    public void processor(ResponseSkeletonDTO response) {
        log.info(response.getData());
    }

    public void processor(AccountTransaction.ResponseSkeleton response) {
        log.info(response.getDataList());
    }
}
