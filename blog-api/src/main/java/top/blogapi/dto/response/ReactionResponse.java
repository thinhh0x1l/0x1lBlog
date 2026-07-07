package top.blogapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO phản hồi cho biết người dùng đã tương tác cảm xúc hay chưa và loại cảm xúc.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionResponse {

    private boolean reacted;
    private String type;
}
