package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.shell.Availability;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreeningCommandsTest {

    private final RoomService roomService = mock(RoomService.class);
    private final UserService userService = mock(UserService.class);
    private final MovieService movieService = mock(MovieService.class);
    private final ScreeningService screeningService = mock(ScreeningService.class);

    private final ScreeningCommands underTest = new ScreeningCommands(screeningService, movieService,
            roomService, userService);

    MovieDto movieDto = new MovieDto("testMovie", "genre", 100);
    RoomDto roomDto = new RoomDto("testRoom", 10, 10);

    @Test
    public void testListRoomsShouldReturnCorrectListOfExistingRooms() {
        //Given
        List<ScreeningDto> list = List.of(
                ScreeningDto.builder()
                        .withMovie(movieDto)
                        .withRoom(roomDto)
                        .withScreeningDate(LocalDateTime.of(2021, 11, 25, 10, 0))
                        .build());
        when(screeningService.getScreeningList()).thenReturn(list);
        String expected = "[testMovie (genre, 100 minutes), screened in room testRoom, at 2021-11-25 10:00]";

        //When
        String actual = underTest.listAvailableScreenings().toString();

        //Then
        assertEquals(expected, actual);
    }

    @Test
    public void testListScreeningsShouldReturnEmptyList() {
        //Given
        when(screeningService.getScreeningList()).thenReturn(List.of());
        String expected = "There are no screenings";

        //When
        String actual = underTest.listAvailableScreenings().toString();

        //Then
        assertEquals(expected, actual);
        verify(screeningService).getScreeningList();
    }

    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin!");

        Method isAvailable = ScreeningCommands.class.getDeclaredMethod("isAvailable");
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

        Method isAvailable = ScreeningCommands.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);

        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }
}