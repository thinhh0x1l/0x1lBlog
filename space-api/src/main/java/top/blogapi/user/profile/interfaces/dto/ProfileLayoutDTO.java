package top.blogapi.user.profile.interfaces.dto;

import java.util.List;

public record ProfileLayoutDTO(
        List<WidgetDTO> widgets,
        Boolean gameMode,
        String profileLayout
) {}
