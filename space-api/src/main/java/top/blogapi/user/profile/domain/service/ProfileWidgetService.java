package top.blogapi.user.profile.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.user.profile.interfaces.dto.ProfileLayoutDTO;
import top.blogapi.user.profile.interfaces.dto.WidgetDTO;
import top.blogapi.user.profile.interfaces.dto.WidgetReorderRequest;
import top.blogapi.user.profile.interfaces.dto.WidgetUpdateRequest;
import top.blogapi.user.core.entity.User;
import top.blogapi.user.profile.domain.entity.ProfileWidget;
import top.blogapi.user.profile.domain.repository.ProfileWidgetRepository;
import top.blogapi.user.core.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileWidgetService {

    private static final String AVATAR_BORDER = "AVATAR_BORDER";

    private static final List<String> DEFAULT_WIDGET_TYPES = List.of(
            "AVATAR_BORDER", "BIO", "BLOG_LIST", "BADGE_WALL", "STATS",
            "ROLLTEXT_BANNER", "MUSIC_BOX", "STATUSES"
    );

    private final ProfileWidgetRepository profileWidgetRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public List<WidgetDTO> getWidgets(Long userId) {
        List<ProfileWidget> widgets = profileWidgetRepository.findByUserIdOrderBySortOrder(userId);
        if (widgets.isEmpty()) {
            widgets = createDefaultWidgets(userId);
        }
        return widgets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public WidgetDTO updateWidget(Long userId, WidgetUpdateRequest request) {
        Optional<ProfileWidget> existing = profileWidgetRepository
                .findByUserIdAndWidgetType(userId, request.widgetType());

        ProfileWidget widget;
        if (existing.isPresent()) {
            widget = existing.get();
            if (request.isVisible() != null) {
                if (AVATAR_BORDER.equals(request.widgetType()) && !request.isVisible()) {
                    throw new AppException(ErrorCode.INVALID_INPUT);
                }
                widget.setIsVisible(request.isVisible());
            }
            if (request.sortOrder() != null) {
                widget.setSortOrder(request.sortOrder());
            }
            if (request.config() != null) {
                widget.setConfig(request.config());
            }
            profileWidgetRepository.update(widget);
        } else {
            widget = new ProfileWidget();
            widget.setUserId(userId);
            widget.setWidgetType(request.widgetType());
            widget.setIsVisible(request.isVisible() != null ? request.isVisible() : true);
            widget.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
            widget.setConfig(request.config() != null ? request.config() : "{}");
            profileWidgetRepository.insert(widget);
        }

        return toDTO(widget);
    }

    public WidgetDTO toggleWidget(Long userId, String widgetType) {
        if (AVATAR_BORDER.equals(widgetType)) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        ProfileWidget widget = profileWidgetRepository
                .findByUserIdAndWidgetType(userId, widgetType)
                .orElseThrow(() -> new AppException(ErrorCode.WIDGET_NOT_FOUND));

        widget.setIsVisible(!widget.getIsVisible());
        profileWidgetRepository.update(widget);

        return toDTO(widget);
    }

    public List<WidgetDTO> reorderWidgets(Long userId, List<WidgetReorderRequest> orders) {
        Map<String, Integer> orderMap = orders.stream()
                .collect(Collectors.toMap(WidgetReorderRequest::widgetType, WidgetReorderRequest::sortOrder));

        List<ProfileWidget> widgets = profileWidgetRepository.findByUserIdOrderBySortOrder(userId);
        for (ProfileWidget widget : widgets) {
            Integer newOrder = orderMap.get(widget.getWidgetType());
            if (newOrder != null) {
                widget.setSortOrder(newOrder);
                profileWidgetRepository.updateSortOrder(widget.getId(), newOrder);
            }
        }

        return profileWidgetRepository.findByUserIdOrderBySortOrder(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean toggleGameMode(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean newMode = !Boolean.TRUE.equals(user.getGameMode());
        userRepository.updateGameMode(userId, newMode);
        return newMode;
    }

    public ProfileLayoutDTO getProfileLayout(Long userId) {
        List<WidgetDTO> widgets = getWidgets(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return new ProfileLayoutDTO(widgets, Boolean.TRUE.equals(user.getGameMode()), user.getProfileLayout());
    }

    private List<ProfileWidget> createDefaultWidgets(Long userId) {
        List<ProfileWidget> widgets = DEFAULT_WIDGET_TYPES.stream()
                .map(type -> {
                    ProfileWidget w = new ProfileWidget();
                    w.setUserId(userId);
                    w.setWidgetType(type);
                    w.setIsVisible(true);
                    w.setSortOrder(DEFAULT_WIDGET_TYPES.indexOf(type));
                    w.setConfig("{}");
                    profileWidgetRepository.insert(w);
                    return w;
                })
                .collect(Collectors.toList());
        return widgets;
    }

    private WidgetDTO toDTO(ProfileWidget widget) {
        return new WidgetDTO(
                widget.getId(),
                widget.getWidgetType(),
                widget.getIsVisible(),
                widget.getSortOrder(),
                widget.getConfig()
        );
    }
}
