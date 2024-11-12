package permissionmanager.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class AllSnippetsSendDto {
  private List<String> snippetsIds;
  private int maxSnippets;
}
