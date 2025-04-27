package com.bootcoin.p2p.bootcoin.service.controller;

import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionRequest;
import com.bootcoin.p2p.bootcoin.service.bean.transaction.TransactionResponse;
import com.bootcoin.p2p.bootcoin.service.service.TransactionService;
import com.bootcoin.p2p.bootcoin.service.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bootcoin/transactions")
@Slf4j
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<TransactionResponse> getTransactionsByWallet(@PathVariable ("walletId") String walletId) {
        log.info("-> Init transactions by wallet {}", walletId);
        return transactionService.getTransactionsByWallet(walletId);

    }
}
