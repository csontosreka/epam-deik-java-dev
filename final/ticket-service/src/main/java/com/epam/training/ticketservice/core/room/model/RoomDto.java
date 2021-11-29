package com.epam.training.ticketservice.core.room.model;

import java.util.Objects;

public class RoomDto {

    private final String name;
    private final Integer seatRows;
    private final Integer seatCols;

    public RoomDto(String name, Integer seatRows, Integer seatCols) {
        this.name = name;
        this.seatRows = seatRows;
        this.seatCols = seatCols;
    }

    public static RoomDto.Builder builder() {
        return new RoomDto.Builder();
    }

    public String getName() {
        return name;
    }

    public Integer getSeatRows() {
        return seatRows;
    }

    public Integer getSeatCols() {
        return seatCols;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomDto roomDto = (RoomDto) o;
        return Objects.equals(name, roomDto.name)
                && Objects.equals(seatRows, roomDto.seatRows)
                && Objects.equals(seatCols, roomDto.seatCols);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, seatRows, seatCols);
    }

    @Override
    public String toString() {
        return "Room " + name + " with " + seatRows * seatCols
                + " seats, " + seatRows + " rows and " + seatCols + " columns";
    }

    public static class Builder {
        private String name;
        private Integer seatRows;
        private Integer seatCols;

        public RoomDto.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public RoomDto.Builder withSeatRows(Integer seatRows) {
            this.seatRows = seatRows;
            return this;
        }

        public RoomDto.Builder withSeatCols(Integer seatCols) {
            this.seatCols = seatCols;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(name, seatRows, seatCols);
        }
    }
}
