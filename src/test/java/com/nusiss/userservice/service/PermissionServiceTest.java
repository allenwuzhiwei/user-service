package com.nusiss.userservice.service;

import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dto.PermissionDTO;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.PermissionProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private Permission permission;

    @BeforeEach
    void setup() {
        permission = new Permission();
        permission.setId(1);
        permission.setEndpoint("/api/test");
        permission.setMethod("GET");
        permission.setDescription("Test permission");
        permission.setCreateUser("admin");
        permission.setCreateDatetime(LocalDateTime.now());
    }

    @Test
    void testCreate() {
        when(permissionRepository.save(permission)).thenReturn(permission);

        Permission created = permissionService.create(permission);

        assertNotNull(created);
        assertEquals(permission.getId(), created.getId());
        verify(permissionRepository, times(1)).save(permission);
    }

    @Test
    void testGetFound() {
        when(permissionRepository.findById(1)).thenReturn(Optional.of(permission));

        Optional<Permission> result = permissionService.get(1);

        assertTrue(result.isPresent());
        assertEquals(permission.getId(), result.get().getId());
    }

    @Test
    void testGetNotFound() {
        when(permissionRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Permission> result = permissionService.get(2);

        assertFalse(result.isPresent());
    }

    @Test
    void testList() {
        List<Permission> list = Arrays.asList(permission);
        when(permissionRepository.findAll()).thenReturn(list);

        List<Permission> result = permissionService.list();

        assertEquals(1, result.size());
        verify(permissionRepository, times(1)).findAll();
    }

    @Test
    void testUpdateSuccess() {
        Permission updatedData = new Permission();
        updatedData.setEndpoint("/api/updated");
        updatedData.setMethod("POST");

        when(permissionRepository.findById(1)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Permission updated = permissionService.update(1, updatedData);

        assertEquals("/api/updated", updated.getEndpoint());
        assertEquals("POST", updated.getMethod());
        verify(permissionRepository).save(permission);
    }

    @Test
    void testUpdateNotFound() {
        when(permissionRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            permissionService.update(1, permission);
        });
    }

    @Test
    void testDelete() {
        doNothing().when(permissionRepository).deleteById(1);

        permissionService.delete(1);

        verify(permissionRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAllPermissions() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Permission> list = Arrays.asList(permission);
        Page<Permission> page = new PageImpl<>(list, pageable, list.size());

        when(permissionRepository.findAll(pageable)).thenReturn(page);

        Page<Permission> result = permissionService.getAllPermissions(pageable);

        assertEquals(1, result.getTotalElements());
        verify(permissionRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllPermissionsDTO() {
        Pageable pageable = PageRequest.of(0, 10);

        PermissionProjection projection = new PermissionProjection() {
            @Override
            public Integer getId() { return 1; }
            @Override
            public String getEndpoint() { return "/api/test"; }
            @Override
            public String getMethod() { return "GET"; }
            @Override
            public String getDescription() { return "desc"; }
            @Override
            public String getCreateUser() { return "admin"; }
            @Override
            public LocalDateTime getCreateDatetime() { return LocalDateTime.now(); }
            @Override
            public String getUpdateUser() { return "admin"; }
            @Override
            public LocalDateTime getUpdateDatetime() { return LocalDateTime.now(); }
            @Override
            public String getRoles() { return "ROLE_ADMIN,ROLE_USER"; }
        };

        List<PermissionProjection> list = List.of(projection);
        Page<PermissionProjection> page = new PageImpl<>(list, pageable, list.size());

        when(permissionRepository.findAllPermissionsWithRoles("", pageable)).thenReturn(page);

        Page<PermissionDTO> result = permissionService.getAllPermissionsDTO("", pageable);

        assertEquals(1, result.getTotalElements());
        PermissionDTO dto = result.getContent().get(0);
        assertEquals(1, dto.getId());
        assertEquals("/api/test", dto.getEndpoint());
        assertTrue(dto.getRoles().contains("ROLE_ADMIN"));
        assertTrue(dto.getRoles().contains("ROLE_USER"));
    }

    @Test
    void testFindPermissionsByUserId() {
        Set<String> permissions = new HashSet<>(Arrays.asList("PERM_READ", "PERM_WRITE"));
        when(permissionRepository.findPermissionsByUserId(1)).thenReturn(permissions);

        Set<String> result = permissionService.findPermissionsByUserId(1);

        assertEquals(2, result.size());
        assertTrue(result.contains("PERM_READ"));
    }
}
