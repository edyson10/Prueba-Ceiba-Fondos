package com.ceiba.pruebaceiba.service.impl;

import com.ceiba.pruebaceiba.dto.response.FundResponse;
import com.ceiba.pruebaceiba.mapper.FundMapper;
import com.ceiba.pruebaceiba.repository.FundRepository;
import com.ceiba.pruebaceiba.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;
    private final FundMapper fundMapper;

    @Override
    public List<FundResponse> getActiveFunds() {
        return fundRepository.findByActiveTrue()
                .stream()
                .map(fundMapper::toResponse)
                .toList();
    }
}