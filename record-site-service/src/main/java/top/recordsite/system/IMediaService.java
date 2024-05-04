package top.recordsite.system;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.recordsite.entity.system.Media;
import top.recordsite.vo.system.MediaVo;

/**
 * <p>
 * 文件存储表 服务类
 * </p>
 *
 * @author lpl
 * @since 2024-01-24
 */
public interface IMediaService extends IService<Media> {

    MediaVo uploadMd(MultipartFile multipartFile);

    MediaVo uploadImage(MultipartFile multipartFile,String way);
}
