package com.luannv.rentroom.service;

import com.luannv.rentroom.entity.Room;
import com.luannv.rentroom.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    public List<Room> getAllRoom() {
        return this.roomRepository.findAll();
    }
    public Room addRoom(Room room, MultipartFile multipartFile) {
        try {
            room.setRoomImages(multipartFile.getBytes());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return this.roomRepository.save(room);
    }
    public Room findRoomById(Long id) {
        return this.roomRepository.findById(id).get();
    }
}
