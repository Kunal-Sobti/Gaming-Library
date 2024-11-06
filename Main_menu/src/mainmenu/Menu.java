package mainmenu;
import flappybird.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Thread;

public class Menu {

    public Menu() {
        
        JFrame frame = new JFrame("Game Library 🏓");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 350);
        frame.setLayout(new GridLayout(2, 1)); 

        ImageIcon gamepadIcon= new ImageIcon(getClass().getResource("/mainmenu/Images/Game_Icon.png"));
        frame.setIconImage(gamepadIcon.getImage());
        
        // flappy bird button
        JButton flappyBirdButton = new JButton("Play Flappy Bird");
        
        Font commonFont = new Font("SansSerif", Font.BOLD | Font.ITALIC, 25);
        flappyBirdButton.setFont(commonFont);
        
        Color colorForFlappy= new Color(120, 200, 213);
        flappyBirdButton.setBackground(colorForFlappy);
        
        // ping pong button
        JButton pingPongButton = new JButton("Play Ping Pong");
        
        pingPongButton.setFont(commonFont);

        Color colorForPong= new Color(249, 47, 48);
        pingPongButton.setBackground(colorForPong);

        frame.add(flappyBirdButton);
        frame.add(pingPongButton);

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
                src.PongGame.main(null);
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
