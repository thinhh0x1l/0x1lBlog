package top.blogapi.user.profile.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.user.profile.domain.service.ProfileWidgetService;
import top.blogapi.user.profile.interfaces.dto.ProfileLayoutDTO;
import top.blogapi.user.profile.interfaces.dto.WidgetDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWidgetsQuery {

    private final ProfileWidgetService profileWidgetService;

    public List<WidgetDTO> execute(Long userId) {
        return profileWidgetService.getWidgets(userId);
    }

    public ProfileLayoutDTO getProfileLayout(Long userId) {
        return profileWidgetService.getProfileLayout(userId);
    }
}
