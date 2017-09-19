package system;

/**
 * @author fangtest on 2017/9/11.
 */
public class Test {
    public static void main(String[] args) {
//        System.setProperty("mykey", "1");
        String mykey = System.getProperty("mykey");
        System.out.println(mykey);
    }
}
