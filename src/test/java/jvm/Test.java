package jvm;

/**
 * Created by Administrator on 2017/1/4.
 */
public class Test {
    public static final People   a = new People("aa");

    public static void main(String[] args) {
        a.setId("11");
        System.out.print(a.getId());
    }

}
