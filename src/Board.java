import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final int h;
    private final int w;
    private final int toFill;
    private final boolean[][] walls;
    private final Set<Point> occupied;
    private final Point head;
    public final TraceLink<Direction> moves;
    private final Set<Board> exploredBoards;
    
    public Board(int h, int w, Set<Point> walls, Point head) {
        this.h = h;
        this.w = w;
        toFill = h * w - walls.size();
        this.walls = new boolean[h][w];
        for (Point wall : walls) this.walls[wall.y()][wall.x()] = true;
        if(head == null) throw new IllegalArgumentException("No head present");
        occupied = new HashSet<>();
        occupied.add(head);
        this.head = head;
        moves = null;
        exploredBoards = new HashSet<>();
    }
    
    public Board(int h, int w, int toFill, boolean[][] walls, Set<Point> occupied, Point head,
                 TraceLink<Direction> moves, Set<Board> exploredBoards) {
        this.h = h;
        this.w = w;
        this.toFill = toFill;
        this.walls = walls;
        this.occupied = occupied;
        this.head = head;
        this.moves = moves;
        this.exploredBoards = exploredBoards;
    }
    
    public boolean hasWon() {
        return occupied.size() == toFill;
    }
    
    public List<Board> step() {
        List<Board> ret = new ArrayList<>();
        {
            //up
            int x = head.x();
            int y = head.y();
            if (isAllowed(x, y - 1)) {
                Set<Point> newOcc = new HashSet<>(occupied);
                while (isAllowed(x, --y)) newOcc.add(new Point(x, y));
                Point newH = new Point(x, y + 1);
                Board n = new Board(h, w, toFill, walls, newOcc, newH, new TraceLink<>(moves, Direction.UP),
                        exploredBoards);
                if (exploredBoards.add(n)) ret.add(n);
            }
        }
        
        {
            //down
            int x = head.x();
            int y = head.y();
            if (isAllowed(x, y + 1)) {
                Set<Point> newOcc = new HashSet<>(occupied);
                while (isAllowed(x, ++y)) newOcc.add(new Point(x, y));
                Point newH = new Point(x, y - 1);
                Board n = new Board(h, w, toFill, walls, newOcc, newH, new TraceLink<>(moves, Direction.DOWN),
                        exploredBoards);
                if (exploredBoards.add(n)) ret.add(n);
            }
        }
        {
            //left
            int x = head.x();
            int y = head.y();
            if (isAllowed(x - 1, y)) {
                Set<Point> newOcc = new HashSet<>(occupied);
                while (isAllowed(--x, y)) newOcc.add(new Point(x, y));
                Point newH = new Point(x + 1, y);
                Board n = new Board(h, w, toFill, walls, newOcc, newH, new TraceLink<>(moves, Direction.LEFT),
                        exploredBoards);
                if (exploredBoards.add(n)) ret.add(n);
            }
        }
        {
            //right
            int x = head.x();
            int y = head.y();
            if (isAllowed(x + 1, y)) {
                Set<Point> newOcc = new HashSet<>(occupied);
                while (isAllowed(++x, y)) newOcc.add(new Point(x, y));
                Point newH = new Point(x - 1, y);
                Board n = new Board(h, w, toFill, walls, newOcc, newH, new TraceLink<>(moves, Direction.RIGHT),
                        exploredBoards);
                if (exploredBoards.add(n)) ret.add(n);
            }
        }
        return ret;
    }
    
    boolean isAllowed(int x, int y) {
        return x >= 0 && x < w && y >= 0 && y < h && !walls[y][x] && !occupied.contains(new Point(x, y));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Board board = (Board) o;
        
        if (!occupied.equals(board.occupied)) return false;
        return head.equals(board.head);
    }
    
    @Override
    public int hashCode() {
        int result = occupied.hashCode();
        result = 31 * result + head.hashCode();
        return result;
    }
}
