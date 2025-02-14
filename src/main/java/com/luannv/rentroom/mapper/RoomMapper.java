package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.request.RoomRequestDTO;
import com.luannv.rentroom.entity.Room;

public class RoomMapper implements GenericMapper<Room, RoomRequestDTO, Object>{
    @Override
    public Room toEntity(RoomRequestDTO roomRequestDTO) {
        return null;
    }

    @Override
    public Object toResponseDTO(Room room) {
        return null;
    }
}
