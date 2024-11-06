package mainmenu;
import flappybird.App;
import pacman.GamePacman;
import pingpong.PongGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;

public class Menu {

    public Menu() {
        
        JFrame frame = new JFrame("Game Library 🕹️🎯🎮");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLayout(new GridLayout(3, 1)); 

        ImageIcon gamepadIcon= new ImageIcon(getClass().getResource("/mainmenu/Images/Game_Icon.png"));
        frame.setIconImage(gamepadIcon.getImage());
        
        // flappy bird button
        JButton flappyBirdButton = new JButton("Play Flappy Bird 🦅🦅");
        
        Font commonFont = new Font("SansSerif", Font.BOLD | Font.ITALIC, 35);
        flappyBirdButton.setFont(commonFont);
        
        Color colorForFlappy= new Color(120, 200, 213);
        flappyBirdButton.setBackground(colorForFlappy);
        
        // ping pong button
        JButton pingPongButton = new JButton("Play Ping Pong 🏓🏓");
        
        pingPongButton.setFont(commonFont);

        Color colorForPong= new Color(249, 47, 48);
        pingPongButton.setBackground(colorForPong);

        // PacMan button
        JButton pacmanButton= new JButton("Play Pac-Man 🎮🕹️");
        pacmanButton.setFont(commonFont);

        Color colorForPacman= new Color(247, 247, 1);
        pacmanButton.setBackground(colorForPacman);

        frame.add(flappyBirdButton);
        frame.add(pingPongButton);
        frame.add(pacmanButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null); 

        flappyBirdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flappyBirdGame();
            }
        });

        pingPongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pingPongGame();
            }
        });

        pacmanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                pacmanGame();
            }
        });
    }

    private void flappyBirdGame() {

        Thread Game= new Thread(() -> {
            try {
                App.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Game.setDaemon(false); 
        Game.start();
    }

    private void pingPongGame() {

        Thread Game= new Thread(() -> {
            try {
                pingpong.PongGame.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Game.setDaemon(false);
        Game.start();
    }

    private void pacmanGame(){
        Thread Game= new Thread(() -> {
            try {
                pacman.GamePacman.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Game.setDaemon(false);
        Game.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Menu::new);      // seperate menu in independent thread
    }
}
