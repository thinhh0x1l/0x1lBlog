package top.blogapi.service.quest.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.Quest;
import top.blogapi.model.entity.UserQuest;
import top.blogapi.repository.QuestRepository;
import top.blogapi.repository.UserQuestRepository;
import top.blogapi.service.quest.QuestService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai QuestService xử lý gán nhiệm vụ, theo dõi tiến độ
 * qua khớp hành động, nhận thưởng và tự động gán nhiệm vụ hàng ngày/tuần.
 */
public class QuestServiceImpl implements QuestService {

    private final QuestRepository questRepository;
    private final UserQuestRepository userQuestRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<Quest> getActiveQuests() {
        return questRepository.findActiveQuests();
    }

    @Override
    public List<UserQuest> getUserQuests(Long userId) {
        return userQuestRepository.findByUserId(userId);
    }

    @Override
    public UserQuest assignQuest(Long userId, Long questId) {
        Quest quest = questRepository.findById(questId)
                .orElseThrow(() -> new AppException(ErrorCode.QUEST_NOT_FOUND));

        if (!quest.getIsActive()) {
            throw new AppException(ErrorCode.QUEST_NOT_FOUND);
        }

        Optional<UserQuest> existing = userQuestRepository.findActiveByUserIdAndQuestId(userId, questId);
        if (existing.isPresent()) {
            throw new AppException(ErrorCode.QUEST_ALREADY_ASSIGNED);
        }

        String type = quest.getType();
        int limit = getQuestLimit(type);
        if (limit > 0) {
            int activeCount = userQuestRepository.countActiveByType(userId, type);
            if (activeCount >= limit) {
                throw new AppException(ErrorCode.QUEST_MAX_LIMIT);
            }
        }

        int target = parseTargetFromConditions(quest.getConditions());

        OffsetDateTime expiresAt = calculateExpiresAt(type);
        if (expiresAt == null && "MILESTONE".equals(type)) {
            expiresAt = OffsetDateTime.now().plusYears(100);
        }

        UserQuest userQuest = new UserQuest();
        userQuest.setUserId(userId);
        userQuest.setQuestId(questId);
        userQuest.setProgress(0);
        userQuest.setTarget(target);
        userQuest.setExpiresAt(expiresAt);
        userQuestRepository.insert(userQuest);

        return userQuest;
    }

    @Override
    public UserQuest updateProgress(Long userId, String action) {
        List<UserQuest> activeQuests = userQuestRepository.findActiveQuests(userId);

        for (UserQuest uq : activeQuests) {
            Quest quest = questRepository.findById(uq.getQuestId()).orElse(null);
            if (quest == null) continue;

            String questAction = parseActionFromConditions(quest.getConditions());
            if (!action.equals(questAction)) continue;

            uq.setProgress(uq.getProgress() + 1);
            if (uq.getProgress() >= uq.getTarget()) {
                uq.setStatus("COMPLETED");
                if ("MILESTONE".equals(quest.getType())) {
                    uq.setStatus("CLAIMED");
                    uq.setClaimedAt(OffsetDateTime.now());
                    applyRewards(userId, quest.getRewards());
                }
            }
            userQuestRepository.update(uq);
            return uq;
        }

        return null;
    }

    @Override
    public UserQuest claimReward(Long userId, Long userQuestId) {
        UserQuest userQuest = userQuestRepository.findById(userQuestId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_QUEST_NOT_FOUND));

        if (!userQuest.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        if ("CLAIMED".equals(userQuest.getStatus())) {
            throw new AppException(ErrorCode.QUEST_ALREADY_CLAIMED);
        }

        if (!"COMPLETED".equals(userQuest.getStatus())) {
            throw new AppException(ErrorCode.QUEST_NOT_COMPLETED);
        }

        if (userQuest.getExpiresAt() != null && userQuest.getExpiresAt().isBefore(OffsetDateTime.now())) {
            userQuest.setStatus("EXPIRED");
            userQuestRepository.update(userQuest);
            throw new AppException(ErrorCode.QUEST_EXPIRED);
        }

        Quest quest = questRepository.findById(userQuest.getQuestId())
                .orElseThrow(() -> new AppException(ErrorCode.QUEST_NOT_FOUND));

        applyRewards(userId, quest.getRewards());

        userQuest.setStatus("CLAIMED");
        userQuest.setClaimedAt(OffsetDateTime.now());
        userQuestRepository.update(userQuest);

        return userQuest;
    }

    @Override
    public List<UserQuest> autoAssignQuests(Long userId) {
        List<UserQuest> assigned = new ArrayList<>();
        List<String> types = List.of("DAILY", "WEEKLY");

        for (String type : types) {
            List<Quest> quests = questRepository.findByType(type);
            int limit = getQuestLimit(type);
            int activeCount = userQuestRepository.countActiveByType(userId, type);

            for (Quest quest : quests) {
                if (activeCount >= limit) break;

                Optional<UserQuest> existing = userQuestRepository.findActiveByUserIdAndQuestId(userId, quest.getId());
                if (existing.isPresent()) continue;

                int target = parseTargetFromConditions(quest.getConditions());
                OffsetDateTime expiresAt = calculateExpiresAt(type);

                UserQuest uq = new UserQuest();
                uq.setUserId(userId);
                uq.setQuestId(quest.getId());
                uq.setProgress(0);
                uq.setTarget(target);
                uq.setExpiresAt(expiresAt);
                userQuestRepository.insert(uq);
                assigned.add(uq);
                activeCount++;
            }
        }

        return assigned;
    }

    private int parseTargetFromConditions(String conditionsJson) {
        try {
            JsonNode node = objectMapper.readTree(conditionsJson);
            JsonNode count = node.get("count");
            return count != null ? count.asInt(1) : 1;
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse conditions JSON: {}", conditionsJson, e);
            return 1;
        }
    }

    private String parseActionFromConditions(String conditionsJson) {
        try {
            JsonNode node = objectMapper.readTree(conditionsJson);
            JsonNode action = node.get("action");
            return action != null ? action.asText() : "";
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse conditions JSON: {}", conditionsJson, e);
            return "";
        }
    }

    private void applyRewards(Long userId, String rewardsJson) {
        try {
            JsonNode node = objectMapper.readTree(rewardsJson);
            log.info("Applying rewards to user {}: exp={}, coins={}, gems={}, item_id={}",
                    userId,
                    node.get("exp"),
                    node.get("coins"),
                    node.get("gems"),
                    node.get("item_id"));
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse rewards JSON: {}", rewardsJson, e);
        }
    }

    private OffsetDateTime calculateExpiresAt(String type) {
        LocalDate today = LocalDate.now();
        return switch (type) {
            case "DAILY" -> today.atTime(LocalTime.MAX).atZone(java.time.ZoneId.systemDefault()).toOffsetDateTime();
            case "WEEKLY" -> today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                    .atTime(LocalTime.MAX).atZone(java.time.ZoneId.systemDefault()).toOffsetDateTime();
            case "SEASON" -> today.plusDays(30)
                    .atTime(LocalTime.MAX).atZone(java.time.ZoneId.systemDefault()).toOffsetDateTime();
            default -> null;
        };
    }

    private int getQuestLimit(String type) {
        return switch (type) {
            case "DAILY" -> 3;
            case "WEEKLY" -> 5;
            case "SEASON" -> 1;
            default -> 0;
        };
    }
}
