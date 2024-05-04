package top.recordsite.security;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Component
@Slf4j
public class TableFieldConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());

        Integer userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            log.error(e.toString());
            userId = -1;//表示是自己创建
        }
        //获取属性名
        String createTime = metaObject.findProperty("createTime", false);
        String createBy = metaObject.findProperty("createBy", false);
        String updateTime = metaObject.findProperty("updateTime", false);
        String updateBy = metaObject.findProperty("updateBy", false);
        if (StringUtils.hasText(createTime)&& ObjectUtils.isEmpty(metaObject.getValue(createTime))) {
            metaObject.setValue(createTime, LocalDateTime.now());
        }
        if (StringUtils.hasText(createBy)&& ObjectUtils.isEmpty(metaObject.getValue(createBy))) {
            metaObject.setValue(createBy, userId);
        }
        if (StringUtils.hasText(updateTime)&& ObjectUtils.isEmpty(metaObject.getValue(updateTime))) {
            metaObject.setValue(updateTime, LocalDateTime.now());
        }
        if (StringUtils.hasText(updateBy)&& ObjectUtils.isEmpty(metaObject.getValue(updateBy))) {
            metaObject.setValue(updateBy, userId);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]");
        log.info(metaObject.toString());

        Integer userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            log.error(e.toString());
            userId = -1;//表示是自己创建
        }
        String updateTime = metaObject.findProperty("updateTime", false);
        String updateBy = metaObject.findProperty("updateBy", false);
        if (StringUtils.hasText(updateTime)&& ObjectUtils.isEmpty(metaObject.getValue(updateTime))) {
            metaObject.setValue(updateTime, LocalDateTime.now());
        }
        if (StringUtils.hasText(updateBy)&& ObjectUtils.isEmpty(metaObject.getValue(updateBy))) {
            metaObject.setValue(updateBy, userId);
        }
    }
}
