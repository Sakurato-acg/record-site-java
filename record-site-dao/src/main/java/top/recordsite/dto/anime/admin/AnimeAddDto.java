package top.recordsite.dto.anime.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AnimeAddDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 番剧名
     */
    @NotNull
    private String name;

    /**
     * 番剧类型
     */
    @NotNull
    private String type;

    /**
     * 番剧年份
     */
    @NotNull
    private Integer year;

    /**
     * 季度
     */
    @NotNull
    private Integer quarter;

    /**
     * 番剧状态
     */
    @NotNull
    private String status;

    /**
     * 封面
     */
    @URL
    @NotNull
    private String picture;

    /**
     * bangumi地址
     */
    @URL
    @NotNull
    private String url;


}
