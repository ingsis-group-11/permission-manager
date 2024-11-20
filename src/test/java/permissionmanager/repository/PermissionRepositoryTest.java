package permissionmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.model.entities.UserPermission;

public class PermissionRepositoryTest {

  @Mock private PermissionRepository permissionRepository;

  private UserPermission userPermission1;

  private UserPermission userPermission2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    userPermission1 = new UserPermission();
    userPermission1.setPermissionId("1");
    userPermission1.setUserId("user1");
    userPermission1.setSnippetId("snippet1");
    userPermission1.setPermission(PermissionType.READ);

    userPermission2 = new UserPermission();
    userPermission2.setPermissionId("2");
    userPermission2.setUserId("user1");
    userPermission2.setSnippetId("snippet2");
    userPermission2.setPermission(PermissionType.READ_WRITE);
  }

  @Test
  void testFindByUserIdAndSnippetId() {
    when(permissionRepository.findByUserIdAndSnippetId("user1", "snippet1"))
        .thenReturn(userPermission1);

    UserPermission found = permissionRepository.findByUserIdAndSnippetId("user1", "snippet1");
    assertNotNull(found);
    assertEquals("snippet1", found.getSnippetId());
  }

  @Test
  void testGetUserPermissionsByUserId() {
    when(permissionRepository.getUserPermissionsByUserId("user1"))
        .thenReturn(List.of(userPermission1, userPermission2));

    List<UserPermission> permissions = permissionRepository.getUserPermissionsByUserId("user1");
    assertEquals(2, permissions.size());
  }

  @Test
  void testDeleteAllBySnippetId() {
    doNothing().when(permissionRepository).deleteAllBySnippetId("snippet1");

    permissionRepository.deleteAllBySnippetId("snippet1");
    verify(permissionRepository, times(1)).deleteAllBySnippetId("snippet1");
  }

  @Test
  void testFindBySnippetIdAndPermission() {
    when(permissionRepository.findBySnippetIdAndPermission("snippet2", PermissionType.READ_WRITE))
        .thenReturn(Optional.of(userPermission2));

    Optional<UserPermission> found =
        permissionRepository.findBySnippetIdAndPermission("snippet2", PermissionType.READ_WRITE);
    assertTrue(found.isPresent());
    assertEquals("snippet2", found.get().getSnippetId());
  }
}
