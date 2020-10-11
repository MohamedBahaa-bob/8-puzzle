import java.lang.Object;
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int[][] tiles;
    private final int N;
    private final int hamming;
    private final int manhattan;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        N = tiles[0].length;
        hamming = calculateHamming();
        manhattan = calculateManhattan();
    }

    public String toString() {
        String printed = String.valueOf(N);
        for (int i = 0; i < N; i++) {
            printed = printed.concat("\n");
            for (int j = 0; j < N; j++)
                printed = printed.concat(" " + String.valueOf(tiles[i][j]));
        }
        return printed;
    }

    private int tileAt(int row, int col) {
        return tiles[row][col];
    }

    public int dimension() {
        return N;
    }

    private int calculateHamming() { // number of tiles out of place
        int outOP = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != N * i + j + 1)
                    outOP++;
        outOP--;
        return outOP;
    }

    private int calculateManhattan() {
        int current, iDestination, jDestination, value = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                current = tiles[i][j];
                if (current != N * i + j + 1 && current != 0) {
                    iDestination = (int) (Math.ceil((current * 1.0) / (N * 1.0))) - 1;
                    jDestination = current - N * iDestination - 1;
                    value += Math.abs(i - iDestination) + Math.abs(j - jDestination);
                }
            }
        return value;
    }

    public int manhattan() {
        return manhattan;
    }

    public int hamming() {
        return hamming;
    }

    public boolean isGoal() {
        boolean goal = true;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1)
                    break;
                if (tiles[i][j] != N * i + j + 1)
                    goal = false;
            }
        return goal;
    }

    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null || this.dimension() != ((Board) y).dimension())
            return false;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != ((Board) y).tileAt(i, j))
                    return false;
        return true;
    }

    private int[][] deepCopy(int[][] original) {
        if (original == null)
            return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++)
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        return copy;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        int i = 0, j = 0;
        boolean enough = false;
        for (i = 0; i < N && !enough; i++)
            for (j = 0; j < N && !enough; j++)
                if (tiles[i][j] == 0) {
                    enough = true;
                }
        if (enough) {
            i--;
            j--;
        }
        if (i != 0) {
            int[][] firstCopy = deepCopy(tiles);
            firstCopy[i][j] = firstCopy[i - 1][j];
            firstCopy[i - 1][j] = 0;
            Board neighbor1 = new Board(firstCopy);
            neighbors.add(neighbor1);
        }
        if (i != N - 1) {
            int[][] secondCopy = deepCopy(tiles);
            secondCopy[i][j] = secondCopy[i + 1][j];
            secondCopy[i + 1][j] = 0;
            Board neighbor2 = new Board(secondCopy);
            neighbors.add(neighbor2);
        }
        if (j != 0) {
            int[][] thirdCopy = deepCopy(tiles);
            thirdCopy[i][j] = thirdCopy[i][j - 1];
            thirdCopy[i][j - 1] = 0;
            Board neighbor3 = new Board(thirdCopy);
            neighbors.add(neighbor3);
        }
        if (j != N - 1) {
            int[][] fourthCopy = deepCopy(tiles);
            fourthCopy[i][j] = fourthCopy[i][j + 1];
            fourthCopy[i][j + 1] = 0;
            Board neighbor4 = new Board(fourthCopy);
            neighbors.add(neighbor4);
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = deepCopy(tiles);
        int i = 0, j = 0;
        if (twin[i][j] == 0 || twin[i + 1][j] == 0)
            j++;
        int temp = twin[i][j];
        twin[i][j] = twin[i + 1][j];
        twin[i + 1][j] = temp;
        return new Board(twin);
    }

    public static void main(String[] args) {
        int[][] test = { { 1, 2, 3 }, { 0, 4, 6 }, { 7, 8, 5 } };
        Board board1 = new Board(test);
        /*
         * for (Board b : board1.neighbors()) { System.out.println(b.toString()); }
         * Board board2 = board1.twin(); Board board3 = board2.twin();
         * System.out.println(board3.toString()); System.out.println(board1.toString());
         * if (board2.isGoal()) System.out.println("goal"); else
         * System.out.println("notGoal"); System.out.println(board3.manhattan());
         */
        System.out.println(board1.twin().toString());
    }
}
