package permissionmanager.model.dto;

import lombok.Getter;
import lombok.Setter;
import permissionmanager.model.entities.PermissionType;

@Getter
@Setter
public class CreatePermissionDto {
  private String userId;
  private String snippetId;
  private PermissionType permission;
}
