import {defineStore} from "pinia";
import {ref, watch} from "vue";
import type {CommentNode, CommentQuery, CommentStats, SaveCommentReq} from "@/types/commentType";
import {getCommentListByQuery, submitComment} from "@/api/comment";

import {validateSchema} from "@/util/validateHelper";
import z from "zod";
import type {ApiResponse} from "@/plugins/axios2";
import {toast} from "@/plugins/primevueConfig/primePluginVue";

export interface InfoUser{
    nickname: string,
    email: string,
    website?: string,
}

export const useCommentStore = defineStore('comment',() => {

    // Comment state
    const infoUser = ref<InfoUser>(getStorage<InfoUser>('infoUser',{
        nickname: '',
        email: '',
        website: ''
    }))

    const commentQuery = ref<CommentQuery>({
        page: 0,
        blogId: null,
        pageNum: 1,
        pageSize: 5
    });
    const commentForm = ref<SaveCommentReq>({
        content: '',
        nickname: infoUser.value.nickname,
        email: infoUser.value.email,
        website: infoUser.value.website,
        notice: true,
        blogId: commentQuery.value.blogId,
        parentCommentId: null
    });
    watch(infoUser, (val) => {
        commentForm.value.nickname = val.nickname
        commentForm.value.email = val.email
        commentForm.value.website = val.website
    }, { immediate: true })
    watch(() =>
        [commentForm.value.nickname,
            commentForm.value.website,
            commentForm.value.email
        ],([nickname, website, email]) => {
        setInfoUser({nickname,website,email} as InfoUser)
    })
    const totalPages = ref<number>(0)
    const commentStats = ref<CommentStats>({
        totalComments: 0,
        uniqueCommenters:0
    })
    const threadRoot = ref<number|null>(null)
    const parentNickname = ref<string>('')
    const comments = ref<CommentNode[]>([])


    // Comment actions
    const getCommentList = async () => {
        try {
            const res: any = await getCommentListByQuery(commentQuery.value);
            console.log(res);
            if (res.code === 200) {
                totalPages.value = res.data.comments.pages
                commentStats.value = res.data.commentStats
                comments.value = res.data.comments.list;
            } else
                toast.error(res.msg);
        } catch {
            toast.error("Yêu cầu không thành công");
        }
    };

    const postComment = async () =>{
        try {
            const res: ApiResponse<null> = await submitComment(commentForm.value)
            if(res.code === 200){
                toast.success(res.msg)
                await getCommentList()
                resetFormComment()
            }

        }catch (e){
            toast.error(e)
        }
    }

    const setCommentQueryPage = (page: number) => {
        commentQuery.value.page = page;
    };

    const setCommentQueryBlogId = (blogId: number | null) => {
        commentQuery.value.blogId = blogId;
        commentForm.value.blogId = blogId;
    };

    const resetFormComment = ():void => {
        commentForm.value.parentCommentId=null
        commentForm.value.content=''
        parentNickname.value = ''
        threadRoot.value = null
    }


    const setFieldFromComment = (tR: number| null, pI: number|null, pNN: string) => {
        commentForm.value.parentCommentId=pI
        parentNickname.value = pNN
        threadRoot.value = tR
    }
    const validateAll = () => {
        const { valid, errors } = validateSchema(commentSchema, commentForm.value)
        const firstErrorKey = Object.entries(errors)[0]
        return{
            valid,
            firstErrorKey
        }
    }
    const setCommentQueryPageNum = (pageNum: number) => {
        commentQuery.value.pageNum = pageNum;
    };

    const setParentCommentId = (id: number| null) => {
        commentForm.value.parentCommentId = id;
    };

    const setInfoUser = (info: InfoUser) => {
        infoUser.value = info
        setStorage('infoUser', info)
    }

    const setCommentFormEmpty = () => {
        commentForm.value = {
            content: '',
            nickname: '',
            email: '',
            website: '',
            notice: true,
            blogId: null,
            parentCommentId: null
        };
    };

    const submitCommentForm = async () => {
        try {
            const form = { ...commentForm.value };
            Object.assign(form, {
                page: commentQuery.value.page,
                blogId: commentQuery.value.blogId,
                parentCommentId: commentForm.value.parentCommentId
            });

            const res: any = await submitComment(form);
            console.log(res);

            if (res.code === 200) {
                toast.success(res.msg);

                setParentCommentId(-1);
                resetFormComment()
                await getCommentList();
            } else {
                toast.error(res.msg);
            }
        } catch {
            toast.error('Lỗi ngoại lệ');
        }
    };

    function setStorage<T>(key: string, value: T): void {
        localStorage.setItem(key, JSON.stringify(value))
    }

    function getStorage<T>(key: string, defaultValue: T): T {
        try {
            const stored = localStorage.getItem(key);
            return stored? JSON.parse(stored): defaultValue;
        }catch {
            return defaultValue;
        }
    }
    const commentSchema = z.object({
        content: z.string()
            // .min(1, { message: 'Phải nhập nội dung bình luận' })
            .max(255, { message: 'Nội dung bình luận quá dài!' }),
        nickname: z.string()
            .min(1, { message: 'Không được để trống Nickname' })
            .max(50, { message: 'Nickname quá dài!' }),
        email: z.string()
            .max(100, { message: 'Email quá dài!' })
            .regex(
                /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                { message: 'Email không hợp lệ' }
            )
    });
    return{
        commentQuery,
        infoUser,
        totalPages,
        commentStats,
        comments,
        threadRoot,
        parentNickname,
        commentForm,
        getCommentList,
        setFieldFromComment,
        resetFormComment,
        setInfoUser,
        postComment,
        setCommentQueryPage,
        setCommentQueryBlogId,
        validateAll,
        setCommentQueryPageNum,
        setParentCommentId,
        setCommentFormEmpty,
        submitCommentForm,
    }
})