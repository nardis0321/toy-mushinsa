package com.toyproject2_5.musinsatoy.salesorder.mapper;

import com.toyproject2_5.musinsatoy.salesorder.dto.*;
import com.toyproject2_5.musinsatoy.salesorder.entity.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SalesOrderDtoMapper {
    SalesOrderDtoMapper INSTANCE = Mappers.getMapper(SalesOrderDtoMapper.class);

    // SalesOrder <-> SalesOrderDto
    SalesOrderDto toDto(SalesOrder salesOrder);
    SalesOrder toEntity(SalesOrderDto salesOrderDto);
}
