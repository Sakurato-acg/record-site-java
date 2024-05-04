package top.recordsite.system;

import com.info.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.recordsite.enums.ContentType;
import top.recordsite.vo.AppHttpCodeEnum;
import top.recordsite.vo.Result;
import top.recordsite.vo.system.MediaVo;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private IMediaService mediaService;

    @SystemLog(businessName = "博客文章导入")
    @PreAuthorize("hasAnyAuthority('blog:edit')")
    @PostMapping("/upload/md")
    public Result uploadMd(@RequestParam @NotNull MultipartFile multipartFile) {
        // 判断上传的是zip
        String contentType = multipartFile.getContentType();
        if (!ContentType.zip.equals(contentType)) {
            return Result.error(AppHttpCodeEnum.BOYMISSING).setMsg("必须为zip文件");
        }

        MediaVo mediaVo = mediaService.uploadMd(multipartFile);

        return Result.success("上传成功", mediaVo);
    }

    @SystemLog(businessName = "图片上传导入")
    @PreAuthorize("hasAnyAuthority('blog:edit')")
    @PostMapping("/upload/image/{way}")
    public Result uploadImage(@RequestParam @NotNull MultipartFile multipartFile, @PathVariable("way") String way) {
        if (!ContentType.isImage(Objects.requireNonNull(multipartFile.getContentType()))) {
            return Result.error(AppHttpCodeEnum.BOYMISSING).setMsg("必须为图片");
        }
        MediaVo mediaVo = mediaService.uploadImage(multipartFile,way);

        return Result.success("上传成功", mediaVo);
    }

}
