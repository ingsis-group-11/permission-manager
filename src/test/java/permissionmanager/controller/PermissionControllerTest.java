package permissionmanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import permissionmanager.model.dto.AllSnippetsSendDto;
import permissionmanager.model.dto.CreatePermissionDto;
import permissionmanager.model.dto.SnippetDto;
import permissionmanager.model.dto.SnippetSharedDto;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.service.PermissionService;

public class PermissionControllerTest {

  @Mock private PermissionService permissionService;

  @InjectMocks private PermissionController permissionController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    Jwt jwt = mock(Jwt.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(jwt);
    when(jwt.getClaimAsString("sub")).thenReturn("1");

    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void createPermissionSuccess() {
    String userId = "1";
    String snippetId = "1";
    String expectedResponse = "Permission created successfully as: READ";
    PermissionType permissionType = PermissionType.READ;

    CreatePermissionDto request = new CreatePermissionDto();
    request.setAssetId(snippetId);
    request.setPermission(permissionType);

    when(permissionService.newPermission(userId, snippetId, permissionType))
        .thenReturn(expectedResponse);

    ResponseEntity<String> response = permissionController.newPermission(request);

    assertEquals(expectedResponse, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void createPermissionFailure() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    CreatePermissionDto request = new CreatePermissionDto();
    request.setAssetId(snippetId);
    request.setPermission(permissionType);

    when(permissionService.newPermission(userId, snippetId, permissionType))
        .thenThrow(new RuntimeException("Error creating permission: Database error"));

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              permissionController.newPermission(request);
            });

    assertEquals("Error creating permission: Database error", exception.getMessage());
  }

  @Test
  void getExistingPermissions() {
    String userId = "1";
    String snippetId = "1";
    String expectedResponse = "READ";

    when(permissionService.getPermission(userId, snippetId)).thenReturn(expectedResponse);

    ResponseEntity<String> response = permissionController.getPermission(snippetId);

    assertEquals("READ", response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void getPermissionNotFound() {
    String userId = "1";
    String snippetId = "1";

    when(permissionService.getPermission(userId, snippetId))
        .thenThrow(new RuntimeException("Error getting permission: Permission not found"));

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              permissionController.getPermission(snippetId);
            });

    assertEquals("Error getting permission: Permission not found", exception.getMessage());
  }

  @Test
  void getSnippetsSuccess() {
    String userId = "1";
    int from = 0;
    int to = 2;
    PermissionType permissionType = PermissionType.READ;

    AllSnippetsSendDto expectedResponse = AllSnippetsSendDto.builder().build();
    expectedResponse.setSnippetsIds(
        List.of(
            SnippetDto.builder().snippetId("snippet1").author("author1").build(),
            SnippetDto.builder().snippetId("snippet2").author("author2").build()));
    expectedResponse.setMaxSnippets(2);

    when(permissionService.getSnippetsId(from, to, userId, permissionType))
        .thenReturn(expectedResponse);

    ResponseEntity<AllSnippetsSendDto> response =
        permissionController.getSnippets(from, to, permissionType);

    assertEquals(expectedResponse, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    verify(permissionService, times(1)).getSnippetsId(from, to, userId, permissionType);
  }

  @Test
  void deletePermissionSuccess() {
    String userId = "1";
    String snippetId = "1";
    String expectedResponse = "Permission deleted successfully";

    when(permissionService.deletePermission(userId, snippetId)).thenReturn(expectedResponse);

    ResponseEntity<String> response = permissionController.deletePermission(snippetId);

    assertEquals(expectedResponse, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void deletePermissionFailure() {
    String userId = "1";
    String snippetId = "1";

    when(permissionService.deletePermission(userId, snippetId))
        .thenThrow(new RuntimeException("Error deleting permission: Database error"));

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              permissionController.deletePermission(snippetId);
            });

    assertEquals("Error deleting permission: Database error", exception.getMessage());
  }

  @Test
  void shareSnippetSuccess() {
    String fromUserId = "1";
    String toUserId = "2";
    String snippetId = "1";
    String expectedResponse = "Snippet shared successfully";
    SnippetSharedDto request = new SnippetSharedDto();
    request.setSnippetId(snippetId);
    request.setToUserId(toUserId);

    when(permissionService.shareSnippet(fromUserId, snippetId, toUserId))
        .thenReturn(expectedResponse);

    ResponseEntity<String> response = permissionController.shareSnippet(request);

    assertEquals(expectedResponse, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void shareSnippetFailure() {
    String fromUserId = "1";
    String toUserId = "2";
    String snippetId = "1";

    SnippetSharedDto request = new SnippetSharedDto();
    request.setSnippetId(snippetId);
    request.setToUserId(toUserId);

    when(permissionService.shareSnippet(fromUserId, snippetId, toUserId))
        .thenThrow(
            new PermissionDeniedDataAccessException(
                "You don't have permission share this snippet", new Exception()));

    Exception exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              permissionController.shareSnippet(request);
            });

    assertEquals("You don't have permission share this snippet", exception.getMessage());
  }

  @Test
  void checkPermissionsSuccess() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    CreatePermissionDto request = new CreatePermissionDto();
    request.setAssetId(snippetId);
    request.setPermission(permissionType);

    when(permissionService.checkPermissions(userId, snippetId, permissionType)).thenReturn(true);

    ResponseEntity<Boolean> response = permissionController.checkPermissions(request);

    assertEquals(true, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void checkPermissionsFailure() {
    String userId = "1";
    String snippetId = "1";
    PermissionType permissionType = PermissionType.READ;

    CreatePermissionDto request = new CreatePermissionDto();
    request.setAssetId(snippetId);
    request.setPermission(permissionType);

    when(permissionService.checkPermissions(userId, snippetId, permissionType)).thenReturn(false);

    ResponseEntity<Boolean> response = permissionController.checkPermissions(request);

    assertEquals(false, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }
}
