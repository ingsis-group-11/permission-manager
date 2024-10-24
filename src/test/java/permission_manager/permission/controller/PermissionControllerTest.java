package permission_manager.permission.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import permission_manager.permission.model.entities.PermissionType;
import permission_manager.permission.service.PermissionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PermissionControllerTest {

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

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
    void createPermission() {
        String userId = "1";
        String snippetId = "1";

        when(permissionService.newPermission(userId, snippetId, any(PermissionType.class)))
                .thenReturn(PermissionType.READ);

        permissionController.newPermission(null); // FINISH

        verify(permissionService, times(1)).newPermission(userId, snippetId, PermissionType.READ );
    }
}
