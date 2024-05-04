package top.recordsite.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.recordsite.entity.system.Media;

/**
 * <p>
 * 文件存储表 Mapper 接口
 * </p>
 *
 * @author lpl
 * @since 2024-01-24
 */
@Mapper
public interface MediaMapper extends BaseMapper<Media> {

    Media existFileWithReturn(@Param("md5") String md5);
}
