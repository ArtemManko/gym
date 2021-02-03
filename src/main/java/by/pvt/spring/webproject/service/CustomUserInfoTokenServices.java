package by.pvt.spring.webproject.service;

import by.pvt.spring.webproject.entities.User;
import by.pvt.spring.webproject.entities.enums.Level;
import by.pvt.spring.webproject.entities.enums.Role;
import by.pvt.spring.webproject.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
public class CustomUserInfoTokenServices implements ResourceServerTokenServices {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected final Log logger = LogFactory.getLog(this.getClass());
    private String userInfoEndpointUrl;
    private String clientId;
    private OAuth2RestOperations restTemplate;
    private String tokenType = "Bearer";
    private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();
    private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

    public void customUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        Assert.notNull(authoritiesExtractor, "AuthoritiesExtractor must not be null");
        this.authoritiesExtractor = authoritiesExtractor;
    }

    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        Assert.notNull(principalExtractor, "PrincipalExtractor must not be null");
        this.principalExtractor = principalExtractor;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {

        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);

        if (map.containsKey("error")) {
            System.out.println("userinfo returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map);
    }

    @Transactional
    public OAuth2Authentication extractAuthentication(Map<String, Object> map) {

        if (userRepo.findByEmail(String.valueOf(map.get("email"))) == null) {
            User user = new User();
            user.setFirst_name(String.valueOf(map.get("given_name")));
            user.setLast_name(String.valueOf(map.get("family_name")));
            user.setActive(true);
            user.setUsername(String.valueOf(map.get("email")));
            user.setPassword(passwordEncoder.encode((CharSequence) map.get("sub")));
            user.setEmail(String.valueOf(map.get("email")));
            user.setActivationCode(null);
            user.setLevels(Level.BEGINNER);
            user.setGender(true);
            user.setRoles(Role.ROLE_USER);
            user.setCountry(String.valueOf(map.get("locale")));
            user.setCity(String.valueOf(map.get("locale")));
            user.setStreet(String.valueOf(map.get("locale")));
            user.setBirthday(new Date(new java.util.Date().getTime()));


            userRepo.save(user);
            if (user.getEmail() != null) {
                if (!StringUtils.isEmpty(user.getEmail())) {
                    String message = String.format(
                            "Hello,%s! \nWelcome to the Team!\nYour Username: %s\nYour Password: %s",
                            user.getFirst_name(), user.getUsername(), map.get("sub")
                    );
                    mailSender.send(user.getEmail(), "Welcome!", message);
                }
            }

            OAuth2Request request = new OAuth2Request((Map) null, this.clientId, (Collection) null, true,
                    (Set) null, (Set) null, (String) null, (Set) null, (Map) null);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                    "N/A", Collections.singleton(new SimpleGrantedAuthority(user.getRoles().name())));

            token.setDetails(map);

            return new OAuth2Authentication(request, token);
        } else {
            User user = userRepo.findByEmail(String.valueOf(map.get("email")));

            OAuth2Request request = new OAuth2Request((Map) null, this.clientId, (Collection) null, true,
                    (Set) null, (Set) null, (String) null, (Set) null, (Map) null);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                    "N/A", Collections.singleton(new SimpleGrantedAuthority(user.getRoles().name())));

            token.setDetails(map);

            return new OAuth2Authentication(request, token);
        }
    }

    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = this.principalExtractor.extractPrincipal(map);
        return principal == null ? "unknown" : principal;
    }

    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private Map<String, Object> getMap(String path, String accessToken) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Getting user info from: " + path);
        }

        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }

            OAuth2AccessToken existingToken = ((OAuth2RestOperations) restTemplate).getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                token.setTokenType(this.tokenType);
                ((OAuth2RestOperations) restTemplate).getOAuth2ClientContext().setAccessToken(token);
            }

            return (Map) ((OAuth2RestOperations) restTemplate).getForEntity(path, Map.class, new Object[0]).getBody();
        } catch (Exception var6) {
            this.logger.warn("Could not fetch user details: " + var6.getClass() + ", " + var6.getMessage());
            return Collections.singletonMap("error", "Could not fetch user details");
        }
    }
}



