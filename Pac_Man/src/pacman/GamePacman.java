package pacman;
import javax.swing.*;

public class GamePacman {
    public static void main(String[] args) throws Exception {
        int rowCount= 21;
        int columnCount= 19;
        int tileSize= 32;
        int boardWidth= columnCount*tileSize;
        int boardHeight= rowCount*tileSize;

        JFrame frame= new JFrame("Pac Man");
        
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);          // window in centre of screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        PacMan pacManGame= new PacMan();
        frame.add(pacManGame);
        frame.pack();       // ensures we get full panel in window 
        pacManGame.requestFocus();
        frame.setVisible(true);
    }
}
