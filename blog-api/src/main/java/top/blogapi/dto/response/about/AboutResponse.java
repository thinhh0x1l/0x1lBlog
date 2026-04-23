package top.blogapi.dto.response.about;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response._common.MusicInfo;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AboutResponse {
    String title;
    String musicId;
    String content;
    String commentEnabled;
    MusicInfo musicInfo;

    public void setter(String key, String value){
        switch (key) {
            case "title" -> title = value;
            case "musicId" -> musicId = value;
            case "content" -> content = value;
            default -> commentEnabled = value;
        }
    }
}
