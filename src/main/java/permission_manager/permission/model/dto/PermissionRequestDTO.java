package permission_manager.permission.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequestDTO {
    private String userId;
    private String snippetId;
}