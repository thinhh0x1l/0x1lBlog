package top.blogapi.service.profile;

import top.blogapi.dto.profile.ProfileLayoutDTO;
import top.blogapi.dto.profile.WidgetDTO;
import top.blogapi.dto.profile.WidgetReorderRequest;
import top.blogapi.dto.profile.WidgetUpdateRequest;

import java.util.List;

/**
 * Giao diện service tùy chỉnh bố cục hồ sơ, quản lý widget,
 * hiển thị, thứ tự và chuyển đổi chế độ game.
 */
public interface ProfileWidgetService {

    List<WidgetDTO> getWidgets(Long userId);

    WidgetDTO updateWidget(Long userId, WidgetUpdateRequest request);

    WidgetDTO toggleWidget(Long userId, String widgetType);

    List<WidgetDTO> reorderWidgets(Long userId, List<WidgetReorderRequest> orders);

    boolean toggleGameMode(Long userId);

    ProfileLayoutDTO getProfileLayout(Long userId);
}
