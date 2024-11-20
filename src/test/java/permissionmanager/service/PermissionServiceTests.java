package permissionmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.PermissionDeniedDataAccessException;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.model.entities.UserPermission;
import permissionmanager.repository.PermissionRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PermissionServiceTests {

  @Mock
  private PermissionRepository permissionRepository;

  @InjectMocks
  private PermissionService permissionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void newPermissionSuccess() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID().toString());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(permissionType);

    when(permissionRepository.save(any(UserPermission.class))).thenReturn(userPermission);

    String response = permissionService.newPermission(userId, snippetId, permissionType);

    assertEquals("Permission created successfully as: READ", response);
    verify(permissionRepository, times(1)).save(any(UserPermission.class));
  }

  @Test
  void newPermissionFailure() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    doThrow(new RuntimeException("Database error")).when(permissionRepository).save(any(UserPermission.class));

    Exception exception = assertThrows(RuntimeException.class, () -> {
      permissionService.newPermission(userId, snippetId, permissionType);
    });

    assertEquals("Error creating permission: Database error", exception.getMessage());
    verify(permissionRepository, times(1)).save(any(UserPermission.class));
  }

  @Test
  void getPermissionSuccess() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID().toString());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(permissionType);

    when(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(userPermission);

    String response = permissionService.getPermission(userId, snippetId);

    assertEquals("READ", response);
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(userId, snippetId);
  }

  @Test
  void getPermissionNotFound() {
    String userId = "1";
    String snippetId = "1";

    when(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(null);

    Exception exception = assertThrows(RuntimeException.class, () -> {
      permissionService.getPermission(userId, snippetId);
    });

    assertEquals("Error getting permission: Permission not found", exception.getMessage());
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(userId, snippetId);
  }

  @Test
  void deletePermissionSuccess() {
    String userId = "1";
    String snippetId = "1";

    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID().toString());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(PermissionType.READ_WRITE);

    when(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(userPermission);
    doNothing().when(permissionRepository).deleteAllBySnippetId(snippetId);

    String response = permissionService.deletePermission(userId, snippetId);

    assertEquals("Permission deleted successfully", response);
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(userId, snippetId);
    verify(permissionRepository, times(1)).deleteAllBySnippetId(snippetId);
  }

  @Test
  void deletePermissionFailure() {
    String userId = "1";
    String snippetId = "1";

    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID().toString());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(PermissionType.READ_WRITE);

    when(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(userPermission);
    doThrow(new RuntimeException("Database error")).when(permissionRepository).deleteAllBySnippetId(snippetId);

    Exception exception = assertThrows(RuntimeException.class, () -> {
      permissionService.deletePermission(userId, snippetId);
    });

    assertEquals("Error deleting permission: Database error", exception.getMessage());
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(userId, snippetId);
    verify(permissionRepository, times(1)).deleteAllBySnippetId(snippetId);
  }

  @Test
  void shareSnippetSuccess() {
    String fromUserId = "1";
    String toUserId = "2";
    String snippetId = "1";

    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID().toString());
    userPermission.setUserId(fromUserId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(PermissionType.READ_WRITE);

    when(permissionRepository.findByUserIdAndSnippetId(fromUserId, snippetId)).thenReturn(userPermission);
    when(permissionRepository.save(any(UserPermission.class))).thenReturn(userPermission);

    String response = permissionService.shareSnippet(fromUserId, snippetId, toUserId);

    assertEquals("Snippet shared successfully", response);
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(fromUserId, snippetId);
    verify(permissionRepository, times(1)).save(any(UserPermission.class));
  }

  @Test
  void shareSnippetFailure() {
    String fromUserId = "1";
    String toUserId = "2";
    String snippetId = "1";

    when(permissionRepository.findByUserIdAndSnippetId(fromUserId, snippetId)).thenReturn(null);

    Exception exception = assertThrows(PermissionDeniedDataAccessException.class, () -> {
      permissionService.shareSnippet(fromUserId, snippetId, toUserId);
    });

    assertEquals("You don't have permission share this snippet", exception.getMessage());
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(fromUserId, snippetId);
    verify(permissionRepository, times(0)).save(any(UserPermission.class));
  }

  @Test
  void checkPermissionsSuccess() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;
    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID().toString());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(permissionType);

    when(permissionRepository.findByUserIdAndSnippetId(userId, snippetId)).thenReturn(userPermission);

    boolean response = permissionService.checkPermissions(userId, snippetId, permissionType);

    assertTrue(response);
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(userId, snippetId);
  }
}