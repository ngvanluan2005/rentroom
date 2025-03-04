package com.luannv.rentroom.dto.request;

import lombok.Getter;

@Getter
public class RoomImagesRequest {
  private int room_id;
  private byte[] image;
}
