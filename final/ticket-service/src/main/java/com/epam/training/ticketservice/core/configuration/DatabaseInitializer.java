package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.pesistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.pesistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import com.epam.training.ticketservice.core.user.persistance.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DatabaseInitializer {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public DatabaseInitializer(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Movie spiritedAway = new Movie("Spirited Away", "animation", 125);
        Movie hungerGames = new Movie("Hunger Games", "action", 130);
        movieRepository.saveAll(List.of(spiritedAway, hungerGames));

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);
    }

}
