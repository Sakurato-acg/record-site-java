package top.recordsite.vo.system;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MediaVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String content;

    private List<String> fileList = new ArrayList<>();

    private String url;
}
