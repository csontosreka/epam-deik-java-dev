package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.pesistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.pesistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import com.epam.training.ticketservice.core.user.persistance.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DatabaseInitializer {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public DatabaseInitializer(MovieRepository movieRepository, UserRepository userRepository,
                               RoomRepository roomRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    @PostConstruct
    public void init() {
        Movie spiritedAway = new Movie("Spirited Away", "animation", 125);
        Movie hungerGames = new Movie("Hunger Games", "action", 130);
        movieRepository.saveAll(List.of(spiritedAway, hungerGames));

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);

        Room r1Room = new Room("R1", 10, 10);
        Room r2Room = new Room("R2", 15, 10);
        roomRepository.saveAll(List.of(r1Room, r2Room));

    }

}
