package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.pesistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.pesistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ScreeningServiceImplTest {

    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final MovieService movieService = mock(MovieService.class);
    private final RoomService roomService = mock(RoomService.class);
    private final ScreeningServiceImpl underTest = new ScreeningServiceImpl(screeningRepository,
            movieService, roomService);

    private final static Movie movie = new Movie("IT", "horror", 100);
    private final static MovieDto movieDto = new MovieDto.Builder()
            .withTitle(movie.getTitle())
            .withGenre(movie.getGenre())
            .withMovieLength(movie.getMovieLength())
            .build();
    private final static Room room = new Room("R1", 10, 10);
    private final static RoomDto roomDto = new RoomDto.Builder()
            .withName(room.getName())
            .withSeatRows(room.getSeatRows())
            .withSeatCols(room.getSeatCols())
            .build();
    private final static Screening savedScreening = new Screening(movieDto.getTitle(), roomDto.getName(),
            LocalDateTime.of(2021, 11, 25, 10, 0));
    private final static ScreeningDto savedScreeningDto = new ScreeningDto.Builder()
            .withMovie(movieDto)
            .withRoom(roomDto)
            .withScreeningDate(savedScreening.getScreeningDate())
            .build();

    @Test
    public void testListScreeningsShouldReturnScreenings() {
        //When
        underTest.getScreeningList();

        //Then
        verify(screeningRepository).findAll();
    }

    @Test
    void testCreateScreeningShouldThrowNullPointerExceptionWhenMovieDoesNotExists() {
        // Given
        when(movieRepository.findByTitle("dummy")).thenReturn(Optional.empty());
        when(roomRepository.findByName("R1")).thenReturn(Optional.of(room));

        ScreeningDto screeningDto = new ScreeningDto.Builder()
                .withMovie(null)
                .withRoom(roomDto)
                .withScreeningDate(LocalDateTime.of(2021, 12, 3, 10, 1))
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));
    }

    @Test
    void testCreateScreeningShouldThrowNullPointerExceptionWhenRoomDoesNotExists() {
        // Given
        when(movieRepository.findByTitle("IT")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("dummy")).thenReturn(Optional.empty());

        ScreeningDto screeningDto = new ScreeningDto.Builder()
                .withMovie(movieDto)
                .withRoom(null)
                .withScreeningDate(LocalDateTime.of(2021, 12, 3, 10, 1))
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));
    }

    @Test
    void testCreateScreeningShouldThrowNullPointerExceptionWhenScreeningDateDoesNotExists() {
        // Given
        ScreeningDto screeningDto = new ScreeningDto.Builder()
                .withMovie(movieDto)
                .withRoom(roomDto)
                .withScreeningDate(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));
    }


    @Test
    void testCreateScreeningShouldReturnOptionalScreening() {
        // Given
        ScreeningDto screeningDto = new ScreeningDto(movieDto, roomDto,
                LocalDateTime.of(2021,12, 3,10,0));
        Screening screening = new Screening(movieDto.getTitle(), roomDto.getName(), screeningDto.getScreeningDate());
        Optional<Screening> expected = Optional.of(screening);

        // When
        when(screeningRepository.save(screening)).thenReturn(screening);
        Optional<Screening> actual = underTest.createScreening(screeningDto);

        // Then
        assertEquals(expected, actual);
        verify(screeningRepository).save(screening);
    }

    @Test
    void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenScreeningsOverlap() {
        // Given
        Screening s1 = new Screening("IT", "R1", LocalDateTime.of(
                2021,11,25, 10,0));
        ScreeningDto s1Dto = new ScreeningDto.Builder()
                .withMovie(movieDto)
                .withRoom(roomDto)
                .withScreeningDate(s1.getScreeningDate())
                .build();

        // When
        when(underTest.createScreening(s1Dto)).thenThrow(IllegalArgumentException.class);

        // Then
        assertThrows(IllegalArgumentException.class, () -> underTest.createScreening(s1Dto));
    }


    @Test
    void testCreateScreeningShouldCallOverlapCheckerAndReturnNullPointerExceptionWhenMovieOrRoomNotExist() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(movieDto)
                .withRoom(roomDto)
                .withScreeningDate(LocalDateTime.of(2021, 11, 25, 10, 0))
                .build();
        Screening s1 = new Screening("test", "R1", LocalDateTime.of(2021, 11, 25, 10, 0));

        when(movieService.getMovieByName(movieDto.getTitle())).thenReturn(Optional.of(movieDto));
        when(roomService.getRoomByName(roomDto.getName())).thenReturn(Optional.of(roomDto));
        when(screeningRepository.findByRoomName(roomDto.getName())).thenReturn(List.of(s1));

        // When
        Assertions.assertThrows(NullPointerException.class, () -> underTest.createScreening(screeningDto));

        // Then
        verifyNoMoreInteractions(movieRepository);

    }

    @Test
    void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenOverlapping() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(movieDto)
                .withRoom(roomDto)
                .withScreeningDate(LocalDateTime.of(2021, 11, 25, 10, 0))
                .build();
        Screening s1 = new Screening("IT", "R1", LocalDateTime.of(2021, 11, 25, 10, 0));

        when(movieService.getMovieByName(movieDto.getTitle())).thenReturn(Optional.of(movieDto));
        when(roomService.getRoomByName(roomDto.getName())).thenReturn(Optional.of(roomDto));
        when(screeningRepository.findByRoomName(roomDto.getName())).thenReturn(List.of(s1));

        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.createScreening(screeningDto));

        // Then
        verifyNoMoreInteractions(movieRepository);

    }

    @Test
    void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenOverlappingScreeningTime() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(movieDto)
                .withRoom(roomDto)
                .withScreeningDate(LocalDateTime.of(2021, 11, 25, 12, 30))
                .build();
        Screening s1 = new Screening("IT", "R1", LocalDateTime.of(2021, 11, 25, 11, 41));

        when(movieService.getMovieByName(movieDto.getTitle())).thenReturn(Optional.of(movieDto));
        when(roomService.getRoomByName(roomDto.getName())).thenReturn(Optional.of(roomDto));
        when(screeningRepository.findByRoomName(roomDto.getName())).thenReturn(List.of(s1, savedScreening));

        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.createScreening(screeningDto));

        // Then
        verifyNoMoreInteractions(movieRepository);

    }

    @Test
    void testCreateScreeningShouldThrowIllegalArgumentExceptionWhenOverlappingBreakTime() {
        // Given
        ScreeningDto screeningDto = ScreeningDto.builder()
                .withMovie(movieDto)
                .withRoom(roomDto)
                .withScreeningDate(LocalDateTime.of(2021, 11, 25, 9, 59))
                .build();
        Screening s1 = new Screening("IT", "R1", LocalDateTime.of(2021, 11, 25, 9, 59));

        when(movieService.getMovieByName(movieDto.getTitle())).thenReturn(Optional.of(movieDto));
        when(roomService.getRoomByName(roomDto.getName())).thenReturn(Optional.of(roomDto));
        when(screeningRepository.findByRoomName(roomDto.getName())).thenReturn(List.of(s1, savedScreening));

        // When
        Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.createScreening(screeningDto));

        // Then
        verifyNoMoreInteractions(movieRepository);

    }

    @Test
    void testDeleteShouldCallScreeningRepositoryWhenScreeningExists() {
        //Given

        // When
        underTest.deleteScreening(movieDto.getTitle(), roomDto.getName(), savedScreening.getScreeningDate());
        // Then
        verify(screeningRepository).deleteByMovieTitleAndRoomNameAndScreeningDate(movieDto.getTitle(), roomDto.getName(),
                savedScreening.getScreeningDate());
    }
}