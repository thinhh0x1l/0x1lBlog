package top.blogapi.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAgentDTO {
    public static final UserAgentDTO UNKNOWN = getUnknown();

    String os;
    String browser;

    private static UserAgentDTO getUnknown(){
        if(UserAgentDTO.UNKNOWN != null) return UserAgentDTO.UNKNOWN;
        return new UserAgentDTO("Unknown", "Unknown");
    }
}
