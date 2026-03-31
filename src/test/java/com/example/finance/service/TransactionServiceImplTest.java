package com.example.finance.service;

import com.example.finance.dto.MonthlyStatsDto;
import com.example.finance.entity.Category;
import com.example.finance.entity.Transaction;
import com.example.finance.entity.TransactionType;
import com.example.finance.mapper.TransactionMapper;
import com.example.finance.repository.CategoryRepository;
import com.example.finance.repository.TransactionRepository;
import com.example.finance.repository.UserRepository;
import com.example.finance.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Category food;
    private Category salary;

    @BeforeEach
    void setUp() {
        food = new Category();
        food.setId(1L);
        food.setName("Еда");

        salary = new Category();
        salary.setId(2L);
        salary.setName("Зарплата");
    }

    @Test
    void getCurrentMonthStats_shouldAggregateByTypeAndCategory() {
        String username = "demo";

        Transaction expense = new Transaction();
        expense.setAmount(new BigDecimal("100.00"));
        expense.setType(TransactionType.EXPENSE);
        expense.setCategory(food);
        expense.setTimestamp(LocalDateTime.now());

        Transaction income = new Transaction();
        income.setAmount(new BigDecimal("2000.00"));
        income.setType(TransactionType.INCOME);
        income.setCategory(salary);
        income.setTimestamp(LocalDateTime.now());

        LocalDateTime monthStart = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime monthEnd = YearMonth.now().plusMonths(1).atDay(1).atStartOfDay();

        when(transactionRepository.findAllByUserUsernameAndTimestampBetween(username, monthStart, monthEnd))
                .thenReturn(List.of(expense, income));

        MonthlyStatsDto stats = transactionService.getCurrentMonthStats(username);

        assertEquals(new BigDecimal("2000.00"), stats.getTotalIncome());
        assertEquals(new BigDecimal("100.00"), stats.getTotalExpense());
        assertEquals(1, stats.getIncomeByCategory().size());
        assertEquals("Зарплата", stats.getIncomeByCategory().get(0).getCategory());
        assertEquals(new BigDecimal("2000.00"), stats.getIncomeByCategory().get(0).getTotal());
        assertEquals(1, stats.getExpenseByCategory().size());
        assertEquals("Еда", stats.getExpenseByCategory().get(0).getCategory());
        assertEquals(new BigDecimal("100.00"), stats.getExpenseByCategory().get(0).getTotal());
    }
}
