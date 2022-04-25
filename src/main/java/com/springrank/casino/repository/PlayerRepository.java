package com.springrank.casino.repository;

import com.springrank.casino.model.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player,Long> {

    @Query(value = "SELECT p FROM Player p WHERE p.username = ?1")
    public List<Player> findByUsername(String username);
}
