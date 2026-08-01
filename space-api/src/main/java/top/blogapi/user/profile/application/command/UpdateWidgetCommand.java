package top.blogapi.user.profile.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.user.profile.domain.service.ProfileWidgetService;
import top.blogapi.user.profile.interfaces.dto.WidgetDTO;
import top.blogapi.user.profile.interfaces.dto.WidgetUpdateRequest;

@Service
@RequiredArgsConstructor
public class UpdateWidgetCommand {

    private final ProfileWidgetService profileWidgetService;

    @Transactional
    public WidgetDTO execute(Long userId, WidgetUpdateRequest request) {
        return profileWidgetService.updateWidget(userId, request);
    }

    @Transactional
    public WidgetDTO toggleWidget(Long userId, String widgetType) {
        return profileWidgetService.toggleWidget(userId, widgetType);
    }

    @Transactional
    public boolean toggleGameMode(Long userId) {
        return profileWidgetService.toggleGameMode(userId);
    }
}
