package me.richardh9530.test;
import java.io.IOException;
import java.io.InputStream;

/**
 * 此处 loadClass(ClassLoaderTest)
 * 然后实例化该类
 * 然后Load Object -> ClassLoader -> ClassLoader1 -> ClassLoader2
 * 为什么是先Load Class2 然后 LoadClass1呢？
 */
class ClassLoader1 extends ClassLoader{

}
class ClassLoader2 extends ClassLoader1{
//    static {
//        System.out.println("我被加载了");
//    }
    public Class<?> loadClass(String name)
            throws ClassNotFoundException {
        try {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (null == is) {
                System.out.println(
                        "--filename: "+fileName+"--loading class: "+
                                name +"--此时input stream is null");
                // TODO: 为什么这里输出了两次？也就是为什么这段代码执行了两次
                return super.loadClass(name);
            }else{
                System.out.println("--filename: "+
                        fileName+"--loading class: "+ name
                        +"--此时input stream is not null");
            }
            // System.out.println("out of loader");
            byte[] b = new byte[is.available()];
            is.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }
}

public class ClassLoaderTest {
//    static {
//        System.out.println("Test被加载了");
//    }
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ClassLoader myLoader = new ClassLoader2();
        System.out.println("init myLoader done!");
        Object obj = myLoader.loadClass("me.richardh9530.test.ClassLoaderTest").newInstance();
        System.out.println(obj.getClass());
        System.out.println(obj instanceof ClassLoaderTest);

    }
}