package com.springrank.casino.service;

import com.springrank.casino.model.Player;
import com.springrank.casino.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player getBalanceById(Long id){
        playerRepository.findAll();
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional
    public Player updateBalance(Player player){
          return playerRepository.save(player);
    }

    public Player findByUsername(String username){
        List<Player> players = playerRepository.findByUsername(username);
        if (players.size() == 1){
            return players.get(0);
        }
        return null;
    }
}
