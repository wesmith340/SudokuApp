package Sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LogicProcessor {

    class Coord {
        private int row, col;
        private Stack<Integer> numList;
        public Coord(int row, int col) {
            this.row = row;
            this.col = col;
            numList = new Stack<>();
        }
        public int getRow()
        {   return this.row;    }
        public int getCol()
        {   return this.col;    }
        public int getGuess()
        {   return this.numList.pop();      }
        public boolean hasGuess()
        {   return !this.numList.isEmpty(); }

        public void resetGuesses() {
            numList.empty();
            for (int i = 1; i <= NUM_DIGITS; i++) {
                numList.add(i);
            }
            Collections.shuffle(numList);
        }

        public int hashCode() {
            return (row + " " + col).hashCode();
        }
        public boolean equals(Object o) {
            if(!(o instanceof Coord)) return false;
            Coord other = (Coord) o;
            return this.row == other.row && this.col == other.col;
        }
        public String toString()
        {   return "Row: "+ row+" Col: "+col;    }
    }



    private int NUM_DIGITS = 9;
    private ArrayList<Coord> userGuesses;
    int[][] answerGrid, userGrid;


    public LogicProcessor() {
        answerGrid = new int[NUM_DIGITS][NUM_DIGITS];
        userGrid = new int[NUM_DIGITS][NUM_DIGITS];

        newGrid();
    }
    public LogicProcessor(String filename) {
        answerGrid = new int[NUM_DIGITS][NUM_DIGITS];
        userGrid = new int[NUM_DIGITS][NUM_DIGITS];

        File file = new File(filename);
        try {
            Scanner scan = new Scanner(file);
            int j = 0;
            while (scan.hasNext()) {
                String line[] = scan.nextLine().split(" ");
                for(int k = 0; k < line.length; k++) {
                    answerGrid[j][k] = Integer.parseInt(line[k]);
                    userGrid[j][k] = Integer.parseInt(line[k]);
                }
                j++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println(solvable());
    }
    public void newGrid() {
        Stack<Integer> numList = new Stack<>();
        ArrayList<Integer> row = new ArrayList<>();

        LinkedList<Coord> gridMaker = new LinkedList<>();
        for (int j = 0; j < NUM_DIGITS; j++) {
            for (int k = 0; k < NUM_DIGITS; k++) {
                gridMaker.add(new Coord(j,k));
                gridMaker.getLast().resetGuesses();
            }
        }
        //Collections.shuffle(gridMaker);

        ListIterator it = gridMaker.listIterator();

        while (it.hasNext()) {
            Coord temp = (Coord)it.next();
            if (temp.hasGuess())
                answerGrid[temp.getRow()][temp.getCol()] = temp.getGuess();
            else {
                temp.resetGuesses();
                answerGrid[temp.getRow()][temp.getCol()] = 0;
                it.previous();
            }
            if (checkDigit(temp.getRow(), temp.getCol()) == false)
                it.previous();
        }
        printGrid();
        for (int j = 0; j < NUM_DIGITS; j++) {
            for (int k = 0; k < NUM_DIGITS; k++) {
                userGrid[j][k] = answerGrid[j][k];
            }
        }

        createPuzzle();
    }

    private void createPuzzle() {
        Random randCol = new Random();
        Random randRow = new Random();
        //do {
            userGuesses = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                int col = randCol.nextInt(9);
                int row = randRow.nextInt(9);
                if (!userGuesses.contains(new Coord(row, col))) {
                    userGrid[row][col] = 0;
                    userGuesses.add(new Coord(row, col));
                } else {
                    i--;
                }
            }
        //} while(!solvable());
    }

    private void printGrid() {
        for (int c = 0; c < NUM_DIGITS; c++) {
            for (int d = 0; d < NUM_DIGITS; d++) {
                System.out.print(answerGrid[c][d]+" ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }


    public String getDigit(int row, int col) {
        if (userGrid[row][col] != 0) {
            return String.valueOf(userGrid[row][col]);
        } else {
            return " ";
        }
    }

    private boolean checkDigit(int row, int col) {
        boolean goodDigit = true;

        // Check row and column for repeated digit
        for (int i = 0; i < NUM_DIGITS; i++) {
            if(answerGrid[row][col] == answerGrid[row][i] && i != col) {
                goodDigit = false;
            }
            if(answerGrid[row][col] == answerGrid[i][col] && i != row) {
                goodDigit = false;
            }
        }
        int rowBox = row/3;
        int colBox = col/3;
        int x = (int)Math.sqrt(NUM_DIGITS); // number of columns and rows in a box

        for (int j = 0; j < x; j++) {
            for (int k = 0; k < x; k++) {
                if(answerGrid[row][col] == answerGrid[j+x*rowBox][k+x*colBox] && (j+x*rowBox != row || k+x*colBox != col)) {
                    goodDigit = false;
                }
            }
        }
        return goodDigit;
    }


    private boolean solvable() {
        boolean valid = true;
        LinkedList<Coord> gridMaker = new LinkedList<>();
        for (int j = 0; j < NUM_DIGITS; j++) {
            for (int k = 0; k < NUM_DIGITS; k++) {
                if (userGrid[j][k] == 0) {
                    gridMaker.add(new Coord(j, k));
                    gridMaker.getLast().resetGuesses();
                }
            }
        }

        ListIterator it = gridMaker.listIterator();

        while (it.hasNext()) {
            Coord temp = (Coord)it.next();
            if (temp.hasGuess())
                answerGrid[temp.getRow()][temp.getCol()] = temp.getGuess();
            else {
                temp.resetGuesses();
                answerGrid[temp.getRow()][temp.getCol()] = 0;
                it.previous();
            }
            if (checkDigit(temp.getRow(), temp.getCol()) == false)
                it.previous();
        }
        for (int j = 0; j < NUM_DIGITS; j++) {
            for (int k = 0; k < NUM_DIGITS; k++) {
                //if()
                userGrid[j][k] = answerGrid[j][k];
            }
        }
        return valid;
    }
    public void enterGuess(int row, int col, int n) {
        if (userGuesses.contains(new Coord(row, col))) {
            userGrid[row][col] = n;
        }
    }
    public boolean isGuess(int row, int col) {
        return userGuesses.contains(new Coord(row, col));
    }

    public boolean checkGrid() {
        boolean goodGrid = true;
        for (int j = 0; j < NUM_DIGITS; j++) {
            for (int k = 0; k < NUM_DIGITS; k++) {
                if (userGrid[j][k] != answerGrid[j][k]) {
                    goodGrid = false;
                }
            }
        }
        return goodGrid;
    }
}
