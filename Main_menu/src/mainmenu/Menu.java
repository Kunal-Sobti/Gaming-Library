package mainmenu;
import flappybird.App;
import pacman.GamePacman;
import pingpong.PongGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.lang.Thread;

public class Menu {

    Clip buttonPress= null;
    
    public Menu() {
        
        // Sound Track
        class SoundTrack {
            Clip clip = null;
            AudioInputStream audioStream;

            SoundTrack(String audio) {
                try {
                    InputStream soundFile = getClass().getResourceAsStream(audio);
                    if (soundFile == null) {
                        System.out.println("Sound file not found: " + audio);
                        return;
                    }
                    audioStream = AudioSystem.getAudioInputStream(soundFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Clip getClip() {
                return clip;
            }
        }
        
        if(buttonPress == null){
            buttonPress= new SoundTrack("./Sound/button-push.wav").getClip();
        }

        JFrame frame = new JFrame("🕹️🎯 Game Library 🎮⭕");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 550);
        frame.setLayout(new GridLayout(2, 2)); 

        ImageIcon gamepadIcon= new ImageIcon(getClass().getResource("/mainmenu/Images/Game_Icon.png"));
        frame.setIconImage(gamepadIcon.getImage());
        
        // flappy bird button
        JButton flappyBirdButton = new JButton("Play Flappy Bird 🐥🐥");
        
        Font commonFont = new Font("SansSerif", Font.BOLD | Font.ITALIC, 35);
        flappyBirdButton.setFont(commonFont);
        
        Color colorForFlappy= new Color(79, 184, 88);
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

        // ticTac button
        JButton ticTacButton= new JButton("Play Tic-Tac-Toe ❌⭕");
        ticTacButton.setFont(commonFont);

        Color colorForTictac= new Color(12, 89, 255);
        ticTacButton.setBackground(colorForTictac);

        frame.add(flappyBirdButton);
        frame.add(pingPongButton);
        frame.add(pacmanButton);
        frame.add(ticTacButton);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null); 

        flappyBirdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonSound();
                flappyBirdGame();
            }
        });

        pingPongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonSound();
                pingPongGame();
            }
        });

        pacmanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                buttonSound();
                pacmanGame();
            }
        });

        ticTacButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                buttonSound();
                ticTacToeGame();
            }
        });
    }

    private void buttonSound(){
        buttonPress.setFramePosition(0);
        buttonPress.start();
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

    private void ticTacToeGame(){
        Thread Game= new Thread(() -> {
            try {
                tictactoe.TicTacGame.main(null);
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
