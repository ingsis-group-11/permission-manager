package permissionmanager.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import permissionmanager.model.dto.AllSnippetsSendDto;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.model.entities.UserPermission;
import permissionmanager.repository.PermissionRepository;

@Service
public class PermissionService {

  @Autowired private PermissionRepository permissionRepository;

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
    userPermission.setUserId(userId);
    userPermission.setSnippetId(snippetId);
    userPermission.setPermission(permission);
    try {
      permissionRepository.save(userPermission);
    } catch (Exception e) {
      throw new RuntimeException("Error creating permission: " + e.getMessage());
    }
    return "Permission created successfully as: " + permission;
  }

  public AllSnippetsSendDto getSnippetsId(
      Integer from, Integer to, String userId, PermissionType permissionType) {
    List<UserPermission> allUserPermissions =
        permissionRepository.getUserPermissionsByUserId(userId);

    int maxSnippets = allUserPermissions.size();
    // Apply the range filtering
    List<UserPermission> filteredPermissions =
        allUserPermissions.stream()
            .skip(from != null ? from : 0)
            .limit(to != null ? to - (from != null ? from : 0) : allUserPermissions.size())
            .collect(Collectors.toList());

    List<String> snippetsIds =
        filteredPermissions.stream()
            .filter(up -> hasPermissions(permissionType, up.getPermission()))
            .map(UserPermission::getSnippetId)
            .collect(Collectors.toList());

    return AllSnippetsSendDto.builder().snippetsIds(snippetsIds).maxSnippets(maxSnippets).build();
  }

  @Transactional
  public String deletePermission(String userId, String snippetId) {
    UserPermission userPermission =
        permissionRepository.findByUserIdAndSnippetId(userId, snippetId);

    if (userPermission != null && userPermission.getPermission().equals(PermissionType.READ)) {
      throw new RuntimeException("User does not have permission to delete the snippet");
    }

    try {
      permissionRepository.deleteAllBySnippetId(snippetId);
    } catch (Exception e) {
      throw new RuntimeException("Error deleting permission: " + e.getMessage());
    }

    return "Permission deleted successfully";
  }

  public String shareSnippet(String fromUserId, String snippetId, String toUserId) {
    if (fromUserId.equals(toUserId)) {
      throw new IllegalArgumentException("You can't share a snippet with yourself");
    }

    UserPermission userPermission =
        permissionRepository.findByUserIdAndSnippetId(fromUserId, snippetId);

    if (userPermission == null || userPermission.getPermission().equals(PermissionType.READ)) {
      throw new PermissionDeniedDataAccessException(
          "You don't have permission share this snippet",
          new Exception("You don't have permission share this snippet"));
    }

    UserPermission newUserPermission = new UserPermission();
    newUserPermission.setUserId(toUserId);
    newUserPermission.setSnippetId(snippetId);
    newUserPermission.setPermission(PermissionType.READ);

    try {
      permissionRepository.save(newUserPermission);
    } catch (Exception e) {
      throw new RuntimeException("Error sharing snippet: " + e.getMessage());
    }

    return "Snippet shared successfully";
  }

  public boolean checkPermissions(String userId, String snippetId, PermissionType permission) {
    UserPermission userPermission =
        permissionRepository.findByUserIdAndSnippetId(userId, snippetId);
    return hasPermissions(permission, userPermission.getPermission());
  }

  private boolean hasPermissions(PermissionType permissionToBeChecked, PermissionType permission) {
    List<PermissionType> permissions = List.of(PermissionType.READ, PermissionType.READ_WRITE);
    return permissions.indexOf(permissionToBeChecked) <= permissions.indexOf(permission);
  }
}
