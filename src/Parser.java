import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Parser {
    public static void main(String... args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Type in the puzzle, row by row");
        System.out.println("Use 'X' for walls, spaces or 'O' for empty spaces, and 'S' for start");
        System.out.println("End your input with 'end'");
        
        Set<Point> walls = new HashSet<>();
        Point start = null;
        int y = 0;
        int w = 0;
        while (true) {
            String row = s.nextLine();
            if (row.equalsIgnoreCase("end")) break;
            w = Math.max(w, row.length());
            for (int x = 0; x < row.toCharArray().length; x++) {
                switch (row.charAt(x)) {
                    case 'x', 'X' -> walls.add(new Point(x, y));
                    case 's', 'S' -> {
                        if (start == null) start = new Point(x, y);
                        else throw new IllegalArgumentException("Cannot have multiple starting points");
                    }
                    case 'o', 'O', ' ' -> {
                    }
                    default -> throw new IllegalArgumentException("Char '" + row.charAt(x) + "' unrecognized");
                }
            }
            y++;
        }
        
        Board b = new Board(y, w, walls, start);
        long time = System.currentTimeMillis();
        Optional<Board> sol = Solver.solveNonParallel(b);
        time = System.currentTimeMillis() - time;
        if (sol.isPresent()) {
            Direction[] dirs = sol.get().moves.toArray();
            System.out.println("Moves:");
            System.out.println(
                    Arrays.stream(dirs).map(Direction::name).reduce((s1, s2) -> s1 + ", " + s2).orElse("")
            );
        } else {
            System.err.println("All combinations exhausted; No solution found");
        }
        System.out.println("Time taken: " + time + "ms");
    }
}
