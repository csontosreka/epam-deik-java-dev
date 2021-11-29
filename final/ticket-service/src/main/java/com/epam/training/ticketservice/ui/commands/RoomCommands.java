package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class RoomCommands {

    private final RoomService roomService;
    private final UserService userService;

    public RoomCommands(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethod(key = "list rooms", value = "List all available rooms")
    public Object listAvailableRooms() {
        if (roomService.getRoomList().isEmpty()) {
            return "There are no rooms at the moment";
        } else {
            return roomService.getRoomList();
        }
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create room", value = "Create new room")
    public RoomDto createRoom(String name, Integer seatRows, Integer seatCols) {
        RoomDto room = RoomDto.builder()
                .withName(name)
                .withSeatRows(seatRows)
                .withSeatCols(seatCols)
                .build();
        roomService.createRoom(room);
        return room;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update room", value = "Updates existing room")
    public RoomDto updateRoom(String name, Integer seatRows, Integer seatCols) {
        RoomDto room = RoomDto.builder()
                .withName(name)
                .withSeatRows(seatRows)
                .withSeatCols(seatCols)
                .build();
        roomService.updateRoom(room);
        return room;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete room", value = "Deletes room")
    public String deleteRoom(String name) {
        if (roomService.getRoomByName(name).isEmpty()) {
            return "Room does not exist";
        } else {
            roomService.deleteRoom(name);
            return name + " is deleted";
        }
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.getLoggedInUser();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
