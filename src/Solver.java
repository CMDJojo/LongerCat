import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Solver {
    
    public static Optional<Board> solveNonParallel(Board board) {
        List<Board> boards = Collections.singletonList(board);
        int round = 0;
        while (true) {
            System.out.printf("Trying %d moves (%d tracked boards)\r", ++round, boards.size());
            List<Board> newBoards = boards
                    .stream()
                    .map(Board::step)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            Optional<Board> winner = newBoards.stream().filter(Board::hasWon).findAny();
            if (newBoards.size() == 0) {
                System.out.println("\nAll combinations exhausted; no solution found");
                return Optional.empty();
            } else if (winner.isPresent()) {
                System.out.println("\nSolution found!");
                return winner;
            } else boards = newBoards;
        }
    }
    
    public static Optional<Board> solve(Board board) {
        return solve(board, 8);
    }
    
    public static Optional<Board> solve(Board board, int threads) {
        return solve(board, new ForkJoinPool(threads));
    }
    
    public static Optional<Board> solve(Board board, ForkJoinPool pool) {
        try {
            List<Board> boards = Collections.singletonList(board);
            int round = 0;
            while (true) {
                System.out.printf("Trying %d moves (%d tracked boards)\r", ++round, boards.size());
                final List<Board> b = boards;
                List<Board> newBoards = pool.submit(() -> b
                        .parallelStream()
                        .map(Board::step)
                        .flatMap(Collection::parallelStream)
                        .collect(Collectors.toList())).get();
                Optional<Board> winner = newBoards.stream().filter(Board::hasWon).findAny();
                if (newBoards.size() == 0) {
                    System.out.println("\nAll combinations exhausted; no solution found");
                    return Optional.empty();
                } else if (winner.isPresent()) {
                    System.out.println("\nSolution found!");
                    return winner;
                } else boards = newBoards;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
