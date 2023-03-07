package expression.generic;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Incorrect number of arguments. Please, provide mode (-i/-d/-bi/-u/-p/-s) " +
                                       "as the first argument and expression as the second.");
            System.exit(-1);
        }
        Tabulator tabulator = new GenericTabulator();
        try {
            Object[][][] res = tabulator.tabulate(
                    args[0].replaceFirst("-", ""),
                    args[1],
                    -2, 2, -2, 2, -2, 2
            );
            System.out.println("x\\y");
            for (int i = 0; i < res.length; ++i) {
                System.out.print(i - 2 + "| ");
                for (int j = 0; j < res[i].length; ++j) {
                    System.out.print(Arrays.toString(res[i][j]) + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
