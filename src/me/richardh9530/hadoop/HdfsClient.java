package me.richardh9530.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HdfsClient {
    @Test
    public void testMkdirs() throws IOException, InterruptedException,
            URISyntaxException {
// 1 获取文件系统
        Configuration configuration = new Configuration();
// 配置在集群上运行
// configuration.set("fs.defaultFS", "hdfs://hadoop102:9000");
// FileSystem fs = FileSystem.get(configuration);
        //Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://master:9000"), configuration,
                "halley");
// 2 创建目录
        fs.delete(new Path("/1108"));
// 3 关闭资源
        fs.close();
    }
}