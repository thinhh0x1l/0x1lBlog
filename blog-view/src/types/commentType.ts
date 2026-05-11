import type {PageResult} from '@/types/commonType'

export interface SaveCommentReq{
    content: string,
    nickname: string,
    email: string,
    notice: boolean,
    blogId: number | null,
    page: number // 0-1
    website?: string,
    parentCommentId: number | null
}

export interface CommentQuery {
    page: number;
    blogId: number | null;
    pageNum: number;
    pageSize: number;
}

// CommentNode
export interface CommentNode {
    id: number
    nickname: string
    content: string
    avatar: string
    createTime: string
    adminComment: boolean
    reply: string
    threadRoot: number
    website: string
    replyComment: CommentNode[]
}

// CommentStats


// Main response
export interface CommentByBlogIdResponse {
    comments: PageResult<CommentNode>
}