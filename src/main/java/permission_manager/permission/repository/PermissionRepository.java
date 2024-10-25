package permission_manager.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import permission_manager.permission.model.entities.UserPermission;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, String> {
  UserPermission findByUserIdAndSnippetId(String userId, String snippetId);

  @Query("SELECT p.snippetId FROM UserPermission p WHERE p.snippetId BETWEEN :from AND :to AND p.userId = :userId")
  List<String> getSnippetsId(Long from, Long to, String userId);
}

