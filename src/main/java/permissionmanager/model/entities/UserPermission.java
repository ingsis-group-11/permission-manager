package permissionmanager.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserPermission {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String permissionId;

  private String userId;
  private String snippetId;

  @Enumerated(EnumType.STRING)
  private PermissionType permission;
}
