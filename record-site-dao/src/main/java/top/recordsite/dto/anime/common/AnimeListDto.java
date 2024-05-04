package top.recordsite.dto.anime.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AnimeListDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @NotNull(message = "分页数据不能为空")
    @Positive(message = "当前页数要>0")
    private Integer currentPage;

    @NotNull
    @PositiveOrZero
    private Integer pageSize;

}
