package top.blogapi.user.profile.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.user.profile.domain.service.ProfileWidgetService;
import top.blogapi.user.profile.interfaces.dto.WidgetDTO;
import top.blogapi.user.profile.interfaces.dto.WidgetReorderRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReorderWidgetsCommand {

    private final ProfileWidgetService profileWidgetService;

    @Transactional
    public List<WidgetDTO> execute(Long userId, List<WidgetReorderRequest> orders) {
        return profileWidgetService.reorderWidgets(userId, orders);
    }
}
