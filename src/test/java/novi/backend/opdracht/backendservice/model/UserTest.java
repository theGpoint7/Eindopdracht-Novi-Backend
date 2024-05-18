package novi.backend.opdracht.backendservice.model;

import novi.backend.opdracht.backendservice.dto.input.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testSetAndGetUsername() {
        // Arrange
        String expectedUsername = "testuser";

        // Act
        user.setUsername(expectedUsername);
        String actualUsername = user.getUsername();

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testSetAndGetPassword() {
        // Arrange
        String expectedPassword = "wachtwoord";

        // Act
        user.setPassword(expectedPassword);
        String actualPassword = user.getPassword();

        // Assert
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testSetAndGetEmail() {
        // Arrange
        String expectedEmail = "testuser@email.com";

        // Act
        user.setEmail(expectedEmail);
        String actualEmail = user.getEmail();

        // Assert
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    public void testSetAndGetFirstName() {
        // Arrange
        String expectedFirstName = "firstnametest";

        // Act
        user.setFirstName(expectedFirstName);
        String actualFirstName = user.getFirstName();

        // Assert
        assertEquals(expectedFirstName, actualFirstName);
    }

    @Test
    public void testSetAndGetLastName() {
        // Arrange
        String expectedLastName = "lastnametest";

        // Act
        user.setLastName(expectedLastName);
        String actualLastName = user.getLastName();

        // Assert
        assertEquals(expectedLastName, actualLastName);
    }

    @Test
    public void testSetAndGetDateOfBirth() {
        // Arrange
        LocalDate expectedDateOfBirth = LocalDate.of(1980, 11, 6);

        // Act
        user.setDateOfBirth(expectedDateOfBirth);
        LocalDate actualDateOfBirth = user.getDateOfBirth();

        // Assert
        assertEquals(expectedDateOfBirth, actualDateOfBirth);
    }

    @Test
    public void testSetAndGetAddress() {
        // Arrange
        String expectedAddress = "22 teststraat";

        // Act
        user.setAddress(expectedAddress);
        String actualAddress = user.getAddress();

        // Assert
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void testSetAndGetPhoneNo() {
        // Arrange
        String expectedPhoneNo = "06-12345678";

        // Act
        user.setPhoneNo(expectedPhoneNo);
        String actualPhoneNo = user.getPhoneNo();

        // Assert
        assertEquals(expectedPhoneNo, actualPhoneNo);
    }

    @Test
    public void testSetAndIsEnabledAccount() {
        // Arrange
        boolean expectedEnabledAccount = true;

        // Act
        user.setEnabledAccount(expectedEnabledAccount);
        boolean actualEnabledAccount = user.isEnabledAccount();

        // Assert
        assertEquals(expectedEnabledAccount, actualEnabledAccount);
    }

    @Test
    public void testSetAndGetUserCreatedOn() {
        // Arrange
        LocalDate expectedUserCreatedOn = LocalDate.now();

        // Act
        user.setUserCreatedOn(expectedUserCreatedOn);
        LocalDate actualUserCreatedOn = user.getUserCreatedOn();

        // Assert
        assertEquals(expectedUserCreatedOn, actualUserCreatedOn);
    }

    @Test
    public void testGetAndSetAuthorities() {
        // Arrange
        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_CUSTOMER);
        Set<Authority> expectedAuthorities = new HashSet<>();
        expectedAuthorities.add(authority);

        // Act
        user.setAuthorities(expectedAuthorities);
        Set<Authority> actualAuthorities = user.getAuthorities();

        // Assert
        assertEquals(expectedAuthorities, actualAuthorities);
    }


    @Test
    public void testAddAuthority() {
        // Arrange
        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_DESIGNER);

        // Act
        user.addAuthority(authority);

        // Assert
        assertTrue(user.getAuthorities().contains(authority));
    }

    @Test
    public void testRemoveAuthority() {
        // Arrange
        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_DESIGNER);
        user.addAuthority(authority);

        // Act
        user.removeAuthority(authority);

        // Assert
        assertFalse(user.getAuthorities().contains(authority));
    }

    @Test
    public void testEnableAccount() {
        // Act
        user.enableAccount();

        // Assert
        assertTrue(user.isEnabledAccount());
    }

    @Test
    public void testDisableAccount() {
        // Act
        user.disableAccount();

        // Assert
        assertFalse(user.isEnabledAccount());
    }

    @Test
    public void testUpdateInformation() {
        // Arrange
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("updated@email.com");
        updateUserRequest.setFirstName("Anne");
        updateUserRequest.setLastName("Achternaam");
        updateUserRequest.setDateOfBirth(LocalDate.of(1985, 7, 7));
        updateUserRequest.setAddress("44 nieuwstraat");
        updateUserRequest.setPhoneNo("06-87654321");

        // Act
        user.updateInformation(updateUserRequest);

        // Assert
        assertEquals("updated@email.com", user.getEmail());
        assertEquals("Anne", user.getFirstName());
        assertEquals("Achternaam", user.getLastName());
        assertEquals(LocalDate.of(1985, 7, 7), user.getDateOfBirth());
        assertEquals("44 nieuwstraat", user.getAddress());
        assertEquals("06-87654321", user.getPhoneNo());
    }

    @Test
    public void testUpdatePassword() {
        // Arrange
        String newPassword = "nieuwwachtwoord";

        // Act
        user.updatePassword(newPassword);

        // Assert
        assertEquals(newPassword, user.getPassword());
    }
}
