package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.RoleRepository;
import com.nusiss.userservice.dao.UserRoleRepository;
import com.nusiss.userservice.entity.Role;
import com.nusiss.userservice.entity.UserRole;
import com.nusiss.userservice.entity.UserRoleId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRoleServiceImplTest {

    @InjectMocks
    private UserRoleService userRoleService;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserRole mockUserRole;
    private Role mockRole;
    private UserRoleId userRoleId;

    /*@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepare mock data for the tests
        userRoleId = new UserRoleId();
        userRoleId.setUserId(1L);
        userRoleId.setRoleId(101L);

        mockUserRole = new UserRole();
        mockUserRole.setId(userRoleId);

        mockRole = new Role();
        mockRole.setId(101L);
        mockRole.setName("Admin");
    }

    @Test
    void testGetRoleByUserId_UserExists() {
        // Arrange
        when(userRoleRepository.findByIdUserId(1L)).thenReturn(Optional.of(mockUserRole));
        when(roleRepository.findById(101L)).thenReturn(Optional.of(mockRole));

        // Act
        Optional<Role> result = userRoleService.getRoleByUserId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockRole, result.get());
        verify(userRoleRepository, times(1)).findByIdUserId(1L);
        verify(roleRepository, times(1)).findById(101L);
    }

    @Test
    void testGetRoleByUserId_UserDoesNotExist() {
        // Arrange
        when(userRoleRepository.findByIdUserId(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Role> result = userRoleService.getRoleByUserId(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(userRoleRepository, times(1)).findByIdUserId(1L);
        verify(roleRepository, times(0)).findById(anyLong());
    }

    @Test
    void testCreateRoleForUser_ShouldSaveUserRole() {
        // Arrange
        Long userId = 2L;
        Long roleId = 202L;

        // Act
        userRoleService.createRoleForUser(roleId, userId);

        // Assert
        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository, times(1)).save(captor.capture());

        UserRole savedUserRole = captor.getValue();
        assertNotNull(savedUserRole);
        assertNotNull(savedUserRole.getId());
        assertEquals(userId, savedUserRole.getId().getUserId());
        assertEquals(roleId, savedUserRole.getId().getRoleId());
    }*/


}