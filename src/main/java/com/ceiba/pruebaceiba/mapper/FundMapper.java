package com.ceiba.pruebaceiba.mapper;

import com.ceiba.pruebaceiba.dto.response.FundResponse;
import com.ceiba.pruebaceiba.model.Fund;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FundMapper {
    FundResponse toResponse(Fund fund);
}
