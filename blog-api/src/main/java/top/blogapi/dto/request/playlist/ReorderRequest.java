package top.blogapi.dto.request.playlist;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReorderRequest {
    @NotEmpty(message = "Song IDs are required")
    private List<Long> songIds;
}
