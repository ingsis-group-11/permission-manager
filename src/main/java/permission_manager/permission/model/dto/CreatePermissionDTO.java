package permission_manager.permission.model.dto;

import lombok.Getter;
import permission_manager.permission.model.entities.PermissionType;

@Getter
public class CreatePermissionDTO {
    private Long userId;
    private Long snippetId;
    private PermissionType permission;
}