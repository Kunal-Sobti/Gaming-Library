package tictactoe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class TicTacToe {
        int boardWidth= 600;
        int boardHeight= 700;

        JFrame frame= new JFrame("Tic-Tac-Toe");
        JLabel textLabel= new JLabel();
        JPanel textPanel= new JPanel();
        JPanel boardPanel= new JPanel();

        JPanel resetPanel= new JPanel();
        JButton resetButton= new JButton("Reset Game");
        
        JButton[][] board= new JButton[3][3];
        String playerX= "X";
        String playerO= "O";
        String currentPlayer= playerX;

        boolean gameOver= false;
        int turns=0;

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
                    
                    BufferedInputStream bufferedStream = new BufferedInputStream(soundFile);
                    audioStream = AudioSystem.getAudioInputStream(bufferedStream);
                    
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

        TicTacToe(){
            frame.setVisible(true);
            frame.setSize(boardWidth,boardHeight);
            
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            textLabel.setBackground(Color.darkGray);
            textLabel.setForeground(Color.WHITE);
            textLabel.setFont(new Font("ARIAL",Font.BOLD,50));
            textLabel.setHorizontalAlignment(JLabel.CENTER);
            textLabel.setText("Tic-Tac-Toe");
            textLabel.setOpaque(true);

            textPanel.setLayout(new BorderLayout());
            textPanel.add(textLabel);
            frame.add(textPanel,BorderLayout.NORTH);

            boardPanel.setLayout(new GridLayout(3,3));
            boardPanel.setBackground(Color.DARK_GRAY);
            frame.add(boardPanel);

            resetPanel.setLayout(new BorderLayout());
            resetPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
            resetButton.setFont(new Font("ARIAL",Font.BOLD,50));
            resetPanel.add(resetButton, BorderLayout.CENTER);
            frame.add(resetPanel, BorderLayout.SOUTH);  

            // reset button
            resetButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    Clip clickClip = null;
                    if(clickClip == null){
                        clickClip= new SoundTrack("tick1.wav").getClip();
                    }
                    if(clickClip != null){
                        clickClip.start();
                    }
                    for(int r=0;r<3;r++){
                        for(int c=0;c<3;c++){
                            resetTile(board[r][c]);
                        }
                    }
                    textLabel.setText("Tic-Tac-Toe");
                    turns= 0;
                    gameOver= false;
                }
            });

            for(int r=0;r<3;r++){
                for(int c=0;c<3;c++){
                    JButton tile= new JButton();
                    board[r][c]= tile;
                    boardPanel.add(tile);
                    
                    tile.setBackground(Color.darkGray);
                    tile.setForeground(Color.white);
                    tile.setFont(new Font("ARIAL",Font.BOLD,120));
                    tile.setFocusable(false);

                    tile.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e){
                            if(gameOver) return;
                            
                            Clip clickClip = null;
                            if(clickClip == null){
                                clickClip= new SoundTrack("tick1.wav").getClip();
                            }
                            if(clickClip != null){
                                clickClip.start();
                            }


                            JButton tile= (JButton) e.getSource();
                            if(tile.getText() == ""){
                                tile.setText(currentPlayer);
                                turns += 1;
                                checkWinner();
                                if(!gameOver){
                                    currentPlayer= (currentPlayer == playerX ? playerO : playerX);
                                    textLabel.setText(currentPlayer+"'s turn");
                                }
                            }
                        }
                    });
                }   
            }
        }
        void checkWinner(){
            // horizontal
            for(int r=0;r<3;r++){
                if(board[r][0].getText() == "") continue;

                if(board[r][0].getText() == board[r][1].getText() && board[r][0].getText() == board[r][2].getText()){
                    for(int i=0;i<3;i++){
                        setWinner(board[r][i]);
                    }
                    gameOver= true;
                    return;
                }
            }
            
            // vertical
            for(int c=0;c<3;c++){
                if(board[0][c].getText() == "") continue;

                if(board[0][c].getText() == board[1][c].getText() && board[2][c].getText() == board[1][c].getText()){
                    for(int i=0;i<3;i++){
                        setWinner(board[i][c]);
                    }
                    gameOver= true;
                    return;
                }
            }

            // diagonally
            if(board[0][0].getText() == board[1][1].getText() && board[2][2].getText() == board[1][1].getText()
                && board[0][0].getText() != ""){
                    for(int i=0;i<3;i++){
                        setWinner(board[i][i]);
                    }
                    gameOver= true;
                    return;
            }

            // anti-diagonally
            if(board[0][2].getText() == board[1][1].getText() && board[2][0].getText() == board[1][1].getText()
                && board[0][2].getText() != ""){
                    for(int i=0;i<3;i++){
                        setWinner(board[i][3-i-1]);
                    }
                    gameOver= true;
                    return;
            }
            if(turns == 9){
                for(int r=0;r<3;r++){
                    for(int c=0;c<3;c++){
                        setTie(board[r][c]);
                    }
                }
                gameOver= true;
            }
        }

        void setWinner(JButton tile){
            tile.setBackground(Color.gray);
            tile.setForeground(Color.green);
            textLabel.setText(currentPlayer+" is the winner!");
        }

        void setTie(JButton tile){
            tile.setBackground(Color.gray);
            tile.setForeground(Color.orange);
            textLabel.setText(" Tie!");
        }

        void resetTile(JButton tile){
            tile.setBackground(Color.darkGray);
            tile.setForeground(Color.WHITE);
            tile.setText("");
        }
}
