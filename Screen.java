package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Screen {
    private final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    private final Font DIGIT_FONT = new Font("Courier New", Font.PLAIN, 19);
    private final int WIDTH  = 1000;
    private final int HEIGHT = 700;

    private final int numDigits = 9;
    private final int NUMPAD_SIZE = 12;

    private JFrame gameFrame;
    private JPanel numPad;
    private JButton[] buttNumbers;
    private JTextField[][] digits = new JTextField[numDigits][numDigits];
    private LogicProcessor logic;
    private GameRenderer renderer;


    public Screen () {
        gameFrame = makeGameFrame();
        gameFrame.setVisible(true);
        gameFrame.setSize(new Dimension(WIDTH, HEIGHT));
        gameFrame.setLocation((dim.width - WIDTH)/2, (dim.height - HEIGHT)/2);
        gameFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        gameFrame.setResizable(true);

        KeyHandler keyHandle = new KeyHandler();
        gameFrame.addKeyListener(keyHandle);

    }

    private JFrame makeGameFrame() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        renderer = new GameRenderer();
        MouseHandler mouseHandle = new MouseHandler();
        renderer.addMouseListener(mouseHandle);
        JPanel numPadHolder = new JPanel();
        numPadHolder.setLayout(new GridLayout(4,1));
        numPadHolder.add(new Panel());
        numPadHolder.add(CreateNumPad());
        frame.add(renderer);
        frame.add(numPadHolder, BorderLayout.EAST);

        return frame;
    }
    private JPanel CreateNumPad(){
        //This module creates a numpad for the application window
        NumPadHandler handler = new NumPadHandler();
        buttNumbers = new JButton[NUMPAD_SIZE];
        numPad = new JPanel();
        numPad.setLayout(new GridLayout(4, 3, 10, 5));
        buttNumbers[9] = new JButton("New Puzzle");
        buttNumbers[10] = new JButton("Check");
        buttNumbers[11] = new JButton("Clear");
        for (int i = 0; i < NUMPAD_SIZE; i++){
            int n = i+1;
            if (i < NUMPAD_SIZE - 3){
                buttNumbers[i]  = new JButton(n+"");
            }
            buttNumbers[i].setFont(DIGIT_FONT);
            buttNumbers[i].addActionListener(handler);
            numPad.add(buttNumbers[i]);
        }
        return numPad;
    }
    private class NumPadHandler implements ActionListener {
        //This class manages action related to the number pad
        public void actionPerformed(ActionEvent event){
            for (int i = 0; i < NUMPAD_SIZE; i++){
                if (event.getSource() == buttNumbers[i]){
                    switch (i){
                        case 0: case 1:
                        case 2: case 3:
                        case 4: case 5:
                        case 6: case 7:
                        case 8:
                            int n = i+1;
                            renderer.guess(n);
                            break;
                        case 9:
                            System.out.println("New Grid");
                            renderer.newGrid();
                            break;
                        case 10:
                            System.out.println("Check");
                            String msg = "Nope";
                            if (renderer.checkGrid()) {
                                msg = "Yep";
                            }
                            JOptionPane.showMessageDialog(null, msg);
                            break;
                        case 11:
                            System.out.println("11");
                            renderer.guess(0);
                    }
                }
            }
        }
    }
    private class KeyHandler implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) { }
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            System.out.println(keyEvent.getKeyChar());
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_9:
                    renderer.guess(9);
                    break;
                case KeyEvent.VK_8:
                    renderer.guess(8);
                    break;
                case KeyEvent.VK_7:
                    renderer.guess(7);
                    break;
                case KeyEvent.VK_6:
                    renderer.guess(6);
                    break;
                case KeyEvent.VK_5:
                    renderer.guess(5);
                    break;
                case KeyEvent.VK_4:
                    renderer.guess(4);
                    break;
                case KeyEvent.VK_3:
                    renderer.guess(3);
                    break;
                case KeyEvent.VK_2:
                    renderer.guess(2);
                    break;
                case KeyEvent.VK_1:
                    renderer.guess(1);
                    break;
            }
        }
        @Override
        public void keyReleased(KeyEvent keyEvent) { }
    }
    private class MouseHandler implements MouseListener {
        public void mouseReleased(MouseEvent event) {
            renderer.selectCell(event.getX(),event.getY());
        }
        public void mousePressed(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e){ }
        public void mouseClicked(MouseEvent e){ }
    }
    public static void main(String[] args) {
        Screen app = new Screen();
    }
}
