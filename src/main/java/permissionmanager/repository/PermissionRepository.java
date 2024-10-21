package permissionmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import permissionmanager.model.entities.UserPermission;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, String> {
  UserPermission findByUserIdAndSnippetId(Long userId, Long snippetId);
}
