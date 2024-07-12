package dev.RTBalanceCalculator.models.repositories;

import dev.RTBalanceCalculator.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    List<Transaction> findByUserId(String userId);
    long count();

}
