package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RoomServiceImplTest {

    private static final Room R1_ENTITY = new Room("R1", 10, 9);
    private static final Room R2_ENTITY = new Room("R2", 10, 10);

    private final static RoomDto R1_DTO = new RoomDto.Builder()
            .withName(R1_ENTITY.getName())
            .withSeatRows(R1_ENTITY.getSeatRows())
            .withSeatCols(R1_ENTITY.getSeatCols())
            .build();

    private final static RoomDto R2_DTO = new RoomDto.Builder()
            .withName(R2_ENTITY.getName())
            .withSeatRows(R2_ENTITY.getSeatRows())
            .withSeatCols(R2_ENTITY.getSeatCols())
            .build();

    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final RoomServiceImpl underTest = new RoomServiceImpl(roomRepository);

    @Test
    public void testGetRoomListShouldCallRoomRepositoryAndReturnADtoList() {
        // Given
        when(roomRepository.findAll()).thenReturn(List.of(R1_ENTITY, R2_ENTITY));
        List<RoomDto> expected = List.of(R1_DTO, R2_DTO);

        // When
        List<RoomDto> actual = underTest.getRoomList();

        // Then
        assertEquals(expected, actual);
        verify(roomRepository).findAll();
    }

    @Test
    public void testGetRoomByNameShouldReturnAnR1DtoWhenTheRoomExists() {
        // Given
        when(roomRepository.findByName("R1")).thenReturn(Optional.of(R1_ENTITY));
        Optional<RoomDto> expected = Optional.of(R1_DTO);

        // When
        Optional<RoomDto> actual = underTest.getRoomByName("R1");

        // Then
        assertEquals(expected, actual);
        verify(roomRepository).findByName("R1");
    }

    @Test
    public void testGetRoomByNameShouldReturnOptionalEmptyWhenInputRoomNameDoesNotExist() {
        // Given
        when(roomRepository.findByName("dummy")).thenReturn(Optional.empty());
        Optional<RoomDto> expected = Optional.empty();

        // When
        Optional<RoomDto> actual = underTest.getRoomByName("dummy");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(roomRepository).findByName("dummy");
    }

    @Test
    public void testGetRoomByNameShouldReturnOptionalEmptyWhenInputRoomNameIsNull() {
        // Given
        when(roomRepository.findByName(null)).thenReturn(Optional.empty());
        Optional<RoomDto> expected = Optional.empty();

        // When
        Optional<RoomDto> actual = underTest.getRoomByName(null);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(roomRepository).findByName(null);
    }

    @Test
    public void testCreateRoomShouldCallRoomRepositoryWhenTheInputIsValid() {
        // Given
        when(roomRepository.save(R2_ENTITY)).thenReturn(R2_ENTITY);

        // When
        underTest.createRoom(R2_DTO);

        // Then
        verify(roomRepository).save(R2_ENTITY);
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(null));
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenRoomNameIsNull() {
        // Given
        RoomDto room = new RoomDto.Builder()
                .withName(null)
                .withSeatRows(10)
                .withSeatCols(9)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(room));
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenSeatRowIsNull() {
        // Given
        RoomDto room = new RoomDto.Builder()
                .withName("R3")
                .withSeatRows(null)
                .withSeatCols(9)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(room));
    }

    @Test
    public void testCreateRoomShouldThrowNullPointerExceptionWhenSeatColIsNull() {
        // Given
        RoomDto room = new RoomDto.Builder()
                .withName("R3")
                .withSeatRows(5)
                .withSeatCols(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createRoom(room));
    }
}