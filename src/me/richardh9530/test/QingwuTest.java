package me.richardh9530.test;

import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

interface inter{
    int a = 1;
}
class Test{
    public static int a = 3;
}
public class QingwuTest extends Test implements inter{
    static List<String> list;
    public static void main(String[] args){
        int i = 0;
        i = i++;
        System.out.println(i++);
        System.out.println(i++);
        System.out.println(i);
        ParameterizedType pt = null;
        try {
             list = new ArrayList<String>();//这里必须指明String类型，创建一个匿名类实现泛型接口};
             pt = (ParameterizedType) QingwuTest.class.getDeclaredField("list").getGenericType();//
            System.out.println(pt.getActualTypeArguments()[0]);
        }catch (Exception e){
            e.printStackTrace();
            pt = null;
        }finally {
            Type clazz = pt.getActualTypeArguments()[0];//取第一个泛型的类型
            System.out.println(clazz);
        }

    }
}

