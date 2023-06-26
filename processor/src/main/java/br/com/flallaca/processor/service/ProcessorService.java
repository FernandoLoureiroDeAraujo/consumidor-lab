package br.com.flallaca.processor.service;

import br.com.flallaca.processor.model.ResponseObject;
import br.com.flallaca.processor.model.Transaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class ProcessorService {

    public void processor(ResponseObject response) {
        log.info(response.getData());
    }

}
