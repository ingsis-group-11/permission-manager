package permissionmanager.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.model.entities.UserPermission;
import permissionmanager.repository.PermissionRepository;

@Service
public class PermissionService {

  @Autowired private PermissionRepository permissionRepository;

  public String processData(String data) {
    return "Processed data: " + data;
  }

  public String getPermission(String userId, String snippetId) {
    try {
      PermissionType result =
          permissionRepository.findByUserIdAndSnippetId(userId, snippetId).getPermission();
      return result.toString();
    } catch (Exception e) {
      throw new RuntimeException("Error getting permission: " + e.getMessage());
    }
  }

  public String newPermission(String userId, String snippetId, PermissionType permission) {
    UserPermission userPermission = new UserPermission();
    userPermission.setPermissionId(UUID.randomUUID());
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    PermissionType newPermission = handlePermission(userId, snippetId, permission);
    userPermission.setPermission(newPermission);
    try {
      permissionRepository.save(userPermission);
    } catch (Exception e) {
      throw new RuntimeException("Error creating permission: " + e.getMessage());
    }
    return "Permission created successfully as: " + newPermission;
  }

  private PermissionType handlePermission(
      String userId, String snippetId, PermissionType permission) {
    UserPermission userPermission =
        permissionRepository.findByUserIdAndSnippetId(userId, snippetId);
    if (userPermission == null) {
      return permission;
    }
    if (userPermission.getPermission().equals(PermissionType.READ)
        && permission == PermissionType.WRITE) {
      return PermissionType.READ_WRITE;
    }

    return userPermission.getPermission();
  }

  public List<String> getSnippetsId(Long from, Long to, String userId) {
    return permissionRepository.getSnippetsId(from, to, userId);
  }
}