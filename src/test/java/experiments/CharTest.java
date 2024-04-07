package experiments;

import org.testng.annotations.Test;

public class CharTest {

    @Test
    public void charOperations() {
        String input = "abCD123^&*";
        char[] chArray = input.toCharArray();

        StringBuilder output = new StringBuilder();
        for(char c : chArray) {
            if(Character.isAlphabetic(c) || Character.isDigit(c))
                if(Character.isLowerCase(c))
                    output.append(Character.toUpperCase(c));
                else if(Character.isUpperCase(c))
                    output.append(Character.toLowerCase(c));
        }
        System.out.println(output);

        int num = 10;
        try {
            int result = num/0;
        }
        catch (ArithmeticException e) {
            System.out.println("catch 1 executed");
        }
        catch (Exception e) {
            System.out.println("catch 2 executed");
        }
        finally {
            System.out.println("finally executed");
        }
        System.out.println("post finally");

        String test = "Saad K ";
        System.out.println(test.lastIndexOf(' '));
    }
}
