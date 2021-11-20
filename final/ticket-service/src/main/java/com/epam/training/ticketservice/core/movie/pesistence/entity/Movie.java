package com.epam.training.ticketservice.core.movie.pesistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String title;
    private String genre;
    private Integer movieLength;

    public Movie() {
    }

    public Movie(String title, String genre, Integer movieLength) {
        this.title = title;
        this.genre = genre;
        this.movieLength = movieLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(Integer movieLength) {
        this.movieLength = movieLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) && Objects.equals(title, movie.title)
                && Objects.equals(genre, movie.genre)
                && Objects.equals(movieLength, movie.movieLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre, movieLength);
    }

    @Override
    public String toString() {
        return "Movie{"
                + "Id=" + id
                + ", title='" + title + '\''
                + ", genre='" + genre + '\''
                + ", movieLength=" + movieLength
                + '}';
    }
}
