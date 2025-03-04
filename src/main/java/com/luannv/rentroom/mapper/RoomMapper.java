package com.luannv.rentroom.mapper;

import org.springframework.stereotype.Component;

import com.luannv.rentroom.dto.request.RoomRequestDTO;
import com.luannv.rentroom.entity.Room;

@Component
public class RoomMapper implements GenericMapper<Room, RoomRequestDTO, Object> {
    @Override
    public Room toEntity(RoomRequestDTO roomRequestDTO) {
        return Room.builder()
                .title(roomRequestDTO.getTitle())
                .list(roomRequestDTO.getRequest())
                .build();
    }

    @Override
    public Object toResponseDTO(Room room) {
        return null;
    }
}
