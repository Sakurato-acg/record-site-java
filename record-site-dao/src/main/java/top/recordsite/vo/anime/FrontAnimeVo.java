package top.recordsite.vo.anime;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FrontAnimeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 番剧id
     */
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


}
