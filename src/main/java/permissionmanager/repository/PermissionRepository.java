package permissionmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import permissionmanager.model.entities.UserPermission;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, String> {
  UserPermission findByUserIdAndSnippetId(String userId, String snippetId);

  @Query(
      "SELECT p.snippetId FROM UserPermission p WHERE "
          + "p.snippetId BETWEEN :from AND :to AND p.userId = :userId")
  List<String> getSnippetsId(Long from, Long to, String userId);
}