package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistance.entity.User;
import com.epam.training.ticketservice.core.user.persistance.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private UserService underTest = new UserServiceImpl(userRepository);

    @Test
    public void testSignInShouldThrowNullPointerExceptionWhenUsernameIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.login(null, "pass"));
    }

    @Test
    public void testSignInShouldThrowNullPointerExceptionWhenPasswordIsNull() {
        // Given - When - Then
        assertThrows(NullPointerException.class, () -> underTest.login("user", null));
    }

    @Test
    public void testSignInShouldSetSignedInAdminWhenUsernameAndPasswordAreCorrect() {
        // Given
        User user = new User("admin", "admin", User.Role.ADMIN);
        Optional<User> expected = Optional.of(user);
        when(userRepository.findByUsernameAndPassword("admin", "admin")).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> actual = underTest.login("admin", "admin");

        // Then
        assertEquals(expected.get().getUsername(), actual.get().getUsername());
        assertEquals(expected.get().getRole(), actual.get().getRole());
        verify(userRepository).findByUsernameAndPassword("admin", "admin");
    }

    @Test
    public void testSigninShouldReturnOptionalEmptyWhenUsernameOrPasswordAreNotCorrect() {
        // Given
        Optional<UserDto> expected = Optional.empty();
        when(userRepository.findByUsernameAndPassword("dummy", "dummy")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> actual = underTest.login("dummy", "dummy");

        // Then
        assertEquals(expected, actual);
        verify(userRepository).findByUsernameAndPassword("dummy", "dummy");
    }

    @Test
    public void testSignOutShouldReturnOptionalEmptyWhenThereIsNoOneSignedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.logout();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testSignOutShouldReturnThePreviouslySignedInAdminWhenThereIsASignedInAdmin() {
        // Given
        UserDto user = new UserDto("admin", User.Role.ADMIN);
        Optional<UserDto> expected = Optional.of(user);
        underTest = new UserServiceImpl(user, userRepository);

        // When
        Optional<UserDto> actual = underTest.logout();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSignedInAdminShouldReturnTheSignedInAdminWhenThereIsASignedInAdmin() {
        // Given
        UserDto user = new UserDto("admin", User.Role.ADMIN);
        Optional<UserDto> expected = Optional.of(user);
        underTest = new UserServiceImpl(user, userRepository);

        // When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLoggedInUserShouldReturnOptionalEmptyWhenThereIsNoOneLoggedIn() {
        // Given
        Optional<UserDto> expected = Optional.empty();

        // When
        Optional<UserDto> actual = underTest.getLoggedInUser();

        // Then
        assertEquals(expected, actual);
    }

    // TODO RegisterTests

}