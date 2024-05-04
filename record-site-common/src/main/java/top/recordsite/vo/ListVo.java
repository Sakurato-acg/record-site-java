package top.recordsite.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ListVo<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> list;

    private Long total;
}
