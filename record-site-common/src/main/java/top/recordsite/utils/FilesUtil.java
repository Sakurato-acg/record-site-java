package top.recordsite.utils;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.util.*;

public class FilesUtil {
    /**
     * 获取指定文件夹下所有文件，不含文件夹里的文件
     *
     * @param dirFilePath 文件夹路径
     * @return
     */
    public static List<File> getAllFile(String dirFilePath) {
        if (StrUtil.isBlank(dirFilePath))
            return null;
        return getAllFile(new File(dirFilePath));
    }


    /**
     * 获取指定文件夹下所有文件，不含文件夹里的文件
     *
     * @param dirFile 文件夹
     * @return
     */
    public static List<File> getAllFile(File dirFile) {
        // 如果文件夹不存在或着不是文件夹，则返回 null
        if (Objects.isNull(dirFile) || !dirFile.exists() || dirFile.isFile())
            return null;

        File[] childrenFiles = dirFile.listFiles();
        if (Objects.isNull(childrenFiles) || childrenFiles.length == 0)
            return null;

        List<File> files = new ArrayList<>();
        for (File childFile : childrenFiles) {
            // 如果是文件，直接添加到结果集合
            if (childFile.isFile()) {
                files.add(childFile);
            }
            //以下几行代码取消注释后可以将所有子文件夹里的文件也获取到列表里
            else {
                // 如果是文件夹，则将其内部文件添加进结果集合
                List<File> cFiles = getAllFile(childFile);
                if (Objects.isNull(cFiles) || cFiles.isEmpty()) continue;
                files.addAll(cFiles);
            }
        }
        return files;
    }

    public static Map<String, File> getAllFileWithMap(File dirFile) {
        // 如果文件夹不存在或着不是文件夹，则返回 null
        if (Objects.isNull(dirFile) || !dirFile.exists() || dirFile.isFile())
            return null;

        File[] childrenFiles = dirFile.listFiles();
        if (Objects.isNull(childrenFiles) || childrenFiles.length == 0)
            return null;

//        List<File> files = new ArrayList<>();
        Map<String, File> map = new HashMap<>();

        for (File childFile : childrenFiles) {
            // 如果是文件，直接添加到结果集合
            if (childFile.isFile()) {
                map.put(childFile.getName(),childFile);
            }
            //以下几行代码取消注释后可以将所有子文件夹里的文件也获取到列表里
            else {
                // 如果是文件夹，则将其内部文件添加进结果集合
                Map<String, File> cFiles = getAllFileWithMap(childFile);
                if (Objects.isNull(cFiles) || cFiles.isEmpty()) continue;
                map.putAll(cFiles);
            }
        }
        return map;
    }
}    
