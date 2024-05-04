package top.recordsite.entity.anime;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lpl
 * @since 2023-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("acg_anime")
@Accessors(chain = true)
public class Anime implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 番剧id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 番剧名
     */
    private String name;

    /**
     * 番剧类型
     */
    private String type;

    /**
     * 番剧年份
     */
    private Integer year;

    /**
     * 季度
     */
    private Integer quarter;

    /**
     * 番剧状态
     */
    private String status;

    /**
     * 封面
     */
    private String picture;

    /**
     * bangumi链接 (包含封面图、简介等信息)
     */
    private String url;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic(value = "0",delval = "1")
    private Integer delFlag;


}
