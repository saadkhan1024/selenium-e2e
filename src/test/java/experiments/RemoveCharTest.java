package experiments;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class RemoveCharTest {
    @Test
    public void removeFirstChar() {
        String input = "apylpna"; //a
        HashMap<Character, Integer> map = new LinkedHashMap<Character, Integer>();

        for(int i = 0; i < input.length(); i++) {
            if(map.containsKey(input.charAt(i)))
                map.put(input.charAt(i), map.get(input.charAt(i)) + 1);
            else
                map.put(input.charAt(i), 1);
        }
        System.out.println(map);

        Character element = null;
        for(Entry<Character, Integer> entry : map.entrySet()) {
            if(entry.getValue() > 1) {
                element = entry.getKey();
                break;
            }
        }

        StringBuilder output = new StringBuilder(input);
        output.deleteCharAt(input.indexOf(element)); //remove first occurrence of repeating character
        output.deleteCharAt(input.lastIndexOf(element)); //remove last occurrence of repeating character
        System.out.println(output);
    }
}