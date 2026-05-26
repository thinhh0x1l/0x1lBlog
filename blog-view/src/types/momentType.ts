export interface Moment {
    id: number;
    content: string;
    createTime: string;
    likes: number
    published: boolean;
}

export interface MomentLikedByGuestId{
    id: number;
    content: string;
    createTime: string;
    likes: number
    liked: boolean;
}


export interface MomentLikeById{
    id: number,
    liked: number,
}