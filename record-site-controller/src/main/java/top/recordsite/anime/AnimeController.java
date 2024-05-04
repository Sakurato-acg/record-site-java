package top.recordsite.anime;


import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.recordsite.dto.anime.admin.AnimeAddDto;
import top.recordsite.dto.anime.admin.AnimeUpdateDto;
import top.recordsite.dto.anime.common.AnimeListDto;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.Result;
import top.recordsite.vo.anime.AdminAnimeVo;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lpl
 * @since 2023-10-08
 */
@RestController
@RequestMapping("/anime")
@Slf4j
@PreAuthorize("hasAnyAuthority('acg:anime')")
public class AnimeController {

    @Autowired
    private IAnimeService animeService;

    @SystemLog(businessName = "获取番剧列表，分页，带筛选条件")
    @PostMapping("/admin/list")
    public Result getAdminAnimeList(@RequestBody @Validated AnimeListDto animeListDto) {
        ListVo<AdminAnimeVo> animeList = animeService.getAdminAnimeList(animeListDto);

        return Result.success("查询成功", animeList);
    }

    @SystemLog(businessName = "获取番剧列表，分页，带筛选条件")
    @PostMapping("/front/list")
    public Result frontGetAnimeList(@RequestBody @Validated AnimeListDto animeListDto) {

//        List<AdminAnimeVo> animeList = animeService.getAnimeList(animeListDto);

        return Result.success();
//        return Result.success("查询成功", animeList);
    }


    @SystemLog(businessName = "获取番剧By id")
    @GetMapping("/{id}")
    public Result getAnimeById(@PathVariable("id") @NotEmpty Integer id) {
        AdminAnimeVo adminAnimeVo = animeService.getAnimeById(id);
        return Result.success(adminAnimeVo);
    }

    @SystemLog(businessName = "添加番剧")
    @PutMapping()
    public Result addAnime(@RequestBody @Validated AnimeAddDto animeAddDto) {
        log.info("{}", animeAddDto);
        animeService.addAnime(animeAddDto);
        return Result.success("添加成功");
    }

    @SystemLog(businessName = "更新番剧")
    @PostMapping()
    public Result updateAnime(@RequestBody @Validated AnimeUpdateDto updateDto) {
        animeService.updateAnime(updateDto);
        return Result.success("修改成功");
    }

    @SystemLog(businessName = "删除番剧")
    @DeleteMapping("/{id}")
    public Result deleteAnime(@PathVariable("id") @NotEmpty List<Integer> ids) {
        animeService.deleteAnimeByIds(ids);
        return Result.success("删除成功");
    }

}

