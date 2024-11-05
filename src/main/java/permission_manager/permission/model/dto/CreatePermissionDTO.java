package permission_manager.permission.model.dto;

import lombok.Getter;
import lombok.Setter;
import permission_manager.permission.model.entities.PermissionType;

@Getter
@Setter
public class CreatePermissionDTO {
    private String userId;
    private String snippetId;
    private PermissionType permission;
}