/**
 * Hello
 */
public class Hello {

    public static int randInt() {
        int randNum = (int) (Math.random() * 4);
        return randNum;
    }

    public static void main(String[] args) {
        String text = "Hello Ya Bastard!";

        int randNum = randInt();

        if (randNum == 0) {
            System.out.println("Welp... You suck!");
        } else {
            System.out.println(text + "\nYour random number is:\n" + randNum);
        }
    }
}