package permission_manager.permission.model.dto;

import lombok.Getter;
import permission_manager.permission.model.entities.PermissionType;

@Getter
public class CreatePermissionDTO {
    private String userId;
    private String snippetId;
    private PermissionType permission;
}