package com.example.atm.service;

import com.example.atm.model.Transaction;
import com.example.atm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByAccountId(Integer accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
