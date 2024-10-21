package permissionmanager.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserPermission {
  @Id private UUID permissionId;
  private Long userId;
  private Long snippetId;
  private PermissionType permission;
}
