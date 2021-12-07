package com.epam.training.ticketservice.core.movie.model;

import java.util.Objects;

public class MovieDto {

    private final String title;
    private final String genre;
    private final Integer movieLength;

    public MovieDto(String title, String genre, Integer movieLength) {
        this.title = title;
        this.genre = genre;
        this.movieLength = movieLength;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getMovieLength() {
        return movieLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(title, movieDto.title) && Objects.equals(genre, movieDto.genre)
                && Objects.equals(movieLength, movieDto.movieLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, movieLength);
    }

    @Override
    public String toString() {
        return title + " (" + genre + ", " + movieLength + " minutes)";
    }

    public static class Builder {
        private String title;
        private String genre;
        private Integer movieLength;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withMovieLength(Integer movieLength) {
            this.movieLength = movieLength;
            return this;
        }

        public MovieDto build() {
            return new MovieDto(title, genre, movieLength);
        }
    }
}
