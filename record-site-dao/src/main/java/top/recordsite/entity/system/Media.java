package top.recordsite.entity.system;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件存储表
 * </p>
 *
 * @author lpl
 * @since 2024-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件雪花id
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * md5值
     */
    private String md5;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件链接
     */
    private String url;

    /**
     * 文件目录
     */
    private String path;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 存储平台
     */
    private String oss;

    /**
     * 状态（0正常，1失效）
     */
    private Integer status;

    private Integer way;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 文章id
     */
    private Integer articleId;



}
