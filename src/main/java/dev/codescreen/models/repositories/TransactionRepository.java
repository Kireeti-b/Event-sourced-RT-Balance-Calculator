package dev.codescreen.models.repositories;

import dev.codescreen.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    List<Transaction> findByUserId(String userId);
    long count();

}
