package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;
    private final RoomService roomService;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository,
                                MovieService movieService,
                                RoomService roomService) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @Override
    public List<ScreeningDto> getScreeningList() {
        return screeningRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private List<ScreeningDto> getScreeningsByRoomName(String room) {
        return screeningRepository.findByRoomName(room)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Screening> createScreening(ScreeningDto screeningDto) {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovie().getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(screeningDto.getRoom().getName(), "Room name cannot be null");
        Objects.requireNonNull(screeningDto.getScreeningDate(), "Screening date cannot be null");

        List<ScreeningDto> screeningsInGivenRoom = getScreeningsByRoomName(screeningDto.getRoom().getName());

        for (var screening : screeningsInGivenRoom) {
            overlapChecker(screening, screeningDto);
        }

        Screening newScreening = new Screening(
                screeningDto.getMovie().getTitle(),
                screeningDto.getRoom().getName(),
                screeningDto.getScreeningDate());
        screeningRepository.save(newScreening);
        return Optional.of(newScreening);
    }

    private void overlapChecker(ScreeningDto screening, ScreeningDto givenScreeningDto) {
        LocalDateTime screeningStartDate = screening.getScreeningDate();
        Integer screeningLength = screening.getMovie().getMovieLength();
        Integer givenScreeningLength = givenScreeningDto.getMovie().getMovieLength();
        LocalDateTime givenScreeningDate = givenScreeningDto.getScreeningDate();

        if (screeningStartDate.isAfter(givenScreeningDate.minusMinutes(givenScreeningLength + 10))
                && (givenScreeningDate.isBefore(screeningStartDate.plusMinutes(screeningLength))
                || givenScreeningDate.isEqual(screeningStartDate.plusMinutes(screeningLength)))) {
            throw new IllegalArgumentException("There is an overlapping screening");

        } else if (givenScreeningDate.isAfter(screeningStartDate.plusMinutes(screeningLength))
                && (givenScreeningDate.isBefore(screeningStartDate.plusMinutes(screeningLength + 10))
                || givenScreeningDate.isEqual(screeningStartDate.plusMinutes(screeningLength + 10)))) {
            throw new IllegalArgumentException(
                    "This would start in the break period after another screening in this room");
        }
    }

    @Override
    public void deleteScreening(String movie, String room, LocalDateTime screeningDate) {
        screeningRepository.deleteByMovieTitleAndRoomNameAndScreeningDate(movie, room, screeningDate);
    }

    private ScreeningDto convertEntityToDto(Screening screening) {

        Optional<MovieDto> movieDto = movieService.getMovieByName(screening.getMovieTitle());
        Optional<RoomDto> roomDto = roomService.getRoomByName(screening.getRoomName());

        if (movieDto.isPresent() && roomDto.isPresent()) {
            return ScreeningDto.builder()
                    .withMovie(movieDto.get())
                    .withRoom(roomDto.get())
                    .withScreeningDate(screening.getScreeningDate())
                    .build();
        } else {
            throw new NullPointerException("Movie or room does not exist");
        }

    }

    private Optional<ScreeningDto> convertEntityToDto(Optional<Screening> screening) {
        return screening.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(screening.get()));
    }
}
