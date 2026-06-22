<template>
  <div class="about-page">
    <h1>Về tôi</h1>
    <div class="about-content">
      <p>Chào mừng đến với 0x1lBlog — nền tảng blog chia sẻ kiến thức lập trình.</p>
      <h2>Tính năng chính</h2>
      <ul>
        <li>Viết blog với Markdown editor</li>
        <li>Hệ thống reaction 6 loại (Like, Love, Haha, Wow, Sad, Angry)</li>
        <li>Bình luận 2 cấp với threading</li>
        <li>Follow tác giả và xem feed cá nhân hóa</li>
        <li>Series — gom các bài viết liên quan</li>
        <li>Hệ thống badge và xếp hạng</li>
        <li>Điểm danh hàng ngày</li>
        <li>Bookmark bài viết yêu thích</li>
      </ul>
      <h2>Công nghệ</h2>
      <p>Backend: Spring Boot + PostgreSQL + Redis + MinIO</p>
      <p>Frontend: Vue 3 + Element Plus</p>
    </div>
  </div>
</template>

<style scoped lang="scss">
.about-page { max-width: 700px; margin: 0 auto; }
.about-page h1 { font-size: 1.5rem; font-weight: 700; margin-bottom: var(--space-lg); }
.about-content { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); padding: var(--space-xl); }
.about-content h2 { font-size: 1.1rem; font-weight: 700; margin: var(--space-lg) 0 var(--space-sm); }
.about-content p { color: var(--text-secondary); line-height: 1.7; margin-bottom: var(--space-sm); }
.about-content ul { padding-left: 20px; color: var(--text-secondary); line-height: 1.8; }
</style>
