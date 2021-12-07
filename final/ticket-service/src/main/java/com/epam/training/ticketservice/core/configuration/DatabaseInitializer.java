package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.movie.pesistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.pesistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import com.epam.training.ticketservice.core.user.persistance.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatabaseInitializer {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    public DatabaseInitializer(MovieRepository movieRepository, UserRepository userRepository,
                               RoomRepository roomRepository, ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;
    }

    @PostConstruct
    public void init() {
        //Movie spiritedAway = new Movie("Spirited Away", "animation", 125);
        //Movie hungerGames = new Movie("Hunger Games", "action", 130);
        //movieRepository.saveAll(List.of(spiritedAway, hungerGames));

        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);

        //Room r1Room = new Room("R1", 10, 10);
        //Room r2Room = new Room("R2", 15, 10);
        //roomRepository.saveAll(List.of(r1Room, r2Room));

        //Screening sc1 = new Screening("Hunger Games", "R1",
        //        LocalDateTime.of(2021, 11, 25, 10, 0));
        //Screening sc2 = new Screening("Spirited Away", "R1",
        //        LocalDateTime.of(2021, 12, 5, 12, 0));
        //screeningRepository.saveAll(List.of(sc1, sc2));
    }

}
