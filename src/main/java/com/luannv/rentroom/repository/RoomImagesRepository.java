package com.luannv.rentroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luannv.rentroom.entity.RoomImagesEntity;

@Repository
public interface RoomImagesRepository extends JpaRepository<RoomImagesEntity, Integer> {

}
