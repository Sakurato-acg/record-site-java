package top.recordsite.enums;

public final class ContentType {
    public final static String zip="application/zip";

    public static boolean isImage(String type){
        return type.contains("image");
    }
}
