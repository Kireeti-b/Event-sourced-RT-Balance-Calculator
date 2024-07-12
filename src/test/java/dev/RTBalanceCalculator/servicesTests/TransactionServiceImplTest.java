package dev.RTBalanceCalculator.servicesTests;
import dev.RTBalanceCalculator.models.Amount;
import dev.RTBalanceCalculator.models.Currency;
import dev.RTBalanceCalculator.models.Transaction;
import dev.RTBalanceCalculator.models.enums.DebitOrCredit;
import dev.RTBalanceCalculator.models.enums.ResponseCode;
import dev.RTBalanceCalculator.models.repositories.TransactionRepository;
import dev.RTBalanceCalculator.models.requests.Request;
import dev.RTBalanceCalculator.models.responses.*;
import dev.RTBalanceCalculator.services.TransactionServices.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Authorization request tests
    @Test
    void testAuthorizationWithSufficientBalanceAndDebitTransaction() {
        Request validDebitRequest = new Request("userId123", "msg001", new Amount(BigDecimal.valueOf(50.0), Currency.USD, DebitOrCredit.DEBIT));
        when(transactionRepository.findByUserId("userId123")).thenReturn(createSampleTransactions());

        Response response = transactionService.authorization("msg001", validDebitRequest);
        AuthorizationResponse response1 = (AuthorizationResponse)response;
        assertEquals(response1.getResponseCode(), ResponseCode.APPROVED);
    }

    @Test
    void testAuthorizationWithInsufficientBalanceAndDebitTransaction() {
        Request insufficientBalanceDebitRequest = new Request("userId123", "msg002", new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.DEBIT));
        when(transactionRepository.findByUserId("userId123")).thenReturn(createSampleTransactions());

        Response response = transactionService.authorization("msg002", insufficientBalanceDebitRequest);
        AuthorizationResponse response1 = (AuthorizationResponse)response;
        assertEquals(response1.getResponseCode(), ResponseCode.DECLINED);
    }

    @Test
    void testAuthorizationWithInvalidRequest(){
        List<Request> invalidRequests = new ArrayList<>();
        Request invalidRequest = new Request("1234", null, new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.DEBIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request(null, "1234", new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.DEBIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request("user1234", "1234", new Amount(null, Currency.USD, DebitOrCredit.DEBIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request("user1234", "1234", new Amount(BigDecimal.valueOf(500.0), null, DebitOrCredit.DEBIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request("user1234", "1234", new Amount(BigDecimal.valueOf(500.0), Currency.USD, null));
        invalidRequests.add(invalidRequest);


        when(transactionRepository.findByUserId("userId456")).thenReturn(createSampleTransactions());
        for (Request request: invalidRequests){
            Response response = transactionService.authorization(request.getMessageId(), request);
            assertTrue(response instanceof ErrorResponse);
        }
    }

    @Test
    void testAuthorizationWithCreditTransaction(){
        Request creditRequest = new Request("1234", "1234", new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.CREDIT));

        when(transactionRepository.findByUserId("userId456")).thenReturn(createSampleTransactions());
        Response response = transactionService.authorization(creditRequest.getMessageId(), creditRequest);
        AuthorizationResponse response1 = (AuthorizationResponse)response;
        assertEquals(response1.getResponseCode(), ResponseCode.DECLINED);
    }

    // Load Request tests
    @Test
    void testLoadWithValidRequest(){
        Request validRequest = new Request("userId456", "msg002", new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.CREDIT));
        when(transactionRepository.findByUserId("userId456")).thenReturn(createSampleTransactions());

        Response response = transactionService.load(validRequest.getMessageId(), validRequest);
        LoadResponse response1 = new LoadResponse();
        response1.setUserId(validRequest.getUserId());
        response1.setMessageId(validRequest.getMessageId());
        response1.setBalance(validRequest.getTransactionAmount());
        assertEquals(response1, response);
    }

    @Test
    void testLoadWithInvalidRequest(){
        List<Request> invalidRequests = new ArrayList<>();
        Request invalidRequest = new Request("1234", null, new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.CREDIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request(null, "1234", new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.CREDIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request("user1234", "1234", new Amount(null, Currency.USD, DebitOrCredit.CREDIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request("user1234", "1234", new Amount(BigDecimal.valueOf(500.0), null, DebitOrCredit.CREDIT));
        invalidRequests.add(invalidRequest);
        invalidRequest = new Request("user1234", "1234", new Amount(BigDecimal.valueOf(500.0), Currency.USD, null));
        invalidRequests.add(invalidRequest);

        when(transactionRepository.findByUserId("userId456")).thenReturn(createSampleTransactions());
        for (Request request: invalidRequests){
            Response response = transactionService.load(request.getMessageId(), request);
            assertTrue(response instanceof ErrorResponse);
        }
    }

    @Test
    void testLoadWithDebitTransaction(){
        Request debitRequest = new Request("1234", "1234", new Amount(BigDecimal.valueOf(500.0), Currency.USD, DebitOrCredit.DEBIT));

        when(transactionRepository.findByUserId("userId456")).thenReturn(createSampleTransactions());
        Response response = transactionService.load(debitRequest.getMessageId(), debitRequest);
        assertTrue(response instanceof ErrorResponse);
    }

    // Helper method to create sample transactions for testing
    private List<Transaction> createSampleTransactions() {
        //DEBIT Transaction
        List<Transaction> sampleTransactions = new ArrayList<>();
        Amount creditAmount = new Amount(BigDecimal.valueOf(200), Currency.USD, DebitOrCredit.CREDIT);
        sampleTransactions.add(new Transaction("user123","msg003",DebitOrCredit.CREDIT, creditAmount, ResponseCode.APPROVED));
        Amount debitAmount = new Amount(BigDecimal.valueOf(150.0), Currency.USD, DebitOrCredit.DEBIT);
        sampleTransactions.add(new Transaction("user123", "msg001", DebitOrCredit.DEBIT, debitAmount, ResponseCode.APPROVED));
        creditAmount = new Amount(BigDecimal.valueOf(300.0), Currency.EUR, DebitOrCredit.CREDIT);
        sampleTransactions.add(new Transaction("user123", "msg001", DebitOrCredit.CREDIT, creditAmount, ResponseCode.APPROVED));
        //total balance for user123: 50 USD and user456: 300 EUR
        return sampleTransactions;
    }
}
