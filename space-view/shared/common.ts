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

export interface SiteInfo {
  webTitleSuffix?: string
  author?: string
  [key: string]: any
}

export interface Introduction {
  avatar: string
  name: string
  leetCode: string
  facebook: string
  instagram: string
  github: string
  email: string
  favorites: string[]
  rollText: string[]
}
