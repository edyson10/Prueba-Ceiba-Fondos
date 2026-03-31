package com.ceiba.pruebaceiba.service;

import com.ceiba.pruebaceiba.dto.response.FundResponse;

import java.util.List;

public interface FundService {
    List<FundResponse> getActiveFunds();
}
