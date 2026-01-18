import java.util.Scanner;

public class RedBlueGame {
    private static final String COMPUTER = "computer";
    private static final String HUMAN = "human";
    private static final String STANDARD = "standard";
    private static final String MISERE = "misere";
    private static final int WIN_SCORE = 10000;
    private static final int LOSE_SCORE = -10000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter red marbles: ");
        int red = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter blue marbles: ");
        int blue = Integer.parseInt(scanner.nextLine().trim());

        String version;
        while (true) {
            System.out.print("Choose version (standard/misere): ");
            version = scanner.nextLine().trim().toLowerCase();
            if (version.equals(STANDARD) || version.equals(MISERE)) break;
            System.out.println("Invalid input.");
        }

        String firstPlayer;
        while (true) {
            System.out.print("Who goes first? (human/computer): ");
            firstPlayer = scanner.nextLine().trim().toLowerCase();
            if (firstPlayer.equals(HUMAN) || firstPlayer.equals(COMPUTER)) break;
            System.out.println("Invalid input.");
        }

        System.out.print("Enter depth (2-6 recommended): ");
        String depthInput = scanner.nextLine().trim();
        int depth = depthInput.isEmpty() ? 4 : Integer.parseInt(depthInput);

        playGame(red, blue, version, firstPlayer, depth);
        scanner.close();
    }

    private static void playGame(int red, int blue, String version, String currentPlayer, int depth) {
        Scanner scanner = new Scanner(System.in);

        while (!isGameOver(red, blue)) {
            showStatus(red, blue);

            if (currentPlayer.equals(COMPUTER)) {
                System.out.println("Computer's turn:");
                String[] move = getComputerMove(red, blue, version, depth);
                if (move == null) break;

                String pile = move[0];
                int count = Integer.parseInt(move[1]);
                System.out.println("Computer removes " + count + " " + pile + " marble(s).\n");

                if (pile.equals("red")) red -= count;
                else blue -= count;

            } else {
                System.out.println("Human's turn:");
                String[] move = getUserMove(red, blue, version, scanner);

                String pile = move[0];
                int count = Integer.parseInt(move[1]);
                if (pile.equals("red")) red -= count;
                else blue -= count;
            }

            currentPlayer = currentPlayer.equals(HUMAN) ? COMPUTER : HUMAN;
        }

        showStatus(red, blue);
        String lastMover = currentPlayer.equals(HUMAN) ? COMPUTER : HUMAN;

        if (version.equals(STANDARD)) {
            System.out.println("Game Over! " + lastMover + " wins!");
        } else {
            System.out.println("Game Over! " + lastMover + " loses!");
        }

        System.out.println("Final score: " + (2 * red + 3 * blue));
    }

    private static boolean isGameOver(int red, int blue) {
        return red == 0 || blue == 0;
    }

    private static void showStatus(int red, int blue) {
        System.out.println("Red: " + red + " Blue: " + blue);
    }

    private static String[] getUserMove(int red, int blue, String version, Scanner scanner) {
        while (true) {
            System.out.print("Choose a pile (red or blue): ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+");
            String pile = parts[0].toLowerCase();
            int cnt;

            if (!pile.equals("red") && !pile.equals("blue")) {
                System.out.println("Invalid pile.");
                continue;
            }

            if (parts.length > 1) {
                try {
                    cnt = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number.");
                    continue;
                }
            } else {
                System.out.print("Enter number of marbles to remove: ");
                try {
                    cnt = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number.");
                    continue;
                }
            }

            if (cnt < 1 || cnt > 2) {
                System.out.println("You can only remove 1 or 2 marbles.");
                continue;
            }

            if ((pile.equals("red") && cnt > red) || (pile.equals("blue") && cnt > blue)) {
                System.out.println("Not enough marbles.");
                continue;
            }

            return new String[]{pile, String.valueOf(cnt)};
        }
    }

    private static String[] getComputerMove(int red, int blue, String version, int depth) {
        String[][] movePriority = version.equals(STANDARD)
                ? new String[][]{{"blue", "2"}, {"red", "2"}, {"blue", "1"}, {"red", "1"}}
                : new String[][]{{"red", "1"}, {"blue", "1"}, {"red", "2"}, {"blue", "2"}};

        String[] bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (String[] move : movePriority) {
            String pile = move[0];
            int count = Integer.parseInt(move[1]);

            int nextRed = red, nextBlue = blue;

            if (pile.equals("red") && red >= count) nextRed -= count;
            else if (pile.equals("blue") && blue >= count) nextBlue -= count;
            else continue;

            int value = runMinMax(nextRed, nextBlue, version, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (value > bestValue) {
                bestValue = value;
                bestMove = new String[]{pile, String.valueOf(count)};
            }
        }

        return bestMove;
    }

    private static int runMinMax(int red, int blue, String version, int depth,
                                 boolean isMax, int alpha, int beta) {
        if (isGameOver(red, blue) || depth == 0) {
            return evaluate(red, blue, version, isMax);
        }

        int[][] moves = {{1, 0}, {2, 0}, {0, 1}, {0, 2}};

        if (isMax) {
            int maxEval = Integer.MIN_VALUE;
            for (int[] move : moves) {
                int newRed = red - move[0];
                int newBlue = blue - move[1];

                if (newRed >= 0 && newBlue >= 0) {
                    int eval = runMinMax(newRed, newBlue, version, depth - 1, false, alpha, beta);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int[] move : moves) {
                int newRed = red - move[0];
                int newBlue = blue - move[1];

                if (newRed >= 0 && newBlue >= 0) {
                    int eval = runMinMax(newRed, newBlue, version, depth - 1, true, alpha, beta);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    private static int evaluate(int red, int blue, String version, boolean isMaximizer) {
        if (isGameOver(red, blue)) {
            if (version.equals(STANDARD)) {
                return isMaximizer ? LOSE_SCORE : WIN_SCORE;
            } else {
                return isMaximizer ? WIN_SCORE : LOSE_SCORE;
            }
        }

        return version.equals(STANDARD) ? 2 * red + 3 * blue : -(2 * red + 3 * blue);
    }
}
