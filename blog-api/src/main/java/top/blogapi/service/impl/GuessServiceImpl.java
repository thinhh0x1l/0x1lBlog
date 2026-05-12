package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.Guess;
import top.blogapi.repository.GuessRepository;
import top.blogapi.service.GuessService;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class GuessServiceImpl implements GuessService {

    GuessRepository guessRepository;

    public String convertToTokenHash(String rawToken){
        return DigestUtils.sha256Hex(rawToken);
    }

    @Override
    public Guess getGuessByTokenHash(String rawToken) {
        return guessRepository.getGuessByTokenHash(convertToTokenHash(rawToken))
                .orElse(null);
    }

    @Override
    public Guess addGuess(String rawToken) {
        Guess newGuess = new Guess(convertToTokenHash(rawToken));
        if(guessRepository.addGuess(newGuess) == 0)
            return null;
        return newGuess;
    }

    @Override
    public Long getGuessIdByTokenHash(String rawToken) {
        return guessRepository.getGuessIdByTokenHash(convertToTokenHash(rawToken));
    }
}
