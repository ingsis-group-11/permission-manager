package permission_manager.permission.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Getter
@Setter
public class UserPermission {
    @Id
    private UUID permissionId;
    private Long userId;
    private Long snippetId;
    private PermissionType permission;
}