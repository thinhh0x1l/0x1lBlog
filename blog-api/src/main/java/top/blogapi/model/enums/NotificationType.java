package top.blogapi.model.enums;

/** Loại sự kiện kích hoạt thông báo cho người dùng. */
public enum NotificationType {
    NEW_COMMENT, NEW_REPLY, NEW_FOLLOWER,
    NEW_BLOG, LIKE_BLOG, LIKE_COMMENT,
    BADGE_AWARD, SERIES_NEW_POST, MENTION,
    TIP_RECEIVED, PAYOUT_STATUS
}
