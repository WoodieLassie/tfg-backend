package es.alten.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings({"WeakerAccess", "unused"})
public class NavSection {
  private String section;
  private NavSectionType sectionType;
  private static final String TITLE_KEY = "title";
  private static final String LIST_KEY = "list";

  public String getTitle() {
    return getTitleKey();
  }

  public String getTitleKey() {

    switch (sectionType) {
      case PROFILE:
        return section + "." + TITLE_KEY;
      case DETAIL:
        return section + "." + TITLE_KEY;
      default:
        return section + "." + LIST_KEY + "." + TITLE_KEY;
    }
  }

  public enum NavSectionType {
    DETAIL,
    LIST,
    PROFILE
  }
}
