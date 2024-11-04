package br.com.flallaca.accounts;

import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@Log4j2
@RestController
public class AccountsController {

    private TransactionVO transaction;

    @PostConstruct
    public void generateFakerData() {
        var faker = new Faker();
        var transactionAmount = new TransactionAmountVO().createTransactionAmount(faker);
        this.transaction = new TransactionVO().createTransaction(faker, transactionAmount);
    }

    @GetMapping("/accounts/transactions")
    public ResponseEntity<ResponseSkeletonVO> transactions(@RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
                                                           @RequestParam(name = "page-size", defaultValue = "25", required = false) Integer pageSize,
                                                           @RequestParam(name = "skip-delay", defaultValue = "true", required = false) boolean skipDelay) {

        log.info("Starting request");

        var transactions = new ArrayList<TransactionVO>();
        for (int x = 0; x < pageSize; x++) {
            transactions.add(this.transaction);
        }

        if ( ! skipDelay) {
            simulateResponseTime();
        }

        log.info("Ending request");

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseSkeletonVO(transactions));
    }

    private void simulateResponseTime() {

        var random = new Random();

        int maxSeconds = 5; // TODO CHANGE TO 60s
        int randomSeconds = random.nextInt(maxSeconds + 1);

        try {
            Thread.sleep(randomSeconds * 1000); // Convert seconds to milliseconds
            log.info("Slept for " + randomSeconds + " seconds.");
        } catch (InterruptedException e) {}
    }

    private HttpStatus simulateHttpStatus() {

        var random = new Random();
        var httpStatus = HttpStatus.values();
        var randomIndex = random.nextInt(httpStatus.length);

        return httpStatus[randomIndex];
    }
}
