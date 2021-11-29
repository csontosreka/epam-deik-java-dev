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
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ShellComponent
public class ScreeningCommands {

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final UserService userService;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScreeningCommands(ScreeningService screeningService, MovieService movieService,
                             RoomService roomService, UserService userService) {
        this.screeningService = screeningService;
        this.movieService = movieService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethod(key = "list screenings", value = "List all available screenings")
    public Object listAvailableScreenings() {
        if (screeningService.getScreeningList().isEmpty()) {
            return "There are no screenings";
        } else {
            return screeningService.getScreeningList();
        }
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Create new screening")
    public ScreeningDto createScreening(String movieTitle, String roomName, String screeningDate) {
        LocalDateTime date = LocalDateTime.parse(screeningDate, dateFormat);
        Optional<MovieDto> movieDto = movieService.getMovieByName(movieTitle);
        Optional<RoomDto> roomDto = roomService.getRoomByName(roomName);

        if (movieDto.isEmpty() && roomDto.isEmpty()) {
            throw new NullPointerException("Movie and room do not exist");
        } else if (movieDto.isEmpty()) {
            throw new NullPointerException("Movie does not exist");
        } else if (roomDto.isEmpty()) {
            throw new NullPointerException("Room does not exist");
        }

        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(movieDto.get())
                .withRoom(roomDto.get())
                .withScreeningDate(date)
                .build();
        screeningService.createScreening(screeningDto);
        return screeningDto;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Deletes screening")
    public String deleteScreening(String movieTitle, String roomName, String screeningDate) {
        LocalDateTime date = LocalDateTime.parse(screeningDate, dateFormat);

        screeningService.deleteScreening(movieTitle, roomName, date);

        if (movieService.getMovieByName(movieTitle).isEmpty() || roomService.getRoomByName(roomName).isEmpty()) {
            return "Movie and room does not exist";
        } else {
            screeningService.deleteScreening(movieTitle, roomName, date);
            return "Screening of " + movieTitle + " in room " + roomName + " is deleted";
        }
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
