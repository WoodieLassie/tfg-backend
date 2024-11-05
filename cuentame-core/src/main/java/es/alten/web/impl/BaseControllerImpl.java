package es.alten.web.impl;

import es.alten.web.BaseController;
import es.alten.web.NavSection;
import es.alten.web.i18n.CustomReloadableResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements interface {@link BaseController}.
 *
 * @author irojas
 * @noinspection WeakerAccess
 */
public abstract class BaseControllerImpl implements BaseController {

  /** serialVersionUID for object serialization. */
  private static final long serialVersionUID = -4249087928971436015L;

  /** The Constant MESSAGES_KEY. */
  public static final String MESSAGES_KEY = "i18n";

  /** The Constant FORM_KEY. */
  public static final String FORM_KEY = "form";

  /** The Constant ERROR_KEY. */
  public static final String ERROR_KEY = "errors";

  /** The i18n. */
  protected transient List<String> messages;

  /** The nav section. */
  protected transient NavSection navSection;

  /**
   * The message source.
   *
   * @noinspection SpringJavaAutowiredMembersInspection
   */
  @Autowired protected transient CustomReloadableResourceBundleMessageSource messageSource;

  /**
   * Adds an attribute 'i18n' to each HTTP response. Messages list stores information, validation
   * and error i18n to show the final user.
   *
   * @return Messages to add to response.
   */
  @ModelAttribute("messages")
  public List<String> initMessages() {
    messages = new ArrayList<>();
    return messages;
  }

  /**
   * Adds an attribute 'navSection' to each HTTP response. NavSection stores the actual section of
   * the web final user is viewing.
   *
   * @return Actual navigation section.
   */
  @ModelAttribute("navSection")
  public NavSection initSection() {
    if (navSection == null) {
      navSection = new NavSection();
    }
    return navSection;
  }

  /**
   * Gets the i18n.
   *
   * @return the i18n
   * @noinspection unused
   */
  public List<String> getMessages() {
    return new ArrayList<>();
  }

  /**
   * Sets the i18n.
   *
   * @param messages the new i18n
   * @noinspection unused
   */
  public void setMessages(List<String> messages) {
    this.messages = messages;
  }

  /**
   * Adds the message.
   *
   * @param message the message
   */
  public void addMessage(String message) {
    this.messages.add(message);
  }
}
