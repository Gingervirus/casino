package com.springrank.casino;

import com.springrank.casino.model.Player;
import com.springrank.casino.model.Transaction;
import com.springrank.casino.service.PlayerService;
import com.springrank.casino.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CasinoApplicationTests {
    @Autowired
    PlayerService playerService = new PlayerService();
    @Autowired
    TransactionService transactionService = new TransactionService();
    @Test
    void contextLoads() {
    }

    @Test
    void testGetBalanceById(){
        Player player = playerService.getBalanceById(1l);
        Assertions.assertEquals(player.getUsername(),"awentzel");
    }

    @Test
    void testCreateTransaction(){
        Transaction transaction = new Transaction();
        transaction.setTransactionType("WIN");
        transaction.setAmount(500);
        Transaction newTransaction = transactionService.createTransaction(transaction);
        Assertions.assertNotNull(newTransaction);
    }

    @Test
    void testUpdateBalance(){
        int newBalance = 600;
        Player updatedPlayer = playerService.updateBalance(new Player(1l,"awentzel", newBalance));
        Assertions.assertNotEquals(updatedPlayer.getBalance(),500);
    }

    @Test
    void getTransactions(){
        boolean success = false;
        Player foundPlayer  = playerService.findByUsername("awentzel");
        if(transactionService.getTransactionsByUserId(foundPlayer.getId()).size() > 0){
            success = true;
        }
        Assertions.assertTrue(success);

    }

}
