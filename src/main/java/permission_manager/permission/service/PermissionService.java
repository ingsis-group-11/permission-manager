package permission_manager.permission.service;

import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  public String processData(String data) {
    return "Processed data: " + data;
  }
}
