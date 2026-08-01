package top.blogapi.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Auth
    INVALID_CREDENTIALS("AUTH_001", HttpStatus.UNAUTHORIZED, "Sai tài khoản hoặc mật khẩu"),
    UNAUTHORIZED("AUTH_002", HttpStatus.UNAUTHORIZED, "Bạn cần phải đăng nhập"),
    TOKEN_EXPIRED("AUTH_003", HttpStatus.UNAUTHORIZED, "Access token đã hết hạn"),
    TOKEN_REVOKED("AUTH_004", HttpStatus.UNAUTHORIZED, "Refresh token đã bị thu hồi"),
    TOKEN_INVALID("AUTH_005", HttpStatus.UNAUTHORIZED, "Token không hợp lệ"),
    INVALID_REFRESH_TOKEN("AUTH_006", HttpStatus.UNAUTHORIZED, "Refresh token không hợp lệ hoặc đã hết hạn"),
    OAUTH2_FAILED("AUTH_007", HttpStatus.UNAUTHORIZED, "Xác thực OAuth2 thất bại"),
    EMAIL_EXISTS("AUTH_008", HttpStatus.CONFLICT, "Email đã tồn tại"),
    DUPLICATE_EMAIL("AUTH_009", HttpStatus.CONFLICT, "Email đã tồn tại"),
    USERNAME_EXISTS("AUTH_010", HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại"),
    EMAIL_NOT_VERIFIED("AUTH_011", HttpStatus.FORBIDDEN, "Email chưa được xác thực"),
    ACCOUNT_LOCKED("AUTH_012", HttpStatus.FORBIDDEN, "Tài khoản bị khóa tạm thời"),
    ACCOUNT_BANNED("AUTH_013", HttpStatus.FORBIDDEN, "Tài khoản đã bị khóa"),

    // User
    USER_NOT_FOUND("USER_001", HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    CANNOT_FOLLOW_SELF("USER_002", HttpStatus.BAD_REQUEST, "Không thể tự theo dõi bản thân"),
    ALREADY_FOLLOWED("USER_003", HttpStatus.CONFLICT, "Đã theo dõi người dùng này"),

    // Blog
    BLOG_NOT_FOUND("BLOG_001", HttpStatus.NOT_FOUND, "Không tìm thấy bài viết"),
    BLOG_SLUG_DUPLICATED("BLOG_002", HttpStatus.CONFLICT, "Slug bài viết đã tồn tại"),
    BLOG_MIN_CONTENT("BLOG_003", HttpStatus.BAD_REQUEST, "Nội dung bài viết phải có ít nhất 100 ký tự khi xuất bản"),
    BLOG_MAX_TAGS("BLOG_004", HttpStatus.BAD_REQUEST, "Mỗi bài viết tối đa 5 thẻ"),
    BLOG_CATEGORY_REQUIRED("BLOG_005", HttpStatus.BAD_REQUEST, "Bài viết phải thuộc một danh mục"),
    BLOG_REPRINT_SOURCE_REQUIRED("BLOG_006", HttpStatus.BAD_REQUEST, "Bài viết转载 phải có link nguồn gốc"),
    ALREADY_BOOKMARKED("BLOG_007", HttpStatus.CONFLICT, "Đã đánh dấu bài viết này"),
    BOOKMARK_NOT_FOUND("BLOG_008", HttpStatus.NOT_FOUND, "Bookmark không tồn tại"),

    // Comment
    COMMENT_NOT_FOUND("COMMENT_001", HttpStatus.NOT_FOUND, "Không tìm thấy bình luận"),
    COMMENT_NOT_ALLOWED("COMMENT_002", HttpStatus.BAD_REQUEST, "Không thể bình luận bài viết này"),
    COMMENT_EDIT_WINDOW("COMMENT_003", HttpStatus.BAD_REQUEST, "Chỉ được sửa trong vòng 10 phút sau khi tạo"),
    CONTENT_REQUIRED("COMMENT_004", HttpStatus.BAD_REQUEST, "Nội dung cần phải được điền"),
    MAX_DEPTH_REACHED("COMMENT_005", HttpStatus.BAD_REQUEST, "Vượt quá độ sâu phản hồi tối đa (3 cấp)"),

    // Category
    CATEGORY_NOT_FOUND("CATEGORY_001", HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"),

    // Tag
    TAG_NOT_FOUND("TAG_001", HttpStatus.NOT_FOUND, "Không tìm thấy thẻ"),

    // Series
    SERIES_NOT_FOUND("SERIES_001", HttpStatus.NOT_FOUND, "Không tìm thấy chuỗi bài viết"),

    // Story
    STORY_NOT_FOUND("STORY_001", HttpStatus.NOT_FOUND, "Không tìm thấy story"),
    STORY_DAILY_LIMIT("STORY_002", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn story trong ngày (5)"),

    // Status
    STATUS_NOT_FOUND("STATUS_001", HttpStatus.NOT_FOUND, "Không tìm thấy status"),

    // Notification
    NOTIFICATION_NOT_FOUND("NOTIF_001", HttpStatus.NOT_FOUND, "Không tìm thấy thông báo"),

    // Conversation / Message
    CONVERSATION_NOT_FOUND("CONVO_001", HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện"),
    MESSAGE_NOT_FOUND("CONVO_002", HttpStatus.NOT_FOUND, "Không tìm thấy tin nhắn"),

    // Moment
    MOMENT_NOT_FOUND("MOMENT_001", HttpStatus.NOT_FOUND, "Không tìm thấy khoảnh khắc"),

    // Canvas
    CANVAS_NOT_FOUND("CANVAS_001", HttpStatus.NOT_FOUND, "Không tìm thấy canvas"),
    CANVAS_RATE_LIMITED("CANVAS_002", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn stroke (1/giây)"),

    // Badge
    BADGE_NOT_FOUND("BADGE_001", HttpStatus.NOT_FOUND, "Không tìm thấy huy hiệu"),
    BADGE_ALREADY_AWARDED("BADGE_002", HttpStatus.CONFLICT, "Huy hiệu đã được trao"),

    // Playlist
    PLAYLIST_NOT_FOUND("PLAYLIST_001", HttpStatus.NOT_FOUND, "Không tìm thấy playlist"),
    PLAYLIST_NOT_OWNER("PLAYLIST_002", HttpStatus.FORBIDDEN, "Bạn không phải chủ sở hữu playlist"),
    PLAYLIST_SONG_NOT_FOUND("PLAYLIST_003", HttpStatus.NOT_FOUND, "Không tìm thấy bài hát trong playlist"),
    PLAYLIST_SONG_ALREADY_EXISTS("PLAYLIST_004", HttpStatus.CONFLICT, "Bài hát đã tồn tại trong playlist"),
    PLAYLIST_SONG_LIMIT("PLAYLIST_005", HttpStatus.BAD_REQUEST, "Playlist đã đạt tối đa 50 bài hát"),
    PLAYLIST_DAILY_LIMIT("PLAYLIST_006", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn thêm bài trong ngày (5)"),

    // Widget
    WIDGET_NOT_FOUND("WIDGET_001", HttpStatus.NOT_FOUND, "Không tìm thấy widget"),

    // Site Settings
    SITE_SETTINGS_NOT_FOUND("SETTINGS_001", HttpStatus.NOT_FOUND, "Không tìm thấy cài đặt"),

    // Reaction
    INVALID_REACTION_TYPE("REACTION_001", HttpStatus.BAD_REQUEST, "Loại reaction không hợp lệ"),
    REACTION_SELF_NOT_ALLOWED("REACTION_002", HttpStatus.BAD_REQUEST, "Không thể tương tác với nội dung của chính mình"),

    // Follow
    ALREADY_CHECKED_IN("USER_004", HttpStatus.CONFLICT, "Bạn đã điểm danh hôm nay"),

    // Shop
    ITEM_NOT_FOUND("SHOP_001", HttpStatus.NOT_FOUND, "Không tìm thấy vật phẩm"),
    ITEM_NOT_AVAILABLE("SHOP_002", HttpStatus.BAD_REQUEST, "Vật phẩm không khả dụng"),
    ITEM_SOLD_OUT("SHOP_003", HttpStatus.BAD_REQUEST, "Vật phẩm đã bán hết"),
    INVENTORY_NOT_FOUND("SHOP_004", HttpStatus.NOT_FOUND, "Không tìm thấy vật phẩm trong túi đồ"),
    INSUFFICIENT_COINS("SHOP_005", HttpStatus.BAD_REQUEST, "Không đủ xu"),
    INSUFFICIENT_GEMS("SHOP_006", HttpStatus.BAD_REQUEST, "Không đủ gem"),
    INSUFFICIENT_FUNDS("SHOP_007", HttpStatus.BAD_REQUEST, "Không đủ tiền"),
    INSUFFICIENT_POINT("SHOP_008", HttpStatus.BAD_REQUEST, "Không đủ điểm"),
    INVALID_CURRENCY("SHOP_009", HttpStatus.BAD_REQUEST, "Loại tiền tệ không hợp lệ"),
    OUT_OF_STOCK("SHOP_010", HttpStatus.BAD_REQUEST, "Hết hàng"),
    REPUTATION_NOT_FOUND("SHOP_011", HttpStatus.NOT_FOUND, "Điểm uy tín không tồn tại"),

    // Challenge
    CHALLENGE_NOT_FOUND("CHALLENGE_001", HttpStatus.NOT_FOUND, "Không tìm thấy thử thách"),
    ALREADY_GUESSED("CHALLENGE_002", HttpStatus.CONFLICT, "Bạn đã đoán hôm nay"),
    CHALLENGE_NOT_REVEALED("CHALLENGE_003", HttpStatus.BAD_REQUEST, "Thử thách chưa được mở"),

    // Quest
    QUEST_NOT_FOUND("QUEST_001", HttpStatus.NOT_FOUND, "Không tìm thấy nhiệm vụ"),
    USER_QUEST_NOT_FOUND("QUEST_002", HttpStatus.NOT_FOUND, "Không tìm thấy nhiệm vụ của người dùng"),
    QUEST_ALREADY_ASSIGNED("QUEST_003", HttpStatus.CONFLICT, "Nhiệm vụ đã được giao"),
    QUEST_MAX_LIMIT("QUEST_004", HttpStatus.BAD_REQUEST, "Đã đạt giới hạn nhiệm vụ tối đa"),
    QUEST_NOT_COMPLETED("QUEST_005", HttpStatus.BAD_REQUEST, "Nhiệm vụ chưa hoàn thành"),
    QUEST_EXPIRED("QUEST_006", HttpStatus.BAD_REQUEST, "Nhiệm vụ đã hết hạn"),
    QUEST_ALREADY_CLAIMED("QUEST_007", HttpStatus.CONFLICT, "Nhiệm vụ đã được nhận thưởng"),
    QUEST_TYPE_INVALID("QUEST_008", HttpStatus.BAD_REQUEST, "Loại nhiệm vụ không hợp lệ"),

    // Common
    INVALID_INPUT("COMMON_001", HttpStatus.BAD_REQUEST, "Dữ liệu đầu vào không hợp lệ"),
    VALIDATION_ERROR("COMMON_002", HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ"),
    ABOUT_UPDATE_PARTIAL("COMMON_003", HttpStatus.BAD_REQUEST, "Update thiếu row"),
    NOT_FOUND("COMMON_004", HttpStatus.NOT_FOUND, "Không tìm thấy"),
    DATA_CONFLICT("COMMON_005", HttpStatus.CONFLICT, "Xung đột dữ liệu"),
    FORBIDDEN("COMMON_006", HttpStatus.FORBIDDEN, "Bạn không được phép truy cập"),
    RATE_LIMIT_EXCEEDED("COMMON_007", HttpStatus.TOO_MANY_REQUESTS, "Vượt quá giới hạn yêu cầu"),
    INTERNAL_ERROR("COMMON_999", HttpStatus.INTERNAL_SERVER_ERROR, "Hệ thống gặp sự cố");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
