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
        Player player = playerService.getBalanceById(playerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<PlayerDTO>(modelMapper.map(player,PlayerDTO.class),HttpStatus.OK);

    }

    @PostMapping("/player/{playerId}/balance/update")
    public ResponseEntity<TransactionResultDTO> updateBalance(@RequestBody Transaction transaction) {
        Player player = playerService.getBalanceById(transaction.getPlayerId());

        if (player == null || transaction.getAmount() < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (transaction.getAmount() > player.getBalance() && transaction.getTransactionType().equals("WAGER")) {
            return new ResponseEntity<>(HttpStatus.valueOf(418));
        }

        int balance;
        if (transaction.getTransactionType().equals("WAGER")){
            balance = player.getBalance() - transaction.getAmount();
        }else{
            balance = player.getBalance() + transaction.getAmount();
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
        Player foundPlayer  = playerService.findByUsername(player.getUsername());

        if (foundPlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(transactionService.getTransactionsByUserId(foundPlayer.getId()).stream().map(post -> modelMapper.map(post, TransactionDTO.class)).collect(Collectors.toList()),HttpStatus.OK);
    }
}
