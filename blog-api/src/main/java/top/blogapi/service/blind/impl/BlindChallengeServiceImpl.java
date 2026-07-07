package top.blogapi.service.blind.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Category;
import top.blogapi.model.entity.blind.BlindChallenge;
import top.blogapi.model.entity.blind.BlindChallengeGuess;
import top.blogapi.repository.BlindChallengeGuessRepository;
import top.blogapi.repository.BlindChallengeRepository;
import top.blogapi.repository.CategoryRepository;
import top.blogapi.service.blind.BlindChallengeService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai BlindChallengeService quản lý thử thách đoán chủ đề hàng ngày
 * với các tùy chọn xáo trộn, ghi nhận dự đoán và bảng xếp hạng.
 */
public class BlindChallengeServiceImpl implements BlindChallengeService {

    private final BlindChallengeRepository blindChallengeRepository;
    private final BlindChallengeGuessRepository guessRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Override
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
        blindChallengeRepository.insert(challenge);

        return challenge;
    }

    @Override
    public BlindChallenge getChallengeStatus(LocalDate date) {
        return blindChallengeRepository.findByDate(date)
                .orElseThrow(() -> new AppException(ErrorCode.CHALLENGE_NOT_FOUND));
    }

    @Override
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
        guessRepository.insert(guess);

        return guess;
    }

    @Override
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

    @Override
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

    @Override
    public BlindChallengeGuess getCurrentGuess(Long userId) {
        BlindChallenge todayChallenge = getTodayChallenge();
        return guessRepository.findByChallengeIdAndUserId(todayChallenge.getId(), userId)
                .orElse(null);
    }

    @Override
    public int checkAndAwardBonuses() {
        BlindChallenge todayChallenge = getTodayChallenge();
        if (!Boolean.TRUE.equals(todayChallenge.getIsRevealed())) {
            throw new AppException(ErrorCode.CHALLENGE_NOT_REVEALED);
        }
        List<BlindChallengeGuess> correctGuesses = guessRepository.findByChallengeIdAndIsCorrectTrue(todayChallenge.getId());
        return correctGuesses.size();
    }
}
