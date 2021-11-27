package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class MovieCommands {

    private final MovieService movieService;
    private final UserService userService;

    public MovieCommands(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @ShellMethod(key = "list movies", value = "List all available movies")
    public Object listAvailableMovies() {
        if (movieService.getMovieList().isEmpty()) {
            return "There are no movies at the moment";
        }
        else {
            return movieService.getMovieList();
        }
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create movie", value = "Create new movie")
    public MovieDto createMovie(String title, String genre, Integer length) {
        MovieDto movie = MovieDto.builder()
                .withTitle(title)
                .withGenre(genre)
                .withMovieLength(length)
                .build();
        movieService.createMovie(movie);
        return movie;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update movie", value = "Updates existing movie")
    public MovieDto updateMovie(String title, String genre, Integer length) {
        MovieDto movie = MovieDto.builder()
                .withTitle(title)
                .withGenre(genre)
                .withMovieLength(length)
                .build();
        movieService.updateMovie(movie);
        return movie;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete movie", value = "Deletes movie")
    public String deleteMovie(String title) {
        if (movieService.getMovieByName(title).isEmpty()) {
            return "Movie does not exist";
        } else {
            movieService.deleteMovie(title);
            return title + " is deleted";
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
