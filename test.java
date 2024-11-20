import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String test = scan.next();
        scan.close();
        for (int i = 0; i <= 2; i++) {
            int length = test.length();
            if (test.substring(length - 1, length).equals("0")) {
                test = test.substring(0, length - 1);
            } else if (test.substring(length - 1, length).equals(".")) {
                test = test.substring(0, length - 1);
            }

        }
        System.out.println(test);
    }
}
