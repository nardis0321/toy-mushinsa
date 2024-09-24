package com.toyproject2_5.musinsatoy.member.mapper;

import com.toyproject2_5.musinsatoy.member.join.dto.MemberDto;
import com.toyproject2_5.musinsatoy.member.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberDtoMapper {
    MemberDtoMapper INSTANCE = Mappers.getMapper(MemberDtoMapper.class);

    MemberDto toDto(Member member);
    Member toEntity(MemberDto memberDto);
}
