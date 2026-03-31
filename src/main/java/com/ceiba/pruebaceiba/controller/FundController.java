package com.ceiba.pruebaceiba.controller;

import com.ceiba.pruebaceiba.dto.response.ApiResponse;
import com.ceiba.pruebaceiba.dto.response.FundResponse;
import com.ceiba.pruebaceiba.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FundResponse>>> getFunds() {
        List<FundResponse> response = fundService.getActiveFunds();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "OK",
                        "Fondos consultados correctamente",
                        response
                )
        );
    }
}
