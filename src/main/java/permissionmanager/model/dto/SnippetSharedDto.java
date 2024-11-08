package permissionmanager.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnippetSharedDto {
  private String toUserId;
  private String snippetId;
}
