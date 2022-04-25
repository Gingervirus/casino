package com.springrank.casino.service;

import com.springrank.casino.model.Transaction;
import com.springrank.casino.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUserId(Long id) {
        return transactionRepository.getTransactionsByUserId(id);
    }
}
