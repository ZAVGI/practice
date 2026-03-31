package com.example.finance.service.impl;

import com.example.finance.dto.CategoryStatDto;
import com.example.finance.dto.MonthlyStatsDto;
import com.example.finance.dto.TransactionRequestDto;
import com.example.finance.dto.TransactionResponseDto;
import com.example.finance.entity.Category;
import com.example.finance.entity.Transaction;
import com.example.finance.entity.TransactionType;
import com.example.finance.entity.User;
import com.example.finance.mapper.TransactionMapper;
import com.example.finance.repository.CategoryRepository;
import com.example.finance.repository.TransactionRepository;
import com.example.finance.repository.UserRepository;
import com.example.finance.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final BigDecimal ZERO_AMOUNT = BigDecimal.ZERO;

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponseDto create(TransactionRequestDto requestDto, String username) {
        User user = findUserByUsername(username);
        Category category = findCategoryById(requestDto.getCategoryId());

        Transaction transaction = new Transaction();
        applyRequest(transaction, requestDto, category, user);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionResponseDto> findAll(String username) {
        return transactionRepository.findAllByUserUsername(username).stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @Override
    public TransactionResponseDto findById(Long id, String username) {
        return transactionMapper.toDto(findUserTransaction(id, username));
    }

    @Override
    public TransactionResponseDto update(Long id, TransactionRequestDto requestDto, String username) {
        Transaction transaction = findUserTransaction(id, username);
        Category category = findCategoryById(requestDto.getCategoryId());

        applyRequest(transaction, requestDto, category, transaction.getUser());
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Override
    public void delete(Long id, String username) {
        transactionRepository.delete(findUserTransaction(id, username));
    }

    @Override
    public MonthlyStatsDto getCurrentMonthStats(String username) {
        LocalDate startDate = YearMonth.now().atDay(1);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = startDate.plusMonths(1).atStartOfDay();

        List<Transaction> monthTransactions = transactionRepository.findAllByUserUsernameAndTimestampBetween(
                username,
                startDateTime,
                endDateTime
        );

        Map<TransactionType, List<Transaction>> transactionsByType = monthTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getType));

        return MonthlyStatsDto.builder()
                .totalIncome(sumByType(transactionsByType, TransactionType.INCOME))
                .totalExpense(sumByType(transactionsByType, TransactionType.EXPENSE))
                .incomeByCategory(groupByCategory(transactionsByType.getOrDefault(TransactionType.INCOME, List.of())))
                .expenseByCategory(groupByCategory(transactionsByType.getOrDefault(TransactionType.EXPENSE, List.of())))
                .build();
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    private Transaction findUserTransaction(Long id, String username) {
        return transactionRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }

    private void applyRequest(Transaction transaction, TransactionRequestDto requestDto, Category category, User user) {
        transaction.setAmount(requestDto.getAmount());
        transaction.setDescription(requestDto.getDescription());
        transaction.setType(requestDto.getType());
        transaction.setTimestamp(requestDto.getTimestamp() == null ? LocalDateTime.now() : requestDto.getTimestamp());
        transaction.setCategory(category);
        transaction.setUser(user);
    }

    private BigDecimal sumByType(Map<TransactionType, List<Transaction>> transactionsByType, TransactionType type) {
        return transactionsByType.getOrDefault(type, List.of()).stream()
                .map(Transaction::getAmount)
                .reduce(ZERO_AMOUNT, BigDecimal::add);
    }

    private List<CategoryStatDto> groupByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getCategory().getName(),
                        Collectors.reducing(ZERO_AMOUNT, Transaction::getAmount, BigDecimal::add)))
                .entrySet()
                .stream()
                .map(entry -> new CategoryStatDto(entry.getKey(), entry.getValue()))
                .toList();
    }
}
