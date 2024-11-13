package permissionmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import permissionmanager.model.entities.PermissionType;
import permissionmanager.model.entities.UserPermission;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, String> {
  UserPermission findByUserIdAndSnippetId(String userId, String snippetId);

  @Query("SELECT p FROM UserPermission p WHERE p.userId = :userId")
  List<UserPermission> getUserPermissionsByUserId(String userId);

  void deleteAllBySnippetId(String snippetId);

  Optional<UserPermission> findBySnippetIdAndPermission(
      String snippetId, PermissionType permissionType);
}
