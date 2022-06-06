package com.springrank.casino.api;

import com.springrank.casino.dto.PlayerDTO;
import com.springrank.casino.dto.TransactionResultDTO;
import com.springrank.casino.model.Player;
import com.springrank.casino.model.Transaction;
import com.springrank.casino.dto.TransactionDTO;
import com.springrank.casino.service.PlayerService;
import com.springrank.casino.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PlayerController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/player/{playerId}/balance")
    public ResponseEntity<PlayerDTO> getBalance(@PathVariable("playerId") Long playerId) {
        //Retrieve User Balance by id, Test User: 1l
        Player player = playerService.getBalanceById(playerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<PlayerDTO>(modelMapper.map(player,PlayerDTO.class),HttpStatus.OK);

    }

    @PostMapping("/player/{playerId}/balance/update")
    public ResponseEntity<?> updateBalance(@RequestBody Transaction transaction) {
        //Retrieve User Balance by id, Test User: 1l
        Player player = playerService.getBalanceById(transaction.getPlayerId());

        if (player == null || transaction.getAmount() < 0) {
            //If user supplied amount below 0 return error
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (transaction.getAmount() > player.getBalance() && transaction.getTransactionType().equals("WAGER")) {
            //If user WAGER is more than in account return Teacup Error
            return new ResponseEntity<>(HttpStatus.valueOf(418));
        }

        //update old balance to new balance if WAGER Subtract from balance else if a WIN then add to Balance
        int balance;
        if (transaction.getTransactionType().equals("WAGER")){
            balance = player.getBalance() - transaction.getAmount();
        }else if(transaction.getTransactionType().equals("WIN")){
            balance = player.getBalance() + transaction.getAmount();
        }else{
            //if user inserted incorrect Value for transaction type then throw error
            return new ResponseEntity<>("transactionType can Only be WAGER or WIN",HttpStatus.BAD_REQUEST);
        }

        //Update player balance
        playerService.updateBalance(new Player(player.getId(),player.getUsername(), balance));
        Transaction newTransaction = transactionService.createTransaction(transaction);

        //map transactinId and balance to TransactionResultDTO
        Map<String,Object> response = new HashMap<String, Object>();
        response.put("transactionId",transaction.getTransactionId());
        response.put("balance",player.getBalance());

        return new ResponseEntity<>(modelMapper.map(response, TransactionResultDTO.class),HttpStatus.OK);
    }

    @PostMapping("/admin/player/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestBody Player player){
        //Retrieve user based on username, custom native query
        Player foundPlayer  = playerService.findByUsername(player.getUsername());

        if (foundPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //returns a list and map to DTO that excludes the username from the list.
        return new ResponseEntity<>(transactionService.getTransactionsByUserId(foundPlayer.getId()).stream().map(post -> modelMapper.map(post, TransactionDTO.class)).collect(Collectors.toList()),HttpStatus.OK);
    }
}
