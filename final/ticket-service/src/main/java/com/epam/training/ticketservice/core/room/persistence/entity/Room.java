package com.epam.training.ticketservice.core.room.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String name;
    private Integer seatRows;
    private Integer seatCols;

    public Room() {
    }

    public Room(String name, Integer seatRows, Integer seatCols) {
        this.name = name;
        this.seatRows = seatRows;
        this.seatCols = seatCols;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeatRows() {
        return seatRows;
    }

    public void setSeatRows(Integer seatRows) {
        this.seatRows = seatRows;
    }

    public Integer getSeatCols() {
        return seatCols;
    }

    public void setSeatCols(Integer seatCols) {
        this.seatCols = seatCols;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id) && Objects.equals(name, room.name)
                && Objects.equals(seatRows, room.seatRows) && Objects.equals(seatCols, room.seatCols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seatRows, seatCols);
    }

    @Override
    public String toString() {
        return "Room{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", seatRows=" + seatRows
                + ", seatCols=" + seatCols + '}';
    }
}

