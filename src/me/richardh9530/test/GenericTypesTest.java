package me.richardh9530.test;

public class GenericTypesTest {
    static class A{
        A(){
            System.out.println("init A");
        }
        static class B{
            B(){
                System.out.println("init B");
            }

            static class C{
                C(){
                    System.out.println("init C");
                }
            }
        }
    }

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);  // 相等
        System.out.println(e == f);  // 不相等
        System.out.println(c == (a + b));  // 相等
        System.out.println(c.equals(a + b));  // 相等
        System.out.println(g == (a + b));  // 不相等
        System.out.println(g.equals(a + b));  // 相等
        System.out.println(5.0==5);

        new GenericTypesTest.A.B.C();
    }
}
