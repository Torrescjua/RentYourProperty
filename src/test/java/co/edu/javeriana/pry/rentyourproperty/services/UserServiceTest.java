package co.edu.javeriana.pry.rentyourproperty.services;

import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Role;
import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AccountActivationService accountActivationService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for get() method - Retrieve all users
    @Test
    void testGetAllUsers_Success() {
        List<User> mockUsers = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(mockUsers);
        when(modelMapper.map(any(User.class), eq(UserDTO.class))).thenReturn(new UserDTO());

        List<UserDTO> result = userService.get();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    // Test for get(Long id) method - Retrieve a user by ID
    @Test
    void testGetUserById_Success() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserDTO.class)).thenReturn(new UserDTO());

        UserDTO result = userService.get(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    // Test for get(Long id) method - User not found
    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.get(1L));
    }

    // Test for saveNew(UserDTO userDTO) method - Create a new user
    @Test
    void testSaveNewUser_Success() {
        UserDTO mockUserDTO = new UserDTO();
        User mockUser = new User();
        
        when(modelMapper.map(mockUserDTO, User.class)).thenReturn(mockUser);
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        when(modelMapper.map(mockUser, UserDTO.class)).thenReturn(mockUserDTO);

        UserDTO result = userService.saveNew(mockUserDTO);

        assertNotNull(result);
        verify(accountActivationService, times(1)).sendActivationEmail(mockUser);
        verify(userRepository, times(1)).save(mockUser);
    }
    // Test for update(Long id, UserDTO userDTO) method
    @Test
    void testUpdateUser_Success() {
        // Create mock user and userDTO
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Old Name"); // Existing user details

        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setName("New Name"); // New values to update the user

        // Simulating the existing user found in the repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Simulate the repository saving the updated user
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Mock the ModelMapper's behavior to return a new UserDTO after mapping
        UserDTO mappedUserDTO = new UserDTO();
        mappedUserDTO.setName("New Name"); // Expected mapped result
        when(modelMapper.map(mockUser, UserDTO.class)).thenReturn(mappedUserDTO);

        // Call the service method
        UserDTO result = userService.update(1L, mockUserDTO);

        // Assertions
        assertNotNull(result); // Ensure that the result is not null
        assertEquals("New Name", result.getName()); // Ensure the name was updated correctly
        
        // Verifying interactions with the repository
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(mockUser);
        verify(modelMapper, times(1)).map(mockUser, UserDTO.class);
    }



    // Test for delete(Long id) method - Soft delete user (set status to INACTIVE)
    @Test
    void testDeleteUser_Success() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setStatus(Status.ACTIVE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        userService.delete(1L);

        assertEquals(Status.INACTIVE, mockUser.getStatus());
        verify(userRepository, times(1)).save(mockUser);
    }

    // Test for logIn(String email, String password) method - Successful login
    @Test
    void testLogIn_Success() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserDTO.class)).thenReturn(new UserDTO());

        UserDTO result = userService.logIn("test@example.com", "password");

        assertNotNull(result);
    }

    // Test for logIn(String email, String password) method - Invalid credentials
    @Test
    void testLogIn_InvalidCredentials() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        assertThrows(IllegalStateException.class, () -> userService.logIn("test@example.com", "wrongpassword"));
    }

    // Test for isUserLandlord(Long userId) method
    @Test
    void testIsUserLandlord_Success() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.ARRENDADOR);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        boolean isLandlord = userService.isUserLandlord(1L);

        assertTrue(isLandlord);
    }

    // Test for isUserActive(Long userId) method
    @Test
    void testIsUserActive_Success() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setStatus(Status.ACTIVE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        boolean isActive = userService.isUserActive(1L);

        assertTrue(isActive);
    }
}
