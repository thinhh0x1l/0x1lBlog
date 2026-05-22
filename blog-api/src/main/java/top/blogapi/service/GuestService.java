package top.blogapi.service;

import top.blogapi.model.entity.Guest;

public interface GuestService {

    Guest getGuestOrCreateByToken(String rawToken);

    Guest addGuest(String rawToken);

    Long getGuestIdByTokenHash(String rawToken);

    Guest createGuess();

    String createGuestToken();
}
