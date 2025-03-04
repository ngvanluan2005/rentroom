package com.luannv.rentroom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luannv.rentroom.entity.Room;
import com.luannv.rentroom.mapper.RoomMapper;
import com.luannv.rentroom.repository.RoomImagesRepository;
import com.luannv.rentroom.repository.RoomRepository;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomImagesRepository roomImagesRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper,
            RoomImagesRepository roomImagesRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomImagesRepository = roomImagesRepository;
    }

    public List<Room> getAllRoom() {
        return this.roomRepository.findAll();
    }

    // public Room addRoom(RoomRequestDTO request, MultipartFile multipartFile) {
    // Room room = this.roomMapper.toEntity(request);
    // try {
    // List<RoomImagesEntity> list = request.getRequest();
    // room.setList();
    // } catch (IOException ioException) {
    // System.out.println(ioException.getMessage());
    // }
    // return;
    // }

    public Room findRoomById(Long id) {
        return this.roomRepository.findById(id).get();
    }
}
