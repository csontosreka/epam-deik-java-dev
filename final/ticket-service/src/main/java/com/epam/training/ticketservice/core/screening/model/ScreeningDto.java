package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ScreeningDto {

    private final MovieDto movie;
    private final RoomDto room;
    private final LocalDateTime screeningDate;

    public ScreeningDto(MovieDto movie, RoomDto room, LocalDateTime screeningDate) {
        this.movie = movie;
        this.room = room;
        this.screeningDate = screeningDate;
    }

    public static ScreeningDto.Builder builder() {
        return new ScreeningDto.Builder();
    }

    public MovieDto getMovie() {
        return movie;
    }

    public RoomDto getRoom() {
        return room;
    }

    public LocalDateTime getScreeningDate() {
        return screeningDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScreeningDto that = (ScreeningDto) o;
        return Objects.equals(movie, that.movie) && Objects.equals(room, that.room)
                && Objects.equals(screeningDate, that.screeningDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, room, screeningDate);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String screeningTime = screeningDate.format(dateFormat);
        return movie.toString() + ", screened in room " + room.getName() + ", at " + screeningTime;
    }

    public static class Builder {
        private MovieDto movie;
        private RoomDto room;
        private LocalDateTime screeningDate;

        public ScreeningDto.Builder withMovie(MovieDto movie) {
            this.movie = movie;
            return this;
        }

        public ScreeningDto.Builder withRoom(RoomDto room) {
            this.room = room;
            return this;
        }

        public ScreeningDto.Builder withScreeningDate(LocalDateTime screeningDate) {
            this.screeningDate = screeningDate;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movie, room, screeningDate);
        }
    }
}
