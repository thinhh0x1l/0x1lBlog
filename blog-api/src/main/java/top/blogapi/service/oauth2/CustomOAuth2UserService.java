package top.blogapi.service.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import top.blogapi.model.entity.OAuth2Account;
import top.blogapi.model.entity.User;
import top.blogapi.repository.OAuth2AccountRepository;
import top.blogapi.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuth2AccountRepository oAuth2AccountRepository;
    private final ObjectMapper objectMapper;
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String providerId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("picture");

        StringBuilder stringBuilder = new StringBuilder();
        for(String key:  attributes.keySet()) {
            Object value = attributes.get(key);
            stringBuilder.append(key).append("=");
            if(value instanceof String) {
                stringBuilder.append((String) value);
            }else if(value instanceof Object[]) {
                stringBuilder.append(Arrays.toString((Object[]) value));
            }else if(value instanceof Collection) {
                stringBuilder.append(value);
            }else{
                stringBuilder.append(value.toString());
            }
            stringBuilder.append("\n");
        }
        log.info(stringBuilder.toString());
        log.info("OAuth2 login: provider={}, email={}, name={}", provider, email, name);

        User user = findOrCreateUser(provider, providerId, email, name, avatarUrl);

        Map<String, Object> enrichedAttributes = new HashMap<>(attributes);
        enrichedAttributes.put("userId", user.getId());
        enrichedAttributes.put("userRole", user.getRole());

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                enrichedAttributes,
                "email"
        );
    }

    private User findOrCreateUser(String provider, String providerId, String email, String name, String avatarUrl) {
        Optional<OAuth2Account> existingLink = oAuth2AccountRepository.findByProvider(provider, providerId);
        if (existingLink.isPresent()) {
            return userRepository.findById(existingLink.get().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found for linked OAuth2 account"));
        }

        if (email != null && !email.isBlank()) {
            java.util.List<OAuth2Account> accountsWithEmail = oAuth2AccountRepository.findByEmail(email);
            if (!accountsWithEmail.isEmpty()) {
                OAuth2Account account = accountsWithEmail.get(0);
                return userRepository.findById(account.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found for email-linked OAuth2 account"));
            }

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                createOAuth2Account(user.getId(), provider, providerId, email, avatarUrl);
                log.info("Linked OAuth2 account {} to existing user {}", providerId, user.getId());
                return user;
            }
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setDisplayName(name != null ? name : email);
        newUser.setAvatarUrl(avatarUrl);
        newUser.setRole("USER");
        newUser.setIsCreator(false);
        newUser.setStatus("ACTIVE");
        userRepository.insert(newUser);

        createOAuth2Account(newUser.getId(), provider, providerId, email, avatarUrl);
        log.info("Created new user {} from OAuth2 login {}", newUser.getId(), providerId);
        return newUser;
    }

    private void createOAuth2Account(Long userId, String provider, String providerId, String email, String avatarUrl) {
        OAuth2Account account = new OAuth2Account();
        account.setUserId(userId);
        account.setProvider(provider);
        account.setProviderId(providerId);
        account.setEmail(email);
        account.setAvatarUrl(avatarUrl);
        account.setRawAttributes("{}");
        oAuth2AccountRepository.insert(account);
    }
}
