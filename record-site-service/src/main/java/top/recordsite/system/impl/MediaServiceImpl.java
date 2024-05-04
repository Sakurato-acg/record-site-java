package top.recordsite.system.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.recordsite.config.OssConfig;
import top.recordsite.entity.system.Media;
import top.recordsite.enums.system.CommonDictionary;
import top.recordsite.enums.system.MediaDictionary;
import top.recordsite.exception.BusinessException;
import top.recordsite.mapper.system.MediaMapper;
import top.recordsite.system.IMediaService;
import top.recordsite.utils.FilesUtil;
import top.recordsite.vo.system.MediaVo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 文件存储表 服务实现类
 * </p>
 *
 * @author lpl
 * @since 2024-01-24
 */
@Slf4j
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements IMediaService {

    @Autowired
    private MediaMapper mediaMapper;
    @Autowired
    private OssConfig ossConfig;

    /**
     * 1. 解压文件
     * 2. 获取md内的图片
     * 3. 查看数据库内是否已存在文件
     * 3.1 若存在，则返回外部连接
     * 3.2 若不存在，则先数据库，再oss
     * 4. 替换md图片
     *
     * @param multipartFile 文件
     * @return 转换过的md内容
     */
    @Override
    public MediaVo uploadMd(MultipartFile multipartFile) {
        MediaVo mediaVo = new MediaVo();
        File zip = null;
        File unzip = null;
        try {
            zip = new File("C:\\Users\\lpl\\eclipse-workspace\\springboot_project\\record-site\\" + IdUtil.getSnowflakeNextIdStr() + ".zip");
            multipartFile.transferTo(zip);

            try {
                unzip = ZipUtil.unzip(zip, StandardCharsets.UTF_8);
            } catch (Exception e) {
                unzip = ZipUtil.unzip(zip, Charset.forName("GBK"));
            }

            //文件列表
            List<File> fileList = FilesUtil.getAllFile(unzip);

            //判断是否存在md文件
            boolean hasMd = false;

            String content = null;

            for (File item : fileList) {
                if (Objects.equals(FileUtil.getType(item), "md")) {
                    hasMd = true;

                    content = FileUtil.readString(item, StandardCharsets.UTF_8);
                    Set<String> pictureList = matchMdPicture(content);

                    for (String picture : pictureList) {
                        for (File file : fileList) {
                            if (picture.contains(file.getName())) {
                                //查看数据库内是否已存在该文件
                                String digestHex = MD5.create().digestHex(file);
                                Media media = mediaMapper.existFileWithReturn(digestHex);

                                if (!ObjectUtils.isEmpty(media) && StringUtils.hasText(media.getUrl())) {
                                    log.error("数据库里有数据");
                                    content = content.replaceAll(picture, media.getUrl());
                                    mediaVo.getFileList().add(media.getId());
                                } else {
                                    String path = getMd5Path(digestHex, file.getName());
                                    String url = upload(path, file.getAbsolutePath());
                                    assert url != null;
                                    //替换url
                                    content = content.replaceAll(picture, url);
                                    //记录
                                    Media insert = new Media()
                                            .setId(IdUtil.getSnowflakeNextIdStr())
                                            .setMd5(digestHex)
                                            .setOriginalName(file.getName())
                                            .setType(new Tika().detect(file))
                                            .setUrl(url)
                                            .setPath(path)//文件目录
                                            .setBucket(ossConfig.getQiniu().getBucket())
                                            .setOss("qiniu")
                                            .setStatus(CommonDictionary.status_able)
                                            .setWay(MediaDictionary.way.get("article"));
                                    mediaMapper.insert(insert);

                                    mediaVo.getFileList().add(insert.getId());

                                }
                                break;
                            }
                        }
                    }

                    break;
                }
            }
            mediaVo.setContent(content);

            if (!hasMd) {
                throw new BusinessException("zip内无md文件");
            }


            System.err.println(mediaVo.getFileList());
            return mediaVo;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("上传失败");
        } finally {
            FileUtil.del(zip);
            FileUtil.del(unzip);
        }
    }

    @Override
    public MediaVo uploadImage(MultipartFile multipartFile,String way) {
        MediaVo mediaVo = new MediaVo();
        try {
            //1. 查数据库里是否已存在
            @Cleanup
            InputStream inputStream = multipartFile.getInputStream();
            String digestHex = MD5.create().digestHex(inputStream);
            Media media = mediaMapper.existFileWithReturn(digestHex);
            if (!ObjectUtils.isEmpty(media) && StringUtils.hasText(media.getUrl())) {
                //1. 已存在
                log.error("已存在图片,不要重复上传");
                mediaVo.setUrl(media.getUrl()).getFileList().add(media.getId());
            } else {
                //2. 不存在,则上传图片
                String path = getMd5Path(digestHex, multipartFile.getOriginalFilename());
                String url = upload(path, multipartFile);
                Media insert = new Media()
                        .setId(IdUtil.getSnowflakeNextIdStr())
                        .setMd5(digestHex)
                        .setOriginalName(multipartFile.getOriginalFilename())
                        .setType(multipartFile.getContentType())
                        .setUrl(url)
                        .setPath(path)//文件目录
                        .setBucket(ossConfig.getQiniu().getBucket())
                        .setOss("qiniu")
                        .setStatus(CommonDictionary.status_able)
                        .setWay(MediaDictionary.way.get(way));
                mediaMapper.insert(insert);
                mediaVo.setUrl(insert.getUrl()).getFileList().add(insert.getId());
            }
            return mediaVo;

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("上传失败");
        }
    }

    private Set<String> matchMdPicture(String content) {
        Set<String> pictureList = new HashSet<>();

        // 定义正则表达式
        Pattern p1 = Pattern.compile("<img\\s+src=\"([^\"}]+)\"", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)", Pattern.CASE_INSENSITIVE);

        //查找匹配项
        Matcher matcher = p1.matcher(content);
        while (matcher.find()) {
            pictureList.add(matcher.group(1));
        }
        matcher = p2.matcher(content);

        while (matcher.find()) {
            pictureList.add(matcher.group(2));
        }
        return pictureList;
    }

    private String getMd5Path(String digestHex, String fileName) {
        String path = String.format(
                "%s/%s/%s",
                digestHex.charAt(0),
                digestHex.substring(1, 3),
                fileName
        );

        return path;
    }


    private String upload(String yunPath, MultipartFile multipartFile) {
        //根据自己的对象空间的地址选（华南）
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        //你7牛云的空间名称
        String upToken = ossConfig.getQiniu().getUploadToken();
        try {
            //开始上传
            /**
             * @param data     上传的数据 byte[]、File、filePath
             * @param key      上传数据保存的文件名
             * @param token    上传凭证
             * @param params   自定义参数，如 params.put("x:foo", "foo")
             * @param mime     指定文件mimetype
             * @param checkCrc 是否验证crc32
             * @return
             * @throws QiniuException
             */
            Response response = uploadManager.put(multipartFile.getInputStream(), yunPath, upToken, null, multipartFile.getContentType());
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
            return ossConfig.getQiniu().getUrl() + yunPath;

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("oss上传失败");
        }
    }

    private String upload(String yunPath, String localPath) {
        //根据自己的对象空间的地址选（华南）
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        //你7牛云的空间名称
        String upToken = ossConfig.getQiniu().getUploadToken();
        try {
            //开始上传
            Response response = uploadManager.put(localPath, yunPath, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
            return ossConfig.getQiniu().getUrl() + yunPath;

        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return null;
    }
}
/*

    String accessKey = ossConfig.getQiniu().getAccessKey();
    String secretKey = ossConfig.getQiniu().getSecretKey();
    String bucket = ossConfig.getQiniu().getBucket();
    String url = ossConfig.getQiniu().getUrl();

    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);*/
