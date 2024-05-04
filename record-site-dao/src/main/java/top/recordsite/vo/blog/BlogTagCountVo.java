package top.recordsite.vo.blog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.recordsite.entity.blog.BlogTag;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogTagCountVo extends BlogTag implements Serializable {

    private Integer count;
}
