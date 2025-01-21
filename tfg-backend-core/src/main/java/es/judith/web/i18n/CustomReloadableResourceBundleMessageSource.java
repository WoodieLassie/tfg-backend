package es.judith.web.i18n;

import java.util.Locale;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Custom class to get internationalized i18n from bundles. Extends
 * {@link ReloadableResourceBundleMessageSource} functionality resolving locale before to read
 * message from bundle.
 *
 * @author irojas
 */
@Service
public class CustomReloadableResourceBundleMessageSource
    extends ReloadableResourceBundleMessageSource {

  /** The locale resolver. */
  private LocaleResolver localeResolver;

  /**
   * Gets one message searching from its key into the bundles with the language of the HTTP request.
   *
   * @param code Message key in bundles.
   * @param req HTTP request.
   * @return Message text resolved.
   */
  public String getMessage(String code, HttpServletRequest req) {
    Locale locale = localeResolver.resolveLocale(req);
    String message = this.getMessage(code, null, locale);

    if (message.isEmpty()) {
      message = this.getDefaultMessage(code);
    }

    return message;
  }

  /**
   * Gets one message searching from its key into the bundles with the language of the HTTP request.
   *
   * @param code Message key in bundles.
   * @param req HTTP request.
   * @param args Arguments to include into message text.
   * @return Message text resolved.
   * @noinspection unused
   */
  public String getMessage(String code, HttpServletRequest req, Object... args) {
    Locale locale = localeResolver.resolveLocale(req);
    String message = this.getMessage(code, args, locale);

    if (message.isEmpty()) {
      message = this.getDefaultMessage(code);
    }

    return message;
  }

  /**
   * Gets the locale resolver.
   *
   * @return the locale resolver
   * @noinspection unused
   */
  public LocaleResolver getLocaleResolver() {
    return localeResolver;
  }

  /**
   * Sets the locale resolver.
   *
   * @param localeResolver the new locale resolver
   * @noinspection unused
   */
  public void setLocaleResolver(LocaleResolver localeResolver) {
    this.localeResolver = localeResolver;
  }
}
