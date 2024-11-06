package flappybird;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth= 360;
        int boardHeight= 640;
        
        JFrame frame= new JFrame("Flappy Bird");
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        FlappyBirdGame fBird= new FlappyBirdGame();
        frame.add(fBird);
        frame.pack();           //frame.pack does not include title bar
        fBird.requestFocus();

        frame.setVisible(true);
    }
}
