package top.recordsite.anime.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.recordsite.anime.IAnimeService;
import top.recordsite.dto.anime.admin.AnimeAddDto;
import top.recordsite.dto.anime.admin.AnimeUpdateDto;
import top.recordsite.dto.anime.common.AnimeListDto;
import top.recordsite.entity.anime.Anime;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.anime.AnimeMapper;
import top.recordsite.security.SecurityUtils;
import top.recordsite.utils.BeanCopyUtils;
import top.recordsite.vo.ListVo;
import top.recordsite.vo.anime.AdminAnimeVo;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2023-10-08
 */
@SuppressWarnings("all")
@Service
//@Transactional
public class AnimeServiceImpl extends ServiceImpl<AnimeMapper, Anime> implements IAnimeService {

    @Autowired
    private AnimeMapper animeMapper;


    public IPage<Anime> getPage(AnimeListDto animeListDto){
        //2.组装查询条件
        LambdaQueryWrapper<Anime> queryWrapper = new LambdaQueryWrapper<>();
        //3.组装筛选条件
        queryWrapper.like(StringUtils.hasText(animeListDto.getName()), Anime::getName, animeListDto.getName());
        queryWrapper.eq(StringUtils.hasText(animeListDto.getType()), Anime::getType, animeListDto.getType());
        queryWrapper.eq(StringUtils.hasText(animeListDto.getStatus()), Anime::getStatus, animeListDto.getStatus());
        queryWrapper.eq(ObjectUtils.allNotNull(animeListDto.getYear()), Anime::getYear, animeListDto.getYear());
        queryWrapper.eq(ObjectUtils.allNotNull(animeListDto.getQuarter()), Anime::getQuarter, animeListDto.getQuarter());
        //这里不用检测用户是否存在，用户id对不对，因为用户必须登录后操作，若直接访问接口或token错的，会被filter拦截
        queryWrapper.eq(Anime::getCreateBy,SecurityUtils.getUserId());

        queryWrapper.select(
                Anime::getId,
                Anime::getName,
                Anime::getType,
                Anime::getYear,
                Anime::getQuarter,
                Anime::getStatus,
                Anime::getPicture,
                Anime::getUrl
        );

        //3.1按照添加顺序倒序
        queryWrapper.orderByDesc(Anime::getId);
        //1.设置分页参数
        IPage<Anime> page = new Page<>(animeListDto.getCurrentPage(), animeListDto.getPageSize(), true);
        //分页查询
        IPage<Anime> animeByPage = animeMapper.selectPage(page, queryWrapper);
        if(animeByPage.getRecords().size()==0){
            throw new BusinessException("暂无番剧收藏");
        }
        return animeByPage;

    }

    @Override
    public ListVo<AdminAnimeVo> getAdminAnimeList(AnimeListDto animeListDto) {

        IPage<Anime> animeByPage= getPage(animeListDto);
        ListVo<AdminAnimeVo> animeListVo = new ListVo<AdminAnimeVo>();
        animeListVo.setList(BeanCopyUtils.copyBeanList(animeByPage.getRecords(), AdminAnimeVo.class));
        animeListVo.setTotal(animeByPage.getTotal());

        return animeListVo;

    }

    @Override
    public void addAnime(AnimeAddDto animeAddDto) {
        Anime anime = BeanCopyUtils.copyBean(animeAddDto, Anime.class);
        anime.setCreateBy(SecurityUtils.getUserId());
        //判断是否存在
        Integer count = animeMapper.bangumiExist(anime, SecurityUtils.getUserId());
        if (count > 0) {
            throw new BusinessException("用户已收藏,不能重复添加");
        }
        animeMapper.insert(anime);

    }

    @Override
    public void updateAnime(AnimeUpdateDto updateDto) {
        LambdaUpdateWrapper<Anime> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Anime::getId,updateDto.getId());
        updateWrapper.eq(Anime::getCreateBy,SecurityUtils.getUserId());
        int update = animeMapper.update(BeanCopyUtils.copyBean(updateDto, Anime.class), updateWrapper);
        if (update==0){
            throw new BusinessException("更新失败");
        }
    }

    /**
     * 逻辑删除
     * @param ids
     */
    @Override
    public void deleteAnimeByIds(List<Integer> ids) {
        LambdaUpdateWrapper<Anime> updateWrapper=new LambdaUpdateWrapper<>();
        updateWrapper.in(Anime::getId,ids);
        updateWrapper.eq(Anime::getCreateBy,SecurityUtils.getUserId());
        updateWrapper.set(Anime::getDelFlag,1);
        int update = animeMapper.update(null, updateWrapper);
        if (update==0){
            throw new BusinessException("删除失败");
        }

    }

    @Override
    public AdminAnimeVo getAnimeById(Integer id) {
        LambdaQueryWrapper<Anime> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Anime::getId,id);
        queryWrapper.eq(Anime::getCreateBy,SecurityUtils.getUserId());
        queryWrapper.select(Anime::getId,Anime::getName,Anime::getType,Anime::getYear,Anime::getQuarter,Anime::getStatus,Anime::getPicture,Anime::getUrl);
        Anime anime = animeMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(anime)){
            throw new BusinessException("暂无番剧收藏");
        }
        return BeanCopyUtils.copyBean(anime, AdminAnimeVo.class);
    }

}
/* //        //1. 判断番剧是否存在
 * //        LambdaQueryWrapper<Anime> queryWrapper = new LambdaQueryWrapper<>();
 * //        queryWrapper.eq(Anime::getPicture, animeAddDto.getPicture());
 * //        Anime anime = animeMapper.selectOne(queryWrapper);
 * //        int animeId;
 * //        if (ObjectUtils.isNotEmpty(anime)) {
 * //            //1.1已存在
 * //            animeId = anime.getId();
 * //        } else {
 * //            //1.2不存在，则先添加番剧
 *         Anime anime = BeanCopyUtils.copyBean(animeAddDto, Anime.class);
 *         Integer count = animeMapper.bangumiExist(anime, SecurityUtils.getUserId());
 *         animeMapper.insert(anime);
 * //        }
 *         //2. 添加番剧与用户的关联表
 *         animeUserService.addAnimeUser(anime.getId());
 */
