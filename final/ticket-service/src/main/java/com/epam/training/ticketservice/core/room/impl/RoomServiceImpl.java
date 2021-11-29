package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomDto> getRoomByName(String name) {
        return convertEntityToDto(roomRepository.findByName(name));
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getName(), "Room name cannot be null");
        Objects.requireNonNull(roomDto.getSeatRows(), "Number of rows cannot be null");
        Objects.requireNonNull(roomDto.getSeatCols(), "Number of columns cannot be null");
        Room room = new Room(roomDto.getName(),
                roomDto.getSeatRows(),
                roomDto.getSeatCols());
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Objects.requireNonNull(roomDto, "Room cannot be null");
        Objects.requireNonNull(roomDto.getName(), "Room name cannot be null");
        Room room = roomRepository.findByName(roomDto.getName())
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist"));
        room.setSeatRows(roomDto.getSeatRows());
        room.setSeatCols(roomDto.getSeatCols());
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        Objects.requireNonNull(name, "Room does not exist");
        roomRepository.deleteByName(name);
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .withName(room.getName())
                .withSeatRows(room.getSeatRows())
                .withSeatCols(room.getSeatCols())
                .build();
    }

    private Optional<RoomDto> convertEntityToDto(Optional<Room> room) {
        return room.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(room.get()));
    }
}
