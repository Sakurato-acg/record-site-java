package top.recordsite.anime;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import top.recordsite.dto.anime.admin.AnimeAddDto;
import top.recordsite.dto.anime.admin.AnimeUpdateDto;
import top.recordsite.dto.anime.common.AnimeListDto;
import top.recordsite.entity.anime.Anime;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.anime.AdminAnimeVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lpl
 * @since 2023-10-08
 */
@Transactional
public interface IAnimeService extends IService<Anime> {

    ListVo<AdminAnimeVo> getAdminAnimeList(AnimeListDto animeListDto);

    void addAnime(AnimeAddDto animeAddDto);

    void updateAnime(AnimeUpdateDto updateDto);

    void deleteAnimeByIds(List<Integer> ids);

    AdminAnimeVo getAnimeById(Integer id);
}
