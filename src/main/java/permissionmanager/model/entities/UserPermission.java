package permissionmanager.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Getter
@Setter
public class UserPermission {
  @Id private UUID permissionId;
  private String userId;
  private String snippetId;
  private PermissionType permission;
}