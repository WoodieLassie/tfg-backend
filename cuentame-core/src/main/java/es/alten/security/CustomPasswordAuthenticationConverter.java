package es.alten.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import jakarta.servlet.http.HttpServletRequest;

public class CustomPasswordAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {
    final String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
    if (!CustomPasswordAuthenticationConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD_NAME
        .equals(grantType)) {
      return null;
    }

    final MultiValueMap<String, String> parameters = getParameters(request);

    final String scope = getParamValue(parameters, OAuth2ParameterNames.SCOPE);
    getParamValue(parameters, OAuth2ParameterNames.USERNAME);
    getParamValue(parameters, OAuth2ParameterNames.PASSWORD);

    Set<String> requestedScopes = null;
    if (StringUtils.isNotBlank(scope)) {
      requestedScopes = new HashSet<>(Arrays.asList(StringUtils.split(scope, StringUtils.SPACE)));
    }

    final Map<String, Object> additionalParameters = new HashMap<>();
    parameters.forEach((key, value) -> {
      if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.SCOPE)) {
        additionalParameters.put(key, value.get(0));
      }
    });

    final Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

    return new CustomPasswordAuthenticationToken(clientPrincipal, requestedScopes,
        additionalParameters);
  }

  private MultiValueMap<String, String> getParameters(HttpServletRequest request) {
    final Map<String, String[]> parameterMap = request.getParameterMap();
    final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
    parameterMap.forEach((key, values) -> {
      if (values.length > 0) {
        for (String value : values) {
          parameters.add(key, value);
        }
      }
    });

    return parameters;
  }

  private String getParamValue(MultiValueMap<String, String> parameters, String key) {
    final String value = parameters.getFirst(key);
    if (StringUtils.isNotBlank(value) && parameters.get(key).size() != 1) {
      throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
    }

    return value;
  }

}
