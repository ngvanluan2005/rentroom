package com.luannv.rentroom.dto.response;

import java.util.List;

import com.luannv.rentroom.entity.RoomImagesEntity;

import lombok.Setter;

@Setter
public class RoomResponse {
  private String title;
  private List<RoomImagesEntity> images;
}
