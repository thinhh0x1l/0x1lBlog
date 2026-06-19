package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog extends BaseEntity {
    Long authorId;
    Long categoryId;
    String title;
    String slug;
    String content;
    String description;
    String coverImage;
    String contentType;
    String locationName;
    BigDecimal latitude;
    BigDecimal longitude;
    String status;
    String visibility;
    Integer price;
    Boolean isTop;
    Boolean isRecommend;
    Boolean allowComments;
    Integer words;
    Integer readTime;
    Integer views;
    Integer likeCount;
    Integer loveCount;
    Integer hahaCount;
    Integer wowCount;
    Integer sadCount;
    Integer angryCount;
    Integer commentCount;
    Integer bookmarkCount;
    Integer shareCount;
    OffsetDateTime publishedAt;
    OffsetDateTime lastCommentedAt;
}
