export interface Blog {
  id: number
  authorId: number
  authorName?: string
  authorAvatar?: string
  categoryId?: number
  categoryName?: string
  title: string
  slug: string
  content: string
  description?: string
  coverImage?: string
  contentType: string
  status: string
  visibility: string
  views: number
  likeCount: number
  commentCount: number
  bookmarkCount: number
  shareCount: number
  words?: number
  readTime?: number
  publishedAt?: string
  createdAt: string
  updatedAt: string
}

export interface User {
  id: number
  email: string
  displayName: string
  avatarUrl?: string
  bio?: string
  role: string
  isCreator: boolean
  blogCount: number
  followerCount: number
  followingCount: number
}

export interface Comment {
  id: number
  blogId: number
  userId?: number
  parentId?: number
  content: string
  authorName?: string
  authorAvatar?: string
  children?: Comment[]
  createdAt: string
}

export interface Category {
  id: number
  name: string
  slug: string
  icon?: string
  blogCount: number
}

export interface ApiResponse<T> {
  success: boolean
  code?: string
  message?: string
  data: T
}

export interface PageResult<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}
