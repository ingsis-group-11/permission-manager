package permission_manager.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import permission_manager.permission.model.dto.CreatePermissionDTO;
import permission_manager.permission.model.dto.PermissionRequestDTO;
import permission_manager.permission.model.entities.PermissionType;
import permission_manager.permission.service.PermissionService;

@RestController
public class PermissionController {

  private final PermissionService permissionService;

  @Autowired
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @PostMapping("/api/receive-data")
  public String receiveData(@RequestBody String data) {
    return permissionService.processData(data);
  }

  @PostMapping("/api/permission")
  public ResponseEntity<String> createPermission(@RequestBody PermissionRequestDTO request) {
    PermissionType result = permissionService.getPermission(request.getUserId(), request.getSnippetId());
    return ResponseEntity.ok(result.toString());
  }

  @PostMapping("/api/new-permission")
  public ResponseEntity<String> newPermission(@RequestBody CreatePermissionDTO request) {
    PermissionType result = permissionService.newPermission(request.getUserId(), request.getSnippetId(), request.getPermission());
    return ResponseEntity.ok("Permission created as: " + result);
  }
}


