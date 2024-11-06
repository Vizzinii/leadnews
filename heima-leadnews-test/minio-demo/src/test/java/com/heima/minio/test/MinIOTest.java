package com.heima.minio.test;

import com.heima.file.service.MinIOFileStorageService;
import com.heima.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class) //RunWith 注解会帮我们加载Spring的上下文
public class MinIOTest {

    @Autowired
    private MinIOFileStorageService minIOFileStorageService;

    @Test
    public void test() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("F:\\Project04\\tmp\\css\\index.css");

        String path = minIOFileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
        System.out.println(path);
    }






    /**
     * 把list.html文件上传到 minio 中，并且可以在浏览器中访问
     * @param args
     */
    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = new FileInputStream("F:\\Project04\\tmp\\js\\index.js");

            // 1. 获取 minio 的链接信息，这需要创建一个 minio 客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://192.168.232.129:9001")
                    .build();
            // 2. 上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("plugin/js/index.js") //文件名称
                    .contentType("text/javascript")  //文件类型
                    .bucket("leadnews")  //所属的自创的bucket1的名称
                    .stream(fileInputStream,fileInputStream.available(),-1)
                    .build();
            minioClient.putObject(putObjectArgs);

            // 3. 拼接出访问路径
            //System.out.println("http://192.168.232.129:9001/leadnews/list.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
