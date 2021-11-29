package com.epam.training.ticketservice.core.screening.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String movieTitle;
    private String roomName;
    private LocalDateTime screeningDate;

    public Screening() {
    }

    public Screening(String movieTitle, String roomName, LocalDateTime screeningDate) {
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.screeningDate = screeningDate;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public LocalDateTime getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(LocalDateTime screeningDate) {
        this.screeningDate = screeningDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Screening screening = (Screening) o;
        return Objects.equals(id, screening.id) && Objects.equals(movieTitle, screening.movieTitle)
                && Objects.equals(roomName, screening.roomName)
                && Objects.equals(screeningDate, screening.screeningDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieTitle, roomName, screeningDate);
    }

    @Override
    public String toString() {
        return "Screening{" + "id=" + id + ", movieTitle='" + movieTitle + '\''
                + ", roomName='" + roomName + '\''
                + ", screeningDate=" + screeningDate + '}';
    }
}
