package com.example.finance.controller;

import com.example.finance.dto.MonthlyStatsDto;
import com.example.finance.dto.TransactionRequestDto;
import com.example.finance.dto.TransactionResponseDto;
import com.example.finance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDto create(@Valid @RequestBody TransactionRequestDto requestDto,
                                         Authentication authentication) {
        return transactionService.create(requestDto, authentication.getName());
    }

    @GetMapping
    public List<TransactionResponseDto> getAll(Authentication authentication) {
        return transactionService.findAll(authentication.getName());
    }

    @GetMapping("/{id}")
    public TransactionResponseDto getById(@PathVariable Long id, Authentication authentication) {
        return transactionService.findById(id, authentication.getName());
    }

    @PutMapping("/{id}")
    public TransactionResponseDto update(@PathVariable Long id,
                                         @Valid @RequestBody TransactionRequestDto requestDto,
                                         Authentication authentication) {
        return transactionService.update(id, requestDto, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        transactionService.delete(id, authentication.getName());
    }

    @GetMapping("/stats/monthly")
    public MonthlyStatsDto getCurrentMonthStats(Authentication authentication) {
        return transactionService.getCurrentMonthStats(authentication.getName());
    }
}
