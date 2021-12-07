package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.pesistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.model.RoomDto;
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

class MovieCommandsTest {

    private final MovieService movieService = mock(MovieService.class);
    private final UserService userService = mock(UserService.class);
    private final MovieRepository movieRepository = mock(MovieRepository.class);

    private MovieCommands underTest = new MovieCommands(movieService, userService);

    MovieDto movieDto = new MovieDto("IT", "horror", 100);

    @Test
    public void testListMoviesShouldReturnCorrectListOfExistingMovies() {
        //Given
        List<MovieDto> list = List.of(
                MovieDto.builder()
                        .withTitle("Spirited Away")
                        .withGenre("animation")
                        .withMovieLength(125)
                        .build());
        when(movieService.getMovieList()).thenReturn(list);
        String expected = "[Spirited Away (animation, 125 minutes)]";

        //When
        String actual = underTest.listAvailableMovies().toString();

        //Then
        assertEquals(expected, actual);
    }

    @Test
    public void testListMoviesShouldReturnEmptyList() {
        //Given
        when(movieService.getMovieList()).thenReturn(List.of());
        String expected = "There are no movies at the moment";

        //When
        String actual = underTest.listAvailableMovies().toString();

        //Then
        assertEquals(expected, actual);
        verify(movieService).getMovieList();
    }

    @Test
    public void testCreateMovie() {
        MovieDto movieDto = new MovieDto("testMovie", "genre", 100);


        MovieDto actual = underTest.createMovie("testMovie", "genre", 100);

        verify(movieService).createMovie(movieDto);
        assertEquals(movieDto, actual);
    }

    @Test
    public void testDeleteMovieShouldReturnErrorMessage() {
        MovieDto movieDto = new MovieDto("testMovie", "genre", 100);

        String expected = "Movie does not exist";
        String actual = underTest.deleteMovie("testMovie");

        verify(movieService).getMovieByName("testMovie");
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteRoomShouldReturnString() {
        MovieDto movieDto = new MovieDto("testMovie", "genre", 100);
        when(underTest.listAvailableMovies()).thenReturn(List.of(movieDto));
        when(movieService.getMovieByName("testMovie")).thenReturn(Optional.of(movieDto));
        String expected = "testMovie is deleted";
        String actual = underTest.deleteMovie("testMovie");

        verify(movieService).getMovieByName("testMovie");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        UserDto userDto = new UserDto("user", User.Role.USER);
        when(userService.getLoggedInUser()).thenReturn(Optional.of(userDto));
        Availability expected = Availability.unavailable("You are not an admin!");

        Method isAvailable = MovieCommands.class.getDeclaredMethod("isAvailable");
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

        Method isAvailable = MovieCommands.class.getDeclaredMethod("isAvailable");
        isAvailable.setAccessible(true);
        Availability actual = (Availability)isAvailable.invoke(underTest);

        assertEquals(expected.isAvailable(), actual.isAvailable());
        assertEquals(expected.getReason(), actual.getReason());
    }

}