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
        String expectedUsername = "testuser";
        user.setUsername(expectedUsername);
        String actualUsername = user.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testSetAndGetPassword() {
        String expectedPassword = "wachtwoord";
        user.setPassword(expectedPassword);
        String actualPassword = user.getPassword();
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testSetAndGetEmail() {
        String expectedEmail = "testuser@email.com";
        user.setEmail(expectedEmail);
        String actualEmail = user.getEmail();
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    public void testSetAndGetFirstName() {
        String expectedFirstName = "firstnametest";
        user.setFirstName(expectedFirstName);
        String actualFirstName = user.getFirstName();
        assertEquals(expectedFirstName, actualFirstName);
    }

    @Test
    public void testSetAndGetLastName() {
        String expectedLastName = "lastnametest";
        user.setLastName(expectedLastName);
        String actualLastName = user.getLastName();
        assertEquals(expectedLastName, actualLastName);
    }

    @Test
    public void testSetAndGetDateOfBirth() {
        LocalDate expectedDateOfBirth = LocalDate.of(1980, 11, 6);
        user.setDateOfBirth(expectedDateOfBirth);
        LocalDate actualDateOfBirth = user.getDateOfBirth();
        assertEquals(expectedDateOfBirth, actualDateOfBirth);
    }

    @Test
    public void testSetAndGetAddress() {
        String expectedAddress = "22 teststraat";
        user.setAddress(expectedAddress);
        String actualAddress = user.getAddress();
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void testSetAndGetPhoneNo() {
        String expectedPhoneNo = "06-12345678";
        user.setPhoneNo(expectedPhoneNo);
        String actualPhoneNo = user.getPhoneNo();
        assertEquals(expectedPhoneNo, actualPhoneNo);
    }

    @Test
    public void testSetAndIsEnabledAccount() {
        boolean expectedEnabledAccount = true;
        user.setEnabledAccount(expectedEnabledAccount);
        boolean actualEnabledAccount = user.isEnabledAccount();
        assertEquals(expectedEnabledAccount, actualEnabledAccount);
    }

    @Test
    public void testSetAndGetUserCreatedOn() {
        LocalDate expectedUserCreatedOn = LocalDate.now();
        user.setUserCreatedOn(expectedUserCreatedOn);
        LocalDate actualUserCreatedOn = user.getUserCreatedOn();
        assertEquals(expectedUserCreatedOn, actualUserCreatedOn);
    }

    @Test
    public void testGetAndSetAuthorities() {
        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_CUSTOMER);
        Set<Authority> expectedAuthorities = new HashSet<>();
        expectedAuthorities.add(authority);
        user.setAuthorities(expectedAuthorities);
        Set<Authority> actualAuthorities = user.getAuthorities();
        assertEquals(expectedAuthorities, actualAuthorities);
    }


    @Test
    public void testAddAuthority() {
        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_DESIGNER);
        user.addAuthority(authority);
        assertTrue(user.getAuthorities().contains(authority));
    }

    @Test
    public void testRemoveAuthority() {
        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_DESIGNER);
        user.addAuthority(authority);
        user.removeAuthority(authority);
        assertFalse(user.getAuthorities().contains(authority));
    }

    @Test
    public void testEnableAccount() {
        user.enableAccount();
        assertTrue(user.isEnabledAccount());
    }

    @Test
    public void testDisableAccount() {
        user.disableAccount();
        assertFalse(user.isEnabledAccount());
    }

    @Test
    public void testUpdateInformation() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("updated@email.com");
        updateUserRequest.setFirstName("Anne");
        updateUserRequest.setLastName("Achternaam");
        updateUserRequest.setDateOfBirth(LocalDate.of(1985, 7, 7));
        updateUserRequest.setAddress("44 nieuwstraat");
        updateUserRequest.setPhoneNo("06-87654321");
        user.updateInformation(updateUserRequest);
        assertEquals("updated@email.com", user.getEmail());
        assertEquals("Anne", user.getFirstName());
        assertEquals("Achternaam", user.getLastName());
        assertEquals(LocalDate.of(1985, 7, 7), user.getDateOfBirth());
        assertEquals("44 nieuwstraat", user.getAddress());
        assertEquals("06-87654321", user.getPhoneNo());
    }

    @Test
    public void testUpdatePassword() {
        String newPassword = "nieuwwachtwoord";
        user.updatePassword(newPassword);
        assertEquals(newPassword, user.getPassword());
    }
}
