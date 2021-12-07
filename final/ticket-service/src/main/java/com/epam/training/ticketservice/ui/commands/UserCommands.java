package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
public class UserCommands {

    private final UserService userService;

    public UserCommands(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(key = "sign in privileged", value = "Login command for admins.")
    public String login(String username, String password) {
        Optional<UserDto> user = userService.login(username, password);
        if (user.isEmpty()) {
            return "Login failed due to incorrect credentials";
        }
        return "Signed in privileged account '" + user.get().getUsername() +"'";
    }

    @ShellMethod(key = "sign out", value = "User sign out")
    public String logout() {
        Optional<UserDto> user = userService.logout();
        if (user.isEmpty()) {
            return "You are not signed in";
        }
        return user.get() + " signed out";
    }

    @ShellMethod(key = "describe account", value = "Get user information")
    public String printLoggedInUser() {
        Optional<UserDto> userDto = userService.getLoggedInUser();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        return "Signed in privileged " + userDto.get().toString();
    }

}
