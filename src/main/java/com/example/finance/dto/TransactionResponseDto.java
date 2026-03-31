package com.example.finance.dto;

import com.example.finance.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private String description;
    private TransactionType type;
    private LocalDateTime timestamp;
    private Long userId;
    private Long categoryId;
    private String categoryName;
}
