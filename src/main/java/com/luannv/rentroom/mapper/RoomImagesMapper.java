package com.luannv.rentroom.mapper;

import com.luannv.rentroom.dto.request.RoomImagesRequest;
import com.luannv.rentroom.entity.RoomImagesEntity;

public class RoomImagesMapper implements GenericMapper<RoomImagesEntity, RoomImagesRequest, Object> {

  @Override
  public RoomImagesEntity toEntity(RoomImagesRequest requestDTO) {
    return null;
  }

  @Override
  public Object toResponseDTO(RoomImagesEntity entity) {
    return null;
  }

}
