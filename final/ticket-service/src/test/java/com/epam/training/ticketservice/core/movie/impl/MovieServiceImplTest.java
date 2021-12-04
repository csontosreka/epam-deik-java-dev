package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.pesistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.pesistence.repository.MovieRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    private static final Movie HG_ENTITY = new Movie("Hunger Games", "action", 90);
    private static final Movie SA_ENTITY = new Movie("Spirited Away", "animation", 125);
    private static final MovieDto HG_DTO = MovieDto.builder()
            .withTitle(HG_ENTITY.getTitle())
            .withGenre(HG_ENTITY.getGenre())
            .withMovieLength(HG_ENTITY.getMovieLength())
            .build();
    private static final MovieDto SA_DTO = MovieDto.builder()
            .withTitle(SA_ENTITY.getTitle())
            .withGenre(SA_ENTITY.getGenre())
            .withMovieLength(SA_ENTITY.getMovieLength())
            .build();
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final MovieServiceImpl underTest = new MovieServiceImpl(movieRepository);

    @Test
    public void testGetMovieListShouldCallMovieRepositoryAndReturnADtoList() {
        // Given
        when(movieRepository.findAll()).thenReturn(List.of(HG_ENTITY, SA_ENTITY));
        List<MovieDto> expected = List.of(HG_DTO, SA_DTO);

        // When
        List<MovieDto> actual = underTest.getMovieList();

        // Then
        assertEquals(expected, actual);
        verify(movieRepository).findAll();
    }

    @Test
    public void testGetMovieByTitleShouldReturnAHungerGamesDtoWhenTheMovieExists() {
        // Given
        when(movieRepository.findByTitle("Hunger Games")).thenReturn(Optional.of(HG_ENTITY));
        Optional<MovieDto> expected = Optional.of(HG_DTO);

        // When
        Optional<MovieDto> actual = underTest.getMovieByName("Hunger Games");

        // Then
        assertEquals(expected, actual);
        verify(movieRepository).findByTitle("Hunger Games");
    }

    @Test
    public void testGetMovieByTitleShouldReturnOptionalEmptyWhenInputMovieTitleDoesNotExist() {
        // Given
        when(movieRepository.findByTitle("dummy")).thenReturn(Optional.empty());
        Optional<MovieDto> expected = Optional.empty();

        // When
        Optional<MovieDto> actual = underTest.getMovieByName("dummy");

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(movieRepository).findByTitle("dummy");
    }

    @Test
    public void testGetMovieByTitleShouldReturnOptionalEmptyWhenInputMovieTitleIsNull() {
        // Given
        when(movieRepository.findByTitle(null)).thenReturn(Optional.empty());
        Optional<MovieDto> expected = Optional.empty();

        // When
        Optional<MovieDto> actual = underTest.getMovieByName(null);

        // Then
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
        verify(movieRepository).findByTitle(null);
    }

    @Test
    public void testCreateMovieShouldCallMovieRepositoryWhenTheInputIsValid() {
        // Given
        when(movieRepository.save(SA_ENTITY)).thenReturn(SA_ENTITY);

        // When
        underTest.createMovie(SA_DTO);

        // Then
        verify(movieRepository).save(SA_ENTITY);
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(null));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieNameIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle(null)
                .withGenre("action")
                .withMovieLength(90)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movie));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenGenreIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle("Harry Potter")
                .withGenre(null)
                .withMovieLength(110)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movie));
    }

    @Test
    public void testCreateMovieShouldThrowNullPointerExceptionWhenMovieLengthIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle("Harry Potter")
                .withGenre("fantasy")
                .withMovieLength(null)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.createMovie(movie));
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(null));
    }

    @Test
    public void testUpdateMovieShouldThrowNullPointerExceptionWhenMovieNameIsNull() {
        // Given
        MovieDto movie = new MovieDto.Builder()
                .withTitle(null)
                .withGenre("action")
                .withMovieLength(90)
                .build();

        // When - Then
        assertThrows(NullPointerException.class, () -> underTest.updateMovie(movie));
    }

    @Test
    public void testDeleteMovieShouldThrowNullPointerExceptionWhenMovieIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.deleteMovie(null));
    }


}