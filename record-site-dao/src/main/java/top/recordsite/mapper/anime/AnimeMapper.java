package top.recordsite.mapper.anime;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.recordsite.entity.anime.Anime;
import top.recordsite.vo.anime.AdminAnimeVo;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2023-10-08
 */
@Mapper
public interface AnimeMapper extends BaseMapper<Anime> {

    public Integer bangumiExist(@Param("anime") Anime anime, @Param("userId") Integer userId);

    @Select("select id,name,type,year,quarter,status,picture,url from acg_anime where create_by=#{id} and del_flag=0")
    public AdminAnimeVo selectById(@Param("id") Integer id);
}
