package com.luannv.rentroom.dto.request;

import java.util.List;

import com.luannv.rentroom.entity.RoomImagesEntity;

import lombok.Getter;

@Getter
public class RoomRequestDTO {
  private String title;
  private List<RoomImagesEntity> request; // RoomImagesRequest
}
