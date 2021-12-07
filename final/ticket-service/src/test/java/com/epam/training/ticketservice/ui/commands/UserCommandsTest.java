package com.epam.training.ticketservice.ui.commands;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCommandsTest {

    private final UserService userService = mock(UserService.class);

    private UserCommands underTest = new UserCommands(userService);
    UserDto userDto = new UserDto("admin", User.Role.ADMIN);

    @Test
    public void testSignInShouldReturnErrorMessage() {
        UserDto userDto = new UserDto("admin", User.Role.ADMIN);

        String expected = "Login failed due to incorrect credentials";
        String actual = underTest.login("dummy", "dummy");

        assertEquals(expected, actual);
    }

    @Test
    public void testSignOutShouldReturnErrorMessage() {

        String expected = "You are not signed in";
        String actual = underTest.logout();

        assertEquals(expected, actual);
    }

    @Test
    public void testDescribeAccountShouldReturnErrorMessage() {

        String expected = "You are not signed in";
        String actual = underTest.printLoggedInUser();

        assertEquals(expected, actual);
    }



}