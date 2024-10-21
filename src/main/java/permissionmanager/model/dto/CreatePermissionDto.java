package permissionmanager.model.dto;

import lombok.Getter;
import permissionmanager.model.entities.PermissionType;

@Getter
public class CreatePermissionDto {
  private Long userId;
  private Long snippetId;
  private PermissionType permission;
}
