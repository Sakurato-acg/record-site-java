package top.recordsite.vo.blog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.recordsite.entity.blog.BlogCategory;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BlogCategoryCountVo extends BlogCategory implements Serializable {

    private Integer count;
}
