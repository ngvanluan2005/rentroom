package com.luannv.rentroom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.luannv.rentroom.constants.UrlConstants.API_ROOM;
import com.luannv.rentroom.dto.request.RoomRequestDTO;
import com.luannv.rentroom.entity.Room;
import com.luannv.rentroom.service.RoomService;

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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Room addRoom(@ModelAttribute RoomRequestDTO room, @RequestParam("img") MultipartFile multipartFile) {
        return this.roomService.addRoom(room, multipartFile);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<?> getImg(@PathVariable Long id) {
        Room room = this.roomService.findRoomById(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(room.getList(), httpHeaders, HttpStatus.OK);
    }
}
