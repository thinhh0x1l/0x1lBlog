package top.blogapi.dto.request.moment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class HandleMomentLike {
    Long id; // momentId
    Integer liked;
}
