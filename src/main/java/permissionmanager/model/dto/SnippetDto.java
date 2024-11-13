package permissionmanager.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class SnippetDto {
  private String snippetId;
  private String author;
}
