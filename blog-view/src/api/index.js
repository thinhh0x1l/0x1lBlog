// API layer — mock data cho frontend
import { users, blogs, categories, hashtags, comments, blogReactions, bookmarks, follows, notifications, blogSeries, badges, dailyCheckins } from '@/data/dummy'

const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

// Helper: attach author info to blog
const enrichBlog = (blog) => {
  const author = users.find(u => u.id === blog.authorId)
  const category = categories.find(c => c.id === blog.categoryId)
  return { ...blog, authorName: author?.displayName, authorAvatar: author?.avatarUrl, categoryName: category?.name }
}

export const authApi = {
  login: async (data) => { await delay(); const user = users.find(u => u.username === data.username) || users[0]; return { data: { accessToken: 'mock-token', user } } },
  register: async (data) => { await delay(); return { data: { accessToken: 'mock-token', user: { ...users[0], username: data.username, displayName: data.displayName } } } },
}

export const blogApi = {
  getAll: async (page = 0, size = 10) => {
    await delay();
    const filtered = blogs.filter(b => b.status === 'PUBLISHED');
    const blog1 = filtered.find(b => b.id === 1);
    const rest = filtered.filter(b => b.id !== 1);
    const sorted = blog1 ? [blog1, ...rest] : rest;
    return { data: sorted.slice(page * size, (page + 1) * size).map(enrichBlog) };
  },
  getById: async (id) => {
    await delay();
    const blog = blogs.find(b => b.id === Number(id));
    return { data: blog ? enrichBlog(blog) : null };
  },
  trending: async (limit = 10) => {
    await delay();
    return { data: blogs.filter(b => b.status === 'PUBLISHED').sort((a, b) => b.views - a.views).slice(0, limit).map(enrichBlog) };
  },
  search: async (q) => {
    await delay();
    return { data: blogs.filter(b => b.title.toLowerCase().includes(q?.toLowerCase() || '')).map(enrichBlog) };
  },
  getByAuthor: async (authorId) => {
    await delay();
    return { data: blogs.filter(b => b.authorId === Number(authorId)).map(enrichBlog) };
  },
  incrementView: async () => { await delay(50); return { data: null } },
  create: async (data) => { await delay(); return { data: { id: 999, ...data } } },
  update: async (id, data) => { await delay(); return { data: { id, ...data } } },
  delete: async () => { await delay(); return { data: null } },
  publish: async () => { await delay(); return { data: null } },
}

export const categoryApi = {
  getAll: async () => { await delay(); return { data: categories } },
  getById: async (id) => { await delay(); return { data: categories.find(c => c.id === Number(id)) } },
}

export const hashtagApi = {
  getTop: async (limit = 10) => { await delay(); return { data: [...hashtags].sort((a, b) => b.usageCount - a.usageCount).slice(0, limit) } },
}

export const commentApi = {
  getByBlog: async (blogId) => {
    await delay();
    const list = comments.filter(c => c.blogId === Number(blogId) && !c.parentId);
    return { data: list.map(c => ({
      ...c,
      authorName: users.find(u => u.id === c.userId)?.displayName || c.guestName,
      authorAvatar: users.find(u => u.id === c.userId)?.avatarUrl,
      children: comments.filter(r => r.parentId === c.id).map(r => ({
        ...r,
        authorName: users.find(u => u.id === r.userId)?.displayName || r.guestName,
        authorAvatar: users.find(u => u.id === r.userId)?.avatarUrl,
      }))
    })) };
  },
  create: async (data) => { await delay(); return { data: { id: 999, ...data, createdAt: new Date().toISOString() } } },
  update: async () => { await delay(); return { data: null } },
  delete: async () => { await delay(); return { data: null } },
}

const reactionTypes = ['LIKE', 'LOVE', 'HAHA', 'WOW', 'SAD', 'ANGRY']
export const reactionApi = {
  toggle: async (blogId, type) => { await delay(200); return { data: { blogId, type } } },
  remove: async () => { await delay(); return { data: null } },
  getSummary: async (blogId) => { await delay(); const reactions = blogReactions.filter(r => r.blogId === Number(blogId)); const summary = {}; reactionTypes.forEach(t => summary[t.toLowerCase()] = reactions.filter(r => r.type === t).length); return { data: summary } },
}

export const bookmarkApi = {
  getAll: async () => {
    await delay();
    return { data: bookmarks.map(b => ({ ...b, blog: blogs.find(bl => bl.id === b.blogId) })) };
  },
  toggle: async (data) => { await delay(); return { data: { blogId: data.blogId } } },
  remove: async () => { await delay(); return { data: null } },
  check: async (blogId) => { await delay(); return { data: bookmarks.some(b => b.blogId === Number(blogId)) } },
}

export const followApi = {
  follow: async () => { await delay(); return { data: null } },
  unfollow: async () => { await delay(); return { data: null } },
  check: async () => { await delay(); return { data: false } },
  getFollowers: async (userId) => { await delay(); return { data: follows.filter(f => f.followingId === Number(userId)).map(f => users.find(u => u.id === f.followerId)).filter(Boolean) } },
  getFollowing: async (userId) => { await delay(); return { data: follows.filter(f => f.followerId === Number(userId)).map(f => users.find(u => u.id === f.followingId)).filter(Boolean) } },
}

export const notificationApi = {
  getAll: async () => {
    await delay();
    return { data: notifications.slice(0, 20).map(n => ({
      ...n,
      actorName: users.find(u => u.id === n.actorId)?.displayName || 'Hệ thống',
      actorAvatar: users.find(u => u.id === n.actorId)?.avatarUrl,
    })) };
  },
  getUnreadCount: async () => { await delay(); return { data: notifications.filter(n => !n.isRead).length } },
  markRead: async () => { await delay(); return { data: null } },
  markAllRead: async () => { await delay(); return { data: null } },
}

export const profileApi = {
  getMe: async () => { await delay(); return { data: users[0] } },
  getPublic: async (userId) => { await delay(); return { data: users.find(u => u.id === Number(userId)) || users[0] } },
  updateMe: async (data) => { await delay(); return { data: { ...users[0], ...data } } },
}

export const seriesApi = {
  getById: async (id) => { await delay(); return { data: blogSeries.find(s => s.id === Number(id)) } },
  getByAuthor: async (authorId) => { await delay(); return { data: blogSeries.filter(s => s.authorId === Number(authorId)) } },
  create: async (data) => { await delay(); return { data: { id: 999, ...data } } },
  update: async (id, data) => { await delay(); return { data: { id, ...data } } },
  delete: async () => { await delay(); return { data: null } },
  addBlog: async () => { await delay(); return { data: null } },
  removeBlog: async () => { await delay(); return { data: null } },
}

export const dashboardApi = {
  getStats: async () => { await delay(); return { data: { users: 1247, blogs: 856, views: 128470, comments: 8934 } } },
}
