package top.blogapi.service;

import top.blogapi.model.entity.Guess;

public interface GuessService {

    Guess getGuessByTokenHash(String rawToken);

    Guess addGuess(String rawToken);

    Long getGuessIdByTokenHash(String rawToken);
}
