package permissionmanager.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import permissionmanager.model.dto.CreatePermissionDto;
import permissionmanager.model.dto.PermissionRequestDto;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.service.PermissionService;

@RestController
public class PermissionController {

  private final PermissionService permissionService;

  private String getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Jwt jwt = (Jwt) authentication.getPrincipal();

    return jwt.getClaimAsString("sub");
  }

  @Autowired
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @PostMapping("/api/receive-data")
  public String receiveData(@RequestBody String data) {
    return permissionService.processData(data);
  }

  @PostMapping("/api/permission")
  public ResponseEntity<String> createPermission(@RequestBody PermissionRequestDto request) {
    PermissionType result =
        permissionService.getPermission(request.getUserId(), request.getSnippetId());
    return ResponseEntity.ok(result.toString());
  }

  @PostMapping("/api/new-permission")
  public ResponseEntity<String> newPermission(@RequestBody CreatePermissionDto request) {
    PermissionType result =
        permissionService.newPermission(
            request.getUserId(), request.getSnippetId(), request.getPermission());
    return ResponseEntity.ok("Permission created as: " + result);
  }

  @GetMapping("/api/get-snippets")
  public ResponseEntity<List<String>> getSnippets(
      @RequestParam("from") Long from, @RequestParam("to") Long to) {
    List<String> snippets = permissionService.getSnippetsId(from, to, getUserId());
    return ResponseEntity.ok(snippets);
  }
}
