package permissionmanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import permissionmanager.model.dto.CreatePermissionDto;
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
    String expectedResponse = "READ";
    PermissionType permissionType = PermissionType.READ;

    CreatePermissionDto request = new CreatePermissionDto();
    request.setUserId(userId);
    request.setSnippetId(snippetId);
    request.setPermission(permissionType);

    when(permissionService.newPermission(userId, snippetId, permissionType))
        .thenReturn(expectedResponse);

    permissionController.newPermission(request);

    ResponseEntity<String> response = permissionController.newPermission(request);

    assertEquals("READ", response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
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
}
