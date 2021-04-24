package Sudoku;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.*;

public class GameRenderer extends JPanel{

    class Square {
        public int x, y;
        public Color color;
        private Stack<Integer> numList;
        public Square(int x, int y) {
            this.x = x;
            this.y = y;
            this.color = Color.WHITE;
        }
        public Square(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
        public int hashCode() {
            return (x + " " + y).hashCode();
        }
        public boolean equals(Object o) {
            if(!(o instanceof Square)) return false;
            Square other = (Square) o;
            return this.x == other.x && this.y == other.y;
        }
        public String toString() {
            return String.format("x: %d y: %d Color %s", x, y, color.toString());
        }
    }

    private Dimension dim;
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    private final Font DIGIT_FONT;
    private final Font GIVEN_FONT;

    private final int DIGIT_WIDTH = 50;
    private final int DIGIT_HEIGHT = 50;
    private final int NUM_LINES = 10;
    private final int NUM_DIGITS = 9;

    private LogicProcessor logic;
    private Square selectedCell;
    Graphics2D g2;

    private ArrayList<Square> colorSquares;

    public GameRenderer() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto_Slab\\static\\RobotoSlab-Regular.tts")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Roboto_Slab\\static\\RobotoSlab-Bold.tts")));
        } catch (IOException |FontFormatException e) {
            //Handle exception
        }
        DIGIT_FONT = new Font("RobotoSlab-Regular", Font.PLAIN, 26);
        GIVEN_FONT = new Font("Courier New", Font.PLAIN, 30);



        dim = new Dimension(WIDTH,HEIGHT);
        this.setMaximumSize(dim);
        this.setMinimumSize(dim);
        logic = new LogicProcessor();//"Grid.txt");
        colorSquares = new ArrayList<>();

        for (int j = 1; j <= NUM_DIGITS; j++) {
            for (int k = 1; k <= NUM_DIGITS; k++) {
                colorSquares.add(new Square(k*DIGIT_WIDTH, j*DIGIT_HEIGHT));
            }
        }

        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D)g;

        for (Square square : colorSquares) {
            g2.setColor(square.color);
            g2.fillRect(square.x, square.y, DIGIT_WIDTH, DIGIT_HEIGHT);
        }
        g2.setColor(Color.BLACK);
        for (int i = 0; i < NUM_LINES; i++){
            setStroke(g2, i);
            int y = DIGIT_HEIGHT * (i+1);
            g2.drawLine(DIGIT_WIDTH,y,NUM_LINES * DIGIT_WIDTH,y);
        }
        for (int i = 0; i < NUM_LINES; i++){
            setStroke(g2,i);
            int x = DIGIT_WIDTH * (i+1);
            g2.drawLine(x,DIGIT_HEIGHT,x,DIGIT_HEIGHT*NUM_LINES);
        }


        for (int j = 1; j <= NUM_DIGITS; j++) {
            for (int k = 1; k <= NUM_DIGITS; k++) {
                g2.setFont(GIVEN_FONT);
                if(logic.isGuess(j-1,k-1)) {
                    g2.setFont(DIGIT_FONT);
                }
                int x = k*DIGIT_WIDTH + DIGIT_WIDTH/3;
                int y = j*DIGIT_HEIGHT + 2*DIGIT_HEIGHT/3;
                g2.drawString(logic.getDigit(j-1,k-1), x, y);

            }
        }


    }
    private void setStroke(Graphics2D g2, int i) {
        if (i % 3  == 0){
            g2.setStroke( new BasicStroke(3));
        } else {
            g2.setStroke( new BasicStroke(1));
        }
    }
    public void selectCell(int x, int y) {
        int i = colorSquares.indexOf(selectedCell);
        if (i != -1)
            colorSquares.get(i).color = Color.WHITE;

        x = x - x%DIGIT_WIDTH;
        y = y - y%DIGIT_HEIGHT;
        selectedCell = new Square(x, y);
        i = colorSquares.indexOf(selectedCell);
        if (i != -1)
            colorSquares.get(i).color = Color.YELLOW;
        this.repaint();
    }
    public void guess(int n) {
        int i = colorSquares.indexOf(selectedCell);
        if (i != -1) {
            logic.enterGuess(selectedCell.y/DIGIT_HEIGHT-1,selectedCell.x/DIGIT_WIDTH-1,n);
        }
        this.repaint();
    }
    public boolean checkGrid() {
        return logic.checkGrid();
    }

}
