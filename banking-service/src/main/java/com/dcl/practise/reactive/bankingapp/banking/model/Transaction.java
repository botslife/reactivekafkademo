package com.dcl.practise.reactive.bankingapp.banking.model;

import com.dcl.practise.reactive.bankingapp.banking.model.constant.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@ToString
@NoArgsConstructor
public class Transaction {

    @Id
    @JsonProperty("transaction_id")
    private String transactionId;
    private Date lastUpdated;
    @JsonProperty("amount_deducted")
    private double amountDebited;
    @JsonProperty("store_name")
    private String storeName;
    @JsonProperty("store_id")
    private String storeId;
    @JsonProperty("card_id")
    private String cardId;
    @JsonProperty("transaction_location")
    private String transactionLocation;
    private TransactionStatus status;
}
