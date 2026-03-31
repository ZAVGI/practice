package com.example.finance.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class MonthlyStatsDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private List<CategoryStatDto> incomeByCategory;
    private List<CategoryStatDto> expenseByCategory;
}
