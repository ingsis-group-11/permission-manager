package permissionmanager.model.dto;

import lombok.Getter;
import permissionmanager.model.entities.PermissionType;

@Getter
public class CreatePermissionDto {
  private String userId;
  private String snippetId;
  private PermissionType permission;
}
