package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.dto.internal.CommentTreeInternal;
import top.blogapi.model.entity.Comment;

import java.util.List;

@Mapper
@Repository
public interface CommentRepository {

    @Select("""
        <script>
            SELECT
            c.id,
            c.nickname,
            c.email,
            c.content,
            c.avatar,
            c.create_time,
            c.website,
            c.ip,
            c.is_published,
            c.is_admin_comment,
            c.page,
            c.is_notice,
            c.blog_id,
            c.parent_comment_id,
            b.title as blog_title
            FROM comment c
            LEFT JOIN blog b ON c.blog_id=b.id
            where c.page=#{page}
                <if test="page==0 and blogId!=null">AND c.blog_id=#{blogId}</if>
                <if test="parentCommentId!=null">AND c.parent_comment_id=#{parentCommentId}</if>
                <if test="parentCommentId==null">AND c.parent_comment_id IS NULL</if>
       
        </script>
""")
    @Results({
            @Result(property = "published" , column = "is_published"),
            @Result(property = "adminComment" , column = "is_admin_comment"),
            @Result(property = "notice" , column = "is_notice"),
            @Result(property = "blog.id", column = "blog_id"),
            @Result(property = "blog.title", column = "blog_title"),
    })
    List<Comment> getListByPageAndParentCommentId(Integer page, Long parentCommentId, Long blogId);

    @Select("select * from comment where parent_comment_id is null")
    @Results({
            @Result(property = "published" , column = "is_published"),
            @Result(property = "adminComment" , column = "is_admin_comment"),
            @Result(property = "notice" , column = "is_notice")
    })
    List<Comment> getParentComment();

    @Update("UPDATE comment SET is_published = #{published} WHERE id = #{id}")
    int updateCommentPublishedById(Long id, boolean published);

    @Update("UPDATE comment SET is_notice = #{notice} WHERE id = #{id}")
    int updateCommentNoticeById(Long id, boolean notice);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    int deleteCommentById(Long id);

    @Update("""
        UPDATE comment SET nickname = #{nickname},
                           email = #{email},
                           content = #{content},
                           website = #{website},
                           ip = #{ip} WHERE id =#{id}
""")
    int updateComment(Long id,String nickname, String email, String content, String ip);

    @Select("""
            <script>
            WITH RECURSIVE comment_tree AS (
                SELECT
                    id, nickname, content, avatar, create_time, is_admin_comment, parent_comment_id,
                    id AS thread_root,
                    1 AS depth
                FROM comment
                WHERE parent_comment_id IS NULL AND page = #{page} AND is_published
                <if test="page==0 and blogId!=null">
                    AND blog_id=#{blogId}
                </if>
                UNION ALL
                SELECT
                    c.id, c.nickname, c.content, c.avatar, c.create_time, c.is_admin_comment, c.parent_comment_id,
                    ct.thread_root,
                    ct.depth + 1
                FROM comment c
                INNER JOIN comment_tree ct
                ON c.parent_comment_id = ct.id
            )
            SELECT * FROM comment_tree
            </script>
""")
    @Results({
            @Result(property = "published" , column = "is_published"),
            @Result(property = "adminComment" , column = "is_admin_comment")
    })
    List<CommentTreeInternal> commentTreeFlat(Long blogId, Integer page);

    @Select("""
    <script>
        SELECT
            id, nickname, content, avatar, create_time, is_admin_comment, parent_comment_id, website, guess_id, is_edited,
            id AS thread_root,
            1 AS depth,
            CAST('' AS char(100)) as reply
        FROM comment
        WHERE parent_comment_id IS NULL
            AND page = #{page}
            AND is_published
            <if test="page==0 and blogId!=null">
                AND blog_id=#{blogId}
            </if>
    </script>
""")
    @Results({
            @Result(property = "adminComment" , column = "is_admin_comment")
    })
    List<CommentTreeInternal> findRootComments(@Param("blogId") Long blogId, @Param("page") Integer page);

    @Select("""
    <script>
        WITH RECURSIVE comment_tree AS (
            SELECT
                id, nickname, content, avatar, create_time, is_admin_comment, parent_comment_id, website, guess_id, is_edited,
                id AS thread_root,
                1 AS depth,
                CAST('' AS char(100)) as reply
            FROM comment
            WHERE id IN
            <foreach item="rootId" collection="rootIds" open="(" separator="," close=")">
                #{rootId}
            </foreach>
            UNION ALL
            SELECT
                c.id, c.nickname, c.content, c.avatar, c.create_time, c.is_admin_comment, c.parent_comment_id, c.website, c.guess_id, c.is_edited,
                ct.thread_root,
                ct.depth + 1,
                CONCAT('@',ct.nickname) as reply
            FROM comment c
            INNER JOIN  comment_tree ct
            ON c.parent_comment_id = ct.id
        )
        SELECT * FROM comment_tree ORDER BY id
    </script>
""")
    @Results({
            @Result(property = "adminComment" , column = "is_admin_comment")
    })
    List<CommentTreeInternal> findRepliesByRootIds(List<Long> rootIds);

    @Insert("""
        INSERT INTO comment (
            nickname,
            email,
            content,
            avatar,
            create_time,
            website,
            ip,
            is_published,
            is_admin_comment,
            page,
            is_notice,
            parent_comment_id,
            blog_id,
            update_at,
            is_edited,
            guess_id
        )values(
            #{nickname},
            #{email},
            #{content},
            #{avatar},
            #{createTime},
            #{website},
            #{ip},
            #{published},
            #{adminComment},
            #{page},
            #{notice},
            #{parentCommentId},
            #{blog.id},
            #{updateAt},
            #{isEdited},
            #{guessId}
        )
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveComment (Comment comment);


    @Update("""
        UPDATE comment SET content = #{content},
                           is_edited = true,
                           update_at = NOW()
                           WHERE id =#{id}
""")
    int editComment(Long id, String content);

    @Select("""
        SELECT COUNT(*) FROM comment
""")
    int totalComments();
}
