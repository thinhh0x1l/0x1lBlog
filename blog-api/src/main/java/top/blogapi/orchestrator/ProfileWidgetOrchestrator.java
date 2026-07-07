package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.profile.ProfileLayoutDTO;
import top.blogapi.dto.profile.WidgetDTO;
import top.blogapi.dto.profile.WidgetReorderRequest;
import top.blogapi.dto.profile.WidgetUpdateRequest;
import top.blogapi.service.profile.ProfileWidgetService;

import java.util.List;

/**
 * Orchestrates profile widget management: update, toggle, reorder, and game mode switching.
 */
@Component
@RequiredArgsConstructor
public class ProfileWidgetOrchestrator {

    private final ProfileWidgetService profileWidgetService;

    public List<WidgetDTO> getWidgets(Long userId) {
        return profileWidgetService.getWidgets(userId);
    }

    @Transactional
    public WidgetDTO updateWidget(Long userId, WidgetUpdateRequest request) {
        return profileWidgetService.updateWidget(userId, request);
    }

    @Transactional
    public WidgetDTO toggleWidget(Long userId, String widgetType) {
        return profileWidgetService.toggleWidget(userId, widgetType);
    }

    @Transactional
    public List<WidgetDTO> reorderWidgets(Long userId, List<WidgetReorderRequest> orders) {
        return profileWidgetService.reorderWidgets(userId, orders);
    }

    @Transactional
    public boolean toggleGameMode(Long userId) {
        return profileWidgetService.toggleGameMode(userId);
    }

    public ProfileLayoutDTO getProfileLayout(Long userId) {
        return profileWidgetService.getProfileLayout(userId);
    }
}
