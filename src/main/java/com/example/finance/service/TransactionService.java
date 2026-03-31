package com.example.finance.service;

import com.example.finance.dto.MonthlyStatsDto;
import com.example.finance.dto.TransactionRequestDto;
import com.example.finance.dto.TransactionResponseDto;

import java.util.List;

public interface TransactionService {
    TransactionResponseDto create(TransactionRequestDto requestDto, String username);

    List<TransactionResponseDto> findAll(String username);

    TransactionResponseDto findById(Long id, String username);

    TransactionResponseDto update(Long id, TransactionRequestDto requestDto, String username);

    void delete(Long id, String username);

    MonthlyStatsDto getCurrentMonthStats(String username);
}
