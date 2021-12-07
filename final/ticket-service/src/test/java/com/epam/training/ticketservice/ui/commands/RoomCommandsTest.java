package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.shell.Availability;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomCommandsTest {

    private final RoomService roomService = mock(RoomService.class);
    private final UserService userService = mock(UserService.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);

    private RoomCommands underTest = new RoomCommands(roomService, userService);

    RoomDto roomDto = new RoomDto("R1", 10, 10);

    @Test
    public void testListRoomsShouldReturnCorrectListOfExistingRooms() {
        //Given
        List<RoomDto> list = List.of(
                RoomDto.builder()
                        .withName("testRoom")
                        .withSeatRows(10)
                        .withSeatCols(10)
                        .build());
        when(roomService.getRoomList()).thenReturn(list);
        String expected = "[Room testRoom with 100 seats, 10 rows and 10 columns]";

        //When
        String actual = underTest.listAvailableRooms().toString();

        //Then
        assertEquals(expected, actual);
    }

    @Test
    public void testListRoomsShouldReturnEmptyList() {
        //Given
        when(roomService.getRoomList()).thenReturn(List.of());
        String expected = "There are no rooms at the moment";

        //When
        String actual = underTest.listAvailableRooms().toString();

        //Then
        assertEquals(expected, actual);
        verify(roomService).getRoomList();
    }

    @Test
    public void testDeleteRoomShouldReturnErrorMessage() {
        RoomDto roomDto = new RoomDto("testRoom", 10, 10);

        String expected = "Room does not exist";
        String actual = underTest.deleteRoom("testRoom");

        verify(roomService).getRoomByName("testRoom");
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteRoomShouldReturnString() {
        RoomDto roomDto = new RoomDto("testRoom", 10, 10);
        when(underTest.listAvailableRooms()).thenReturn(List.of(roomDto));
        when(roomService.getRoomByName("testRoom")).thenReturn(Optional.of(roomDto));
        String expected = "testRoom is deleted";
        String actual = underTest.deleteRoom("testRoom");

        verify(roomService).getRoomByName("testRoom");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin!");

        Method isAvailable = RoomCommands.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);

        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    public void testIsAvailableAdmin() throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        UserDto userDto = new UserDto("user", User.Role.ADMIN);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.available();

        Method isAvailable = RoomCommands.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);

        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
}