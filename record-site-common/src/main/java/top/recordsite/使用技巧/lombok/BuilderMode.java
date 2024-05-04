package top.recordsite.使用技巧.lombok;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.FileNotFoundException;

//https://mingmingruyue.blog.csdn.net/article/details/130190814?
public class BuilderMode {

    public static void main(String[] args) throws FileNotFoundException {
        Person build = Person.builder().name("lpl").build();
        System.out.println("build = " + build.name());

//        @Cleanup FileInputStream fileInputStream = new FileInputStream("");
    }

}

@Data
@Builder
@ToString
@Accessors(fluent = true)
class Person {

    private String name;

    @Builder.Default
    private int age = 10;

}