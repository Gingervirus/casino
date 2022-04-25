package com.springrank.casino.repository;

import com.springrank.casino.model.Player;
import com.springrank.casino.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    @Query(value = "SELECT * FROM TRANSACTION WHERE PLAYER_ID = ?1 ORDER BY TRANSACTION_ID DESC Limit 10",nativeQuery = true)
    //@Query(value = "SELECT t.transactionId,t.amount,t.transactionType FROM Transaction t WHERE t.playerId = ?1 ORDER BY t.transactionId DESC")
    List<Transaction> getTransactionsByUserId(Long playerId);
}
