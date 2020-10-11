
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Board board;
    private int moves = 0;
    private Stack<Board> solution;
    private boolean solvable = false;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException();
        this.board = initial;
        solution = new Stack<Board>();
        MinPQ<SearchNode> mainPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> parallelPQ = new MinPQ<SearchNode>();
        mainPQ.insert(new SearchNode(initial, 0, null));
        parallelPQ.insert(new SearchNode(initial.twin(), 0, null));
        SearchNode main = mainPQ.delMin();
        SearchNode parallel = parallelPQ.delMin();
        if (main.board.isGoal())
            solvable = true;
        else
            while (!parallel.board.isGoal()) {
                for (Board b : main.board.neighbors()) {
                    if (main.prev == null)
                        mainPQ.insert(new SearchNode(b, 1, main));
                    else {
                        if (b.equals(main.prev.board))
                            continue;
                        mainPQ.insert(new SearchNode(b, main.prev.moves + 1, main));
                    }
                }
                main = mainPQ.delMin();
                if (main.board.isGoal()) {
                    solvable = true;
                    break;
                }
                for (Board b : parallel.board.neighbors()) {
                    if (parallel.prev == null)
                        parallelPQ.insert(new SearchNode(b, 1, parallel));
                    else {
                        if (b.equals(parallel.prev.board))
                            continue;
                        parallelPQ.insert(new SearchNode(b, parallel.prev.moves + 1, parallel));
                    }
                }
                parallel = parallelPQ.delMin();
            }
        if (solvable)
            while (main.prev != null) {
                solution.push(main.board);
                main = main.prev;
                moves++;
            }
        solution.push(main.board);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int manhattan;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            manhattan = this.board.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            int value = (this.manhattan + this.moves) - (o.manhattan + o.moves);
            return value == 0 ? this.manhattan - o.manhattan : value;
        }

    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        /*
         * int[][] test = { { 4, 3, 1 }, { 0, 7, 2 }, { 8, 5, 6 } }; Board board1 = new
         * Board(test); Solver solver = new Solver(board1); if (!solver.isSolvable()) {
         * StdOut.println("No solution possible"); Solver solver2=new
         * Solver(board1.twin()); StdOut.println("Minimum number of moves = " +
         * solver2.moves()); for (Board board : solver2.solution())
         * StdOut.println(board); } else { StdOut.println("Minimum number of moves = " +
         * solver.moves()); for (Board board : solver.solution()) StdOut.println(board);
         * }
         */
    }

}
