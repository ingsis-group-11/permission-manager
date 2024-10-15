package permission_manager.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import permission_manager.permission.model.entities.UserPermission;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, String> {
    UserPermission findByUserIdAndSnippetId(Long userId, Long snippetId);
}

