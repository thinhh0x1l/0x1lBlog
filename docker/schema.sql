-- PostgreSQL Schema for 0x1lBlog

-- User table
CREATE TABLE IF NOT EXISTS "user" (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    avatar VARCHAR(500),
    email VARCHAR(255),
    role VARCHAR(50) DEFAULT 'user',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Category table
CREATE TABLE IF NOT EXISTS category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Tag table
CREATE TABLE IF NOT EXISTS tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    color VARCHAR(50)
);

-- Blog table
CREATE TABLE IF NOT EXISTS blog (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content TEXT,
    description TEXT,
    is_published BOOLEAN DEFAULT FALSE,
    is_recommend BOOLEAN DEFAULT FALSE,
    is_appreciation BOOLEAN DEFAULT FALSE,
    is_top BOOLEAN DEFAULT FALSE,
    is_comment_enabled BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    views BIGINT DEFAULT 0,
    words INTEGER DEFAULT 0,
    read_time INTEGER DEFAULT 0,
    music_id VARCHAR(255),
    category_id BIGINT REFERENCES category(id),
    user_id BIGINT REFERENCES "user"(id)
);

-- Blog-Tag relationship table
CREATE TABLE IF NOT EXISTS blog_tag (
    blog_id BIGINT NOT NULL REFERENCES blog(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
    PRIMARY KEY (blog_id, tag_id)
);

-- Comment table
CREATE TABLE IF NOT EXISTS comment (
    id BIGSERIAL PRIMARY KEY,
    nickname VARCHAR(255),
    email VARCHAR(255),
    content TEXT,
    avatar VARCHAR(500),
    website VARCHAR(500),
    ip VARCHAR(50),
    is_published BOOLEAN DEFAULT TRUE,
    is_admin_comment BOOLEAN DEFAULT FALSE,
    page INTEGER DEFAULT 0,
    is_notice BOOLEAN DEFAULT FALSE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    blog_id BIGINT REFERENCES blog(id) ON DELETE CASCADE,
    parent_comment_id BIGINT REFERENCES comment(id) ON DELETE CASCADE,
    guess_id BIGINT,
    is_edited BOOLEAN DEFAULT FALSE
);

-- Moment table
CREATE TABLE IF NOT EXISTS moment (
    id BIGSERIAL PRIMARY KEY,
    content TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_published BOOLEAN DEFAULT FALSE
);

-- Moment-Guest like relationship
CREATE TABLE IF NOT EXISTS moment_guest (
    moment_id BIGINT NOT NULL REFERENCES moment(id) ON DELETE CASCADE,
    guest_id BIGINT NOT NULL,
    PRIMARY KEY (moment_id, guest_id)
);

-- Guest table
CREATE TABLE IF NOT EXISTS guest (
    id BIGSERIAL PRIMARY KEY,
    token_hash VARCHAR(255) UNIQUE NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_seen_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- About table
CREATE TABLE IF NOT EXISTS about (
    id BIGSERIAL PRIMARY KEY,
    name_en VARCHAR(255),
    name_vn VARCHAR(255),
    value TEXT
);

-- Site Setting table
CREATE TABLE IF NOT EXISTS site_setting (
    id BIGSERIAL PRIMARY KEY,
    name_en VARCHAR(255),
    name_vn VARCHAR(255),
    value TEXT,
    type INTEGER
);

-- Visit table
CREATE TABLE IF NOT EXISTS visit (
    id BIGSERIAL PRIMARY KEY,
    guest_id BIGINT,
    ip VARCHAR(50),
    ip_source VARCHAR(255),
    os VARCHAR(255),
    browser VARCHAR(255),
    user_agent TEXT,
    pv INTEGER DEFAULT 0,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PageView table
CREATE TABLE IF NOT EXISTS page_view (
    id BIGSERIAL PRIMARY KEY,
    visit_id BIGINT REFERENCES visit(id) ON DELETE CASCADE,
    page VARCHAR(500),
    referrer VARCHAR(500),
    stay_duration_seconds INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- EventLog table
CREATE TABLE IF NOT EXISTS event_log (
    id BIGSERIAL PRIMARY KEY,
    page_view_id BIGINT,
    uri VARCHAR(500),
    method VARCHAR(10),
    behavior VARCHAR(100),
    source VARCHAR(50),
    content VARCHAR(500),
    status_code INTEGER,
    success BOOLEAN DEFAULT TRUE,
    remark VARCHAR(500),
    param TEXT,
    response_time_ms INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (password: admin123)
INSERT INTO "user" (username, password, nickname, role)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Admin', 'admin')
ON CONFLICT (username) DO NOTHING;

-- Insert default site settings
INSERT INTO site_setting (name_en, name_vn, value, type) VALUES
('blogName', 'Tên blog', 'Thinh0x1l''s Blog', 0),
('author', 'Tác giả', 'Thinh0x1l', 0),
('webTitleSuffix', 'Tiêu đề trang web', 'Think different', 0),
('favicon', 'Favicon', '/img/favicon.ico', 0),
('musicServer', 'Server nhạc', 'zing', 0),
('aboutBackground', 'Ảnh nền about', '', 1),
('introductionAvatar', 'Ảnh đại diện', '', 1),
('introductionName', 'Tên', 'Thinh0x1l', 1),
('introductionRollText', 'Chữ cuộn', 'Developer,Student', 1),
('github', 'GitHub', 'https://github.com/thinhh0x1l', 2),
('email', 'Email', 'thinhh0x1l@gmail.com', 2),
('facebook', 'Facebook', '', 2),
('instagram', 'Instagram', '', 2),
('leetcode', 'LeetCode', '', 2),
('mp3-k', 'ZingMp3 API Key', '{"API_KEY":"","SECRET_KEY":"","VERSION":""}', 3),
('mp3-h', 'ZingMp3 Headers', '{"User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"}', 3)
ON CONFLICT DO NOTHING;

-- Insert default about fields
INSERT INTO about (name_en, name_vn, value) VALUES
('name', 'Tên', 'Thinh0x1l'),
('email', 'Email', 'thinhh0x1l@gmail.com'),
('github', 'GitHub', 'https://github.com/thinhh0x1l'),
('facebook', 'Facebook', ''),
('instagram', 'Instagram', ''),
('leetcode', 'LeetCode', ''),
('favorites', 'Sở thích', 'Coding,Music,Reading'),
('rollText', 'Chữ cuộn', 'Developer,Student')
ON CONFLICT DO NOTHING;
