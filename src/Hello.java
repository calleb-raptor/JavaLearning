
/**
 * Hello
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hello {

    public static int randInt() {
        int randNum = (int) (Math.random() * 4);
        return randNum;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String name = reader.readLine();

        String text = "Hello," + name + ", Ya Bastard!";

        int randNum = randInt();

        if (randNum == 0) {
            System.out.println("Welp... You suck!");
        } else {
            System.out.println(text + "\nYour random number is:\n" + randNum);
        }
    }
}