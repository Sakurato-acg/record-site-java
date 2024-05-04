package top.recordsite.enums.system;

import java.util.HashMap;
import java.util.Map;

public final class MediaDictionary extends CommonDictionary {

    public final static Integer status_enabled=0;

    public final static Integer status_disabled=1;

    public static Map<String,Integer> way=new HashMap<>();

    static {
        way.put("article",1);
        way.put("user",2);
    }

    public final static Integer visible_disabled=1;
}
