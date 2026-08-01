package top.blogapi.gamification.blind.domain.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.content.category.domain.entity.Category;
import top.blogapi.gamification.blind.domain.entity.BlindChallenge;
import top.blogapi.gamification.blind.domain.entity.BlindChallengeGuess;
import top.blogapi.gamification.blind.domain.repository.BlindChallengeGuessRepository;
import top.blogapi.gamification.blind.domain.repository.BlindChallengeRepository;
import top.blogapi.content.category.domain.repository.CategoryRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlindChallengeService {

    private final BlindChallengeRepository blindChallengeRepository;
    private final BlindChallengeGuessRepository guessRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public BlindChallenge getTodayChallenge() {
        LocalDate today = LocalDate.now();
        Optional<BlindChallenge> existing = blindChallengeRepository.findByDate(today);
        if (existing.isPresent()) {
            return existing.get();
        }

        List<Category> categories = categoryRepository.findAllVisible();
        if (categories.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Collections.shuffle(categories);
        Category topic = categories.get(0);

        List<Long> optionIds = new ArrayList<>();
        optionIds.add(topic.getId());

        List<Category> others = categories.stream()
                .filter(c -> !c.getId().equals(topic.getId()))
                .collect(Collectors.toList());
        Collections.shuffle(others);

        int needed = Math.min(9, others.size());
        for (int i = 0; i < needed; i++) {
            optionIds.add(others.get(i).getId());
        }

        Collections.shuffle(optionIds);

        List<Map<String, Object>> optionsList = new ArrayList<>();
        for (Long optId : optionIds) {
            Category cat = categories.stream()
                    .filter(c -> c.getId().equals(optId))
                    .findFirst().orElse(null);
            if (cat == null) continue;
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", cat.getId());
            entry.put("name", cat.getName());
            optionsList.add(entry);
        }

        BlindChallenge challenge = new BlindChallenge();
        challenge.setDate(today);
        challenge.setTopicId(topic.getId());
        challenge.setTopicHint(topic.getName().substring(0, Math.min(2, topic.getName().length())));
        try {
            challenge.setOptions(objectMapper.writeValueAsString(optionsList));
        } catch (Exception e) {
            challenge.setOptions("[]");
        }
        challenge.setIsRevealed(false);
        blindChallengeRepository.save(challenge);

        return challenge;
    }

    public BlindChallenge getChallengeStatus(LocalDate date) {
        return blindChallengeRepository.findByDate(date)
                .orElseThrow(() -> new AppException(ErrorCode.CHALLENGE_NOT_FOUND));
    }

    public BlindChallengeGuess makeGuess(Long userId, Long guessedTopicId) {
        BlindChallenge todayChallenge = getTodayChallenge();

        if (guessRepository.existsByChallengeIdAndUserId(todayChallenge.getId(), userId)) {
            throw new AppException(ErrorCode.ALREADY_GUESSED);
        }

        boolean isCorrect = false;
        if (Boolean.TRUE.equals(todayChallenge.getIsRevealed())) {
            isCorrect = todayChallenge.getTopicId().equals(guessedTopicId);
        }

        BlindChallengeGuess guess = new BlindChallengeGuess();
        guess.setChallengeId(todayChallenge.getId());
        guess.setUserId(userId);
        guess.setGuessedTopicId(guessedTopicId);
        guess.setIsCorrect(todayChallenge.getIsRevealed() ? isCorrect : null);
        guessRepository.save(guess);

        return guess;
    }

    public BlindChallenge revealTopic() {
        BlindChallenge todayChallenge = getTodayChallenge();
        todayChallenge.setIsRevealed(true);
        blindChallengeRepository.reveal(todayChallenge.getId());

        List<BlindChallengeGuess> allGuesses = guessRepository.findByChallengeId(todayChallenge.getId());
        for (BlindChallengeGuess guess : allGuesses) {
            boolean correct = guess.getGuessedTopicId().equals(todayChallenge.getTopicId());
            guessRepository.markCorrect(todayChallenge.getId(), guess.getUserId(), correct);
        }

        return todayChallenge;
    }

    public List<Map<String, Object>> getLeaderboard(LocalDate date) {
        BlindChallenge challenge = blindChallengeRepository.findByDate(date)
                .orElseThrow(() -> new AppException(ErrorCode.CHALLENGE_NOT_FOUND));

        List<BlindChallengeGuess> correctGuesses = guessRepository.findByChallengeIdAndIsCorrectTrue(challenge.getId());
        List<Map<String, Object>> leaderboard = new ArrayList<>();
        for (BlindChallengeGuess g : correctGuesses) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("userId", g.getUserId());
            entry.put("guessedAt", g.getCreatedAt());
            leaderboard.add(entry);
        }
        return leaderboard;
    }

    public BlindChallengeGuess getCurrentGuess(Long userId) {
        BlindChallenge todayChallenge = getTodayChallenge();
        return guessRepository.findByChallengeIdAndUserId(todayChallenge.getId(), userId)
                .orElse(null);
    }

    public int checkAndAwardBonuses() {
        BlindChallenge todayChallenge = getTodayChallenge();
        if (!Boolean.TRUE.equals(todayChallenge.getIsRevealed())) {
            throw new AppException(ErrorCode.CHALLENGE_NOT_REVEALED);
        }
        List<BlindChallengeGuess> correctGuesses = guessRepository.findByChallengeIdAndIsCorrectTrue(todayChallenge.getId());
        return correctGuesses.size();
    }
}
