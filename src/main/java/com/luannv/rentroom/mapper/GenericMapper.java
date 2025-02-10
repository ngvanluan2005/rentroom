package com.luannv.rentroom.mapper;

public interface GenericMapper<Entity, RequestDTO, ResponseDTO> {
    Entity toEntity(RequestDTO requestDTO);
    ResponseDTO toResponseDTO(Entity entity);
}
