package permissionmanager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import permissionmanager.model.dto.CreatePermissionDto;
import permissionmanager.model.dto.SnippetSharedDto;
import permissionmanager.model.entities.PermissionType;
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
            getUserId(), request.getAssetId(), request.getPermission()));
  }

  @GetMapping
  public ResponseEntity<List<String>> getSnippets(
      @RequestParam(value = "from", required = false) Integer from,
      @RequestParam(value = "to", required = false) Integer to,
      @RequestParam(value = "permissionType") PermissionType permissionType) {

    if (from == null) {
      from = 0;
    }
    if (to == null) {
      to = Integer.MAX_VALUE;
    }

    List<String> snippets = permissionService.getSnippetsId(from, to, getUserId(), permissionType);
    return ResponseEntity.ok(snippets);
  }

  @DeleteMapping("/{snippetId}")
  public ResponseEntity<String> deletePermission(@PathVariable String snippetId) {
    return ResponseEntity.ok(permissionService.deletePermission(getUserId(), snippetId));
  }

  @PostMapping("/share")
  public ResponseEntity<String> shareSnippet(@RequestBody SnippetSharedDto request) {
    return ResponseEntity.ok(
        permissionService.shareSnippet(getUserId(), request.getSnippetId(), request.getToUserId()));
  }

  @PostMapping()
  public ResponseEntity<Boolean> checkPermissions(@RequestBody CreatePermissionDto request) {
    return ResponseEntity.ok(
        permissionService.checkPermissions(
            getUserId(), request.getAssetId(), request.getPermission()));
  }
}
