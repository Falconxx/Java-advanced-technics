import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Scanner;

public class Game {

    public static char[][] Table;
    public ScriptEngine difficultyEngine;

    public Invocable invocable;

    public Game(int x) {
        Table = new char[x][x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < x; j++) {
                Table[i][j] = ' ';
            }
        }

        try {
            difficultyEngine = new ScriptEngineManager().getEngineByName("nashorn");
            difficultyEngine.eval(new FileReader("src/hard.js"));
            invocable = (Invocable) difficultyEngine;
        } catch (Exception e) {
        }
    }

    public static void main(String[] argv) {
        try {
            Game g = new Game(10);

            //Object result = g.invocable.invokeFunction("fun1", "Peter Parker");
            //System.out.println(result);
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Change difficulty leve");
                System.out.println("2. Make Your move");

                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.println("Enter name of script written in JS that represents AI (for example easy.js or hard.js): ");
                    String choice2 = scanner.nextLine();
                    g.difficultyEngine.eval(new FileReader("src/" + choice2));
                    g.invocable = (Invocable) g.difficultyEngine;
                }

                for (int i = 0; i < g.Table.length; i++) {
                    for (int j = 0; j < g.Table.length; j++) {
                        System.out.print(g.Table[i][j]);
                    }
                    System.out.println();
                }

                System.out.println("Enter X: ");
                int a = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter Y: ");
                int b = Integer.parseInt(scanner.nextLine());

                do {

                    g.Table[a][b] = 'O';
                }while(g.Table[a][b] == 'X');
                Object result2 = g.invocable.invokeFunction("make_move", (Object) g.Table);
                int[] result3 = (int[]) result2;
                System.out.println(result3[0] + " " + result3[1]);
                g.Table[result3[0]][result3[1]] = 'X';

                for (int i = 0; i < g.Table.length; i++) {
                    for (int j = 0; j < g.Table.length; j++) {
                        System.out.print(g.Table[i][j]);
                    }
                    System.out.println();
                }
                for (int i = 0; i < g.Table.length - 5; i++) {
                    for (int j = 0; j < g.Table.length - 5; j++) {
                        if(g.Table[i][j] != ' ' && (g.Table[i][j] == 'X' && g.Table[i][j+1] == 'X' && g.Table[i][j+2] == 'X' && g.Table[i][j+3] == 'X' && g.Table[i][j+4] == 'X')) {
                            System.out.println("Computer wins!");
                            return;
                        }
                        else if(g.Table[i][j] != ' ' && (g.Table[i][j] == 'X' && g.Table[i+1][j+1] == 'X' && g.Table[i+2][j+2] == 'X' && g.Table[i+3][j+3] == 'X' && g.Table[i+4][j+4] == 'X')) {
                            System.out.println("Computer wins!");
                            return;
                        }
                        else if(g.Table[i][j] != ' ' && (g.Table[i][j] == 'X' && g.Table[i+1][j] == 'X' && g.Table[i+2][j] == 'X' && g.Table[i+3][j] == 'X' && g.Table[i+4][j] == 'X')) {
                            System.out.println("Computer wins!");
                            return;
                        }
                        else if(g.Table[i][j] != ' ' && (g.Table[i][j] == 'O' && g.Table[i][j+1] == 'O' && g.Table[i][j+2] == 'O' && g.Table[i][j+3] == 'O' && g.Table[i][j+4] == 'O')) {
                            System.out.println("You Win! Smart move");
                            return;
                        }
                        else if(g.Table[i][j] != ' ' && (g.Table[i][j] == 'O' && g.Table[i+1][j+1] == 'O' && g.Table[i+2][j+2] == 'O' && g.Table[i+3][j+3] == 'O' && g.Table[i+4][j+4] == 'O')) {
                            System.out.println("You Win! GZ");
                            return;
                        }
                        else if(g.Table[i][j] != ' ' && (g.Table[i][j] == 'O' && g.Table[i+1][j] == 'O' && g.Table[i+2][j] == 'O' && g.Table[i+3][j] == 'O' && g.Table[i+4][j] == 'O')) {
                            System.out.println("You Win! CONGRATULATION");
                            return;
                        }
                    }
                }
                scanner.nextLine();
            }
        } catch (Exception e) {

        }

    }
}
