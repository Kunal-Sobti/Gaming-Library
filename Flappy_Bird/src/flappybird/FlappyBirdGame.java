package flappybird;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.InputStream;

import javax.sound.sampled.*;
import javax.swing.*;

public class FlappyBirdGame extends JPanel implements ActionListener, KeyListener{
    int boardWidth= 360;
    int boardHeight= 640;

    // Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

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

    // Bird
    int BirdX= boardWidth/8;
    int BirdY= boardHeight/2;
    int birdWidth= 34;
    int birdHeight= 24;
    
    class Bird{
        int x= BirdX;
        int y= BirdY;
        int width= birdWidth;
        int height= birdHeight;
        Image img;

        Bird(Image img){
            this.img= img;
        }
    }

    // Pipes
    int pipeX= boardWidth;
    int pipeY= 0;
    int pipeWidth= 64;
    int pipeHeight= 512;

    class Pipe{
        int x= pipeX;
        int y= pipeY;
        int width= pipeWidth;
        int height= pipeHeight;
        Image img;
        boolean passed= false;

        Pipe(Image img){
            this.img= img;
        }
    }

    // game logic
    Bird bird;
    int velocityX= -4;      // velocity of pipe
    int velocityY= 0;
    int gravity= 1;

    ArrayList<Pipe> pipes;
    Random random= new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver= false;
    double score= 0;
    int level=0;            // used to maintain speed of pipes
    FlappyBirdGame(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);             // ensures jpanel in focus for input
        addKeyListener(this);               // object acts as listener to register input from user

        // load images
        backgroundImg= new ImageIcon(getClass().getResource("/flappybird/Images/flappybirdbg.png")).getImage();
        birdImg= new ImageIcon(getClass().getResource("/flappybird/Images/flappybird.png")).getImage();
        topPipeImg= new ImageIcon(getClass().getResource("/flappybird/Images/toppipe.png")).getImage();
        bottomPipeImg= new ImageIcon(getClass().getResource("/flappybird/Images/bottompipe.png")).getImage();

        // bird
        bird= new Bird(birdImg);
        pipes= new ArrayList<Pipe>();

        // place pipes timer
        placePipesTimer= new Timer(1500,new ActionListener() {
            // every 1.5 sec placePipes is called
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();

        // game timer
        gameLoop= new Timer(1000/60, this);   // 1000/60 = 60 frames per sec, this refer to actionPerform.
        gameLoop.start(); 

    }

    public void placePipes(){
        // getting random pipe height , (0-1)*(256[pipeheight/2]) => (0-256)
        // pipeHeight/4= 128, (0-128- (0-256)) ---> 1/4 to 3/4 pipeHeight
        int randomPipeY= (int)((pipeY-pipeHeight/4- Math.random()*(pipeHeight/2)));
        int openingSpace= boardHeight/4;

        Pipe topPipe= new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    
        Pipe bottomPipe= new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
        
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        // backgroung
        g.drawImage(backgroundImg, 0, 0, boardWidth,boardHeight,null);

        // bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe= pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // score
        g.setColor(Color.white);
        g.setFont(new Font("SansSerif",Font.PLAIN,32));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf((int) score), 80, 280);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y= Math.max(0,bird.y);

        // pipes
        for(int i=0;i<pipes.size();i++){
            Pipe pipe= pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                Clip pointClip = null;
                if(pointClip == null){
                    pointClip= new SoundTrack("/flappybird/Sound/point.wav").getClip();
                }
                if(pointClip != null){
                    pointClip.start();
                }
                pipe.passed= true;

                score += 0.5;       // for both pipe
                int val= (int)(Math.floor(score));
                if( val != level){
                    level= val;
                    System.out.println("score: "+ level);
                    if( level% 35 == 0 ){
                        velocityX -=1;
                    }
                }
            }

            if(collision(bird, pipe)){
                gameOver= true;
            }
        }

        if(bird.y > boardHeight){
            gameOver= true;
        }
    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height &&
        a.y + a.height > b.y;
    }

    // actions performed 60 times a sec
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        // call paint
        repaint();
        if(gameOver){
            Clip dieClip= null;
            if(dieClip == null){
                dieClip= new SoundTrack("/flappybird/Sound/die.wav").getClip();
            }
            if(dieClip != null){
                dieClip.start();
            }
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }
    
    // input from user, key listener
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            Clip spaceClip= null;
            if(spaceClip == null) {
                spaceClip= new SoundTrack("/flappybird/Sound/flap.wav").getClip();
            }
            if(spaceClip != null){
                spaceClip.start();
            }
            velocityY = -9;
            if(gameOver){
                // restart game by resetting condition
                bird.y= BirdY;
                velocityY=0;
                velocityX= -4;
                pipes.clear();
                score= 0;
                gameOver= false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
