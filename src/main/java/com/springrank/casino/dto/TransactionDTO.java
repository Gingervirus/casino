package com.springrank.casino.dto;

import lombok.Data;


@Data
public class TransactionDTO {
    private Long transactionId;
    private String transactionType;
    private int amount;
}
