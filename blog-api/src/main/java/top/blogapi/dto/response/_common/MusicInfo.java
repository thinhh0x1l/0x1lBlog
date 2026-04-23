package top.blogapi.dto.response._common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class MusicInfo{
    String name; // tên bài hát
    String lrc;  // lyric
    String artist; // Tên ca sĩ
    String url; // media
    String theme;
    String cover; // Ảnh bìa
}
