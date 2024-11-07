package permissionmanager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import permissionmanager.model.dto.CreatePermissionDto;
import permissionmanager.service.PermissionService;

@RequestMapping("/api/permission")
@RestController
public class PermissionController {

  private final PermissionService permissionService;

  private String getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();
    String userId = jwt.getClaimAsString("sub");
    int position = userId.indexOf("|");

    if (position != -1) {
      userId = userId.substring(position + 1);
    }

    return userId;
  }

  @Autowired
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @GetMapping("/{snippetId}")
  public ResponseEntity<String> getPermission(@PathVariable String snippetId) {
    return ResponseEntity.ok(permissionService.getPermission(getUserId(), snippetId));
  }

  @PutMapping
  public ResponseEntity<String> newPermission(@RequestBody CreatePermissionDto request) {
    return ResponseEntity.ok(
        permissionService.newPermission(
            request.getUserId(), request.getSnippetId(), request.getPermission()));
  }

  @GetMapping
  public ResponseEntity<List<String>> getSnippets(
      @RequestParam("from") Long from, @RequestParam("to") Long to) {
    List<String> snippets = permissionService.getSnippetsId(from, to, getUserId());
    return ResponseEntity.ok(snippets);
  }
}
