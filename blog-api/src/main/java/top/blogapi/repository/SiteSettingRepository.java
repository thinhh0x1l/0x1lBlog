package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.blogapi.model.entity.SiteSetting;

import java.util.List;

@Mapper
@Repository
public interface SiteSettingRepository {
    @Select("SELECT * FROM site_setting")
    List<SiteSetting> getList();

    @Update("""
        <script>
            UPDATE site_setting
            SET value = CASE id
            <foreach collection="list" item="item">
                WHEN #{item.id} THEN #{item.value}
            </foreach>
            END
            WHERE id IN
            <foreach collection="list" item="item" open="(" separator="," close=")">
                #{item.id}
            </foreach>
        </script>
""")
    int updateAll(@Param("list") List<SiteSetting> list);

    @Delete("""
    <script>
        DELETE FROM site_setting
        WHERE id IN
        <foreach collection="ids"
                 item="id"
                 open="("
                 separator=","
                 close=")">
            #{id}
        </foreach>
    </script>
""")
    int deleteBatch(@Param("ids") List<Long> ids);

    @Insert("""
        <script>
            INSERT INTO site_setting (name_en, name_vn, value, type)
            VALUES
            <foreach collection="list" item="item" separator=",">
                (
                    #{item.nameEn},
                    #{item.nameVn},
                    #{item.value},
                    #{item.type}
                )
            </foreach>
        </script>
""")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveBatch(List<SiteSetting> list);

    @Select("""
        SELECT *
        FROM site_setting
        WHERE type = #{type}
""")
    List<SiteSetting> mp3Setting(int type);
}
