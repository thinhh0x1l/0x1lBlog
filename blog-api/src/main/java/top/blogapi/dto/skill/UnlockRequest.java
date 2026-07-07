package top.blogapi.dto.skill;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnlockRequest {
    @NotNull(message = "Skill ID is required")
    private Long skillId;
}
