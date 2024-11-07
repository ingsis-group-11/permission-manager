package permissionmanager.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequestDto {
  private String userId;
  private String snippetId;
}
