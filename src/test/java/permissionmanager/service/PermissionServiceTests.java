package permissionmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.model.entities.UserPermission;
import permissionmanager.repository.PermissionRepository;

public class PermissionServiceTests {

  @Mock private PermissionRepository permissionRepository;

  @InjectMocks private PermissionService permissionService;

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
    userPermission.setPermissionId(UUID.randomUUID());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(permissionType);

    when(permissionRepository.save(any(UserPermission.class))).thenReturn(userPermission);

    String response = permissionService.newPermission(userId, snippetId, permissionType);

    assertEquals("Permission created successfully as: READ", response);
    verify(permissionRepository, times(1)).save(any(UserPermission.class));
  }

  @Test
  void getPermissionSuccess() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(permissionType);

    when(permissionRepository.findByUserIdAndSnippetId(userId, snippetId))
        .thenReturn(userPermission);

    String response = permissionService.getPermission(userId, snippetId);

    assertEquals("READ", response);
    verify(permissionRepository, times(1)).findByUserIdAndSnippetId(userId, snippetId);
  }
}
