package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.constant.CacheNameConstant;
import top.blogapi.model.entity.Guest;
import top.blogapi.repository.GuestRepository;
import top.blogapi.service.GuestService;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class GuestServiceImpl implements GuestService {
    SecureRandom secureRandom = new SecureRandom();
    GuestRepository guestRepository;

    public String convertToTokenHash(String rawToken){
        return DigestUtils.sha256Hex(rawToken);
    }


    @Cacheable(
            cacheNames = CacheNameConstant.GUEST_INFO_BY_TOKEN,
            key = "#rawToken",
            unless = "#result == null"
    )
    @Override
    public Guest getGuestOrCreateByToken(String rawToken) {

        System.out.println(">>> Guest DB");
        String hash = convertToTokenHash(rawToken);
        return guestRepository.getGuessByTokenHash(hash)
                .orElseGet(() -> {
                    try {
                        return addGuest(rawToken);
                    } catch (DataIntegrityViolationException e) {
                        // bị duplicate -> fetch lại
                        return guestRepository.getGuessByTokenHash(hash).orElseThrow();
                    }
                });
    }

    @Override
    public Guest addGuest(String rawToken) {
        Guest newGuess = new Guest(convertToTokenHash(rawToken));
        if(guestRepository.addGuest(newGuess) == 0)
            return null;
        return newGuess;
    }

    @Override
    public Long getGuestIdByTokenHash(String rawToken) {
        return guestRepository.getGuessIdByTokenHash(convertToTokenHash(rawToken));
    }

    @Override
    public Guest createGuess() {
        return addGuest(createGuestToken());
    }

    public String createGuestToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
