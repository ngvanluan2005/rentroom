package com.luannv.rentroom.controller;

import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.entity.Room;
import com.luannv.rentroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.luannv.rentroom.constants.UrlConstants.API_ROOM;

@RestController
@RequestMapping(API_ROOM)
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping
    public List<Room> getAll() {
        return this.roomService.getAllRoom();
    }
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Room addRoom(@ModelAttribute Room room, @RequestParam("img") MultipartFile multipartFile) {
        return this.roomService.addRoom(room, multipartFile);
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<?> getImg(@PathVariable Long id) {
        Room room = this.roomService.findRoomById(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(room.getRoomImages(), httpHeaders, HttpStatus.OK);
    }
}
