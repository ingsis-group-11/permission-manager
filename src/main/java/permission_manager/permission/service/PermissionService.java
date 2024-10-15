package permission_manager.permission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import permission_manager.permission.model.entities.PermissionType;
import permission_manager.permission.model.entities.UserPermission;
import permission_manager.permission.repository.PermissionRepository;

import java.util.UUID;

@Service
public class PermissionService {

  @Autowired
  private PermissionRepository permissionRepository;

  public String processData(String data) {
    return "Processed data: " + data;
  }

  public PermissionType getPermission(Long userId, Long snippetId) {
    return permissionRepository.findByUserIdAndSnippetId(userId, snippetId).getPermission();
  }

  public PermissionType newPermission(Long userId, Long snippetId, PermissionType permission) {
    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    PermissionType newPermission = handlePermission(userId, snippetId, permission);
    userPermission.setPermission(newPermission);
    permissionRepository.save(userPermission);
    return newPermission;
  }

  private PermissionType handlePermission(Long userId, Long snippetId, PermissionType permission) {
    UserPermission userPermission = permissionRepository.findByUserIdAndSnippetId(userId, snippetId);
    if (userPermission == null) {
      return permission;
    }
    if (userPermission.getPermission().equals(PermissionType.READ) && permission == PermissionType.WRITE) {
      return PermissionType.READ_WRITE;
    }

    return userPermission.getPermission();
  }
}
