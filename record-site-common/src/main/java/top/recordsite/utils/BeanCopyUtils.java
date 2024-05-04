package top.recordsite.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BeanCopyUtils {
    private BeanCopyUtils() {

    }

    public static <V> V copyBean(Object source, Class<V> vClass) {
        V result = null;
        try {
            result = vClass.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return result;
    }
    public static <K,V> List<V> copyBeanList(List<K> list,Class<V> vClass){
        return list
                .stream()
                .map(o->copyBean(o,vClass))
                .collect(Collectors.toList());
    }
}
