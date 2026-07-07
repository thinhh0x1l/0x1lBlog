package top.blogapi.service.profile.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.dto.profile.ProfileLayoutDTO;
import top.blogapi.dto.profile.WidgetDTO;
import top.blogapi.dto.profile.WidgetReorderRequest;
import top.blogapi.dto.profile.WidgetUpdateRequest;
import top.blogapi.model.entity.User;
import top.blogapi.model.entity.profile.ProfileWidget;
import top.blogapi.repository.ProfileWidgetRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.profile.ProfileWidgetService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai ProfileWidgetService quản lý widget bố cục hồ sơ,
 * bao gồm chuyển đổi hiển thị, sắp xếp thứ tự và chế độ game.
 */
public class ProfileWidgetServiceImpl implements ProfileWidgetService {

    private static final String AVATAR_BORDER = "AVATAR_BORDER";

    private static final List<String> DEFAULT_WIDGET_TYPES = List.of(
            "AVATAR_BORDER", "BIO", "BLOG_LIST", "BADGE_WALL", "STATS",
            "ROLLTEXT_BANNER", "MUSIC_BOX", "STATUSES"
    );

    private final ProfileWidgetRepository profileWidgetRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<WidgetDTO> getWidgets(Long userId) {
        List<ProfileWidget> widgets = profileWidgetRepository.findByUserIdOrderBySortOrder(userId);
        if (widgets.isEmpty()) {
            widgets = createDefaultWidgets(userId);
        }
        return widgets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WidgetDTO updateWidget(Long userId, WidgetUpdateRequest request) {
        Optional<ProfileWidget> existing = profileWidgetRepository
                .findByUserIdAndWidgetType(userId, request.getWidgetType());

        ProfileWidget widget;
        if (existing.isPresent()) {
            widget = existing.get();
            if (request.getIsVisible() != null) {
                if (AVATAR_BORDER.equals(request.getWidgetType()) && !request.getIsVisible()) {
                    throw new AppException(ErrorCode.INVALID_INPUT);
                }
                widget.setIsVisible(request.getIsVisible());
            }
            if (request.getSortOrder() != null) {
                widget.setSortOrder(request.getSortOrder());
            }
            if (request.getConfig() != null) {
                widget.setConfig(request.getConfig());
            }
            profileWidgetRepository.update(widget);
        } else {
            widget = new ProfileWidget();
            widget.setUserId(userId);
            widget.setWidgetType(request.getWidgetType());
            widget.setIsVisible(request.getIsVisible() != null ? request.getIsVisible() : true);
            widget.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
            widget.setConfig(request.getConfig() != null ? request.getConfig() : "{}");
            profileWidgetRepository.insert(widget);
        }

        return toDTO(widget);
    }

    @Override
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

    @Override
    public List<WidgetDTO> reorderWidgets(Long userId, List<WidgetReorderRequest> orders) {
        Map<String, Integer> orderMap = orders.stream()
                .collect(Collectors.toMap(WidgetReorderRequest::getWidgetType, WidgetReorderRequest::getSortOrder));

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

    @Override
    public boolean toggleGameMode(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean newMode = !Boolean.TRUE.equals(user.getGameMode());
        userRepository.updateGameMode(userId, newMode);
        return newMode;
    }

    @Override
    public ProfileLayoutDTO getProfileLayout(Long userId) {
        List<WidgetDTO> widgets = getWidgets(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ProfileLayoutDTO layout = new ProfileLayoutDTO();
        layout.setWidgets(widgets);
        layout.setGameMode(Boolean.TRUE.equals(user.getGameMode()));
        layout.setProfileLayout(user.getProfileLayout());
        return layout;
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
        WidgetDTO dto = new WidgetDTO();
        dto.setId(widget.getId());
        dto.setWidgetType(widget.getWidgetType());
        dto.setIsVisible(widget.getIsVisible());
        dto.setSortOrder(widget.getSortOrder());
        dto.setConfig(widget.getConfig());
        return dto;
    }
}
