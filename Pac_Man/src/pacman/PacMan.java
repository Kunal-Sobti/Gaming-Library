package pacman;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Random;

public class PacMan extends JPanel implements ActionListener, KeyListener{
    
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;

        int startX;
        int startY;

        char direction= 'U';
        int velocityX=0;
        int velocityY=0;

        Block(Image img, int x,int y, int height, int width){
            this.img= img;
            this.x= x;
            this.y= y;
            this.height= height;
            this.width= width;

            this.startX= x;
            this.startY= y;
        }

        void updateDirection(char direction){
            // this refer to object of block class like ghost,pacman
            char prevDirection= this.direction;
            this.direction= direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall: walls){
                if(collision(this,wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction= prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){      // velocity per frame
            if(this.direction == 'U'){
                this.velocityX= 0;
                this.velocityY = -tileSize/4;
            }
            else if(this.direction == 'D'){
                this.velocityX= 0;
                this.velocityY = tileSize/4;
            }
            else if(this.direction == 'L'){
                this.velocityY= 0;
                this.velocityX= -tileSize/4;
            }
            else if(this.direction == 'R'){
                this.velocityY= 0;
                this.velocityX= tileSize/4;
            }
        }

        void reset(){
            this.x= startX;
            this.y= startY;
        }
    }

    // Sound Track
    Clip eatClip = null;
    Clip dieClip = null;
    Clip levelUpClip = null;
    Clip ghostEat = null;
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
    private int rowCount= 21;
    private int columnCount= 19;
    private int tileSize= 32;
    private int boardWidth= columnCount*tileSize;
    private int boardHeight= rowCount*tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "XXXX X       X XXXX",
        "X    X XXrXX X    X",
        "X       bpo       X",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;


    Timer gameLoop;
    char[] directions= {'U','D','L','R'};    
    Random random= new Random();
    int score= 0;
    int lives = 3;
    boolean gameOver= false;


    PacMan(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.black);
        addKeyListener(this);       // pacman object listens to key presses
        setFocusable(true); //when window focused only when listens
        // load image
        wallImage= new ImageIcon(getClass().getResource("./Images/wall.png")).getImage();
        blueGhostImage= new ImageIcon(getClass().getResource("./Images/blueGhost.png")).getImage();
        orangeGhostImage= new ImageIcon(getClass().getResource("./Images/orangeGhost.png")).getImage();
        redGhostImage= new ImageIcon(getClass().getResource("./Images/redGhost.png")).getImage();
        pinkGhostImage= new ImageIcon(getClass().getResource("./Images/pinkGhost.png")).getImage();

        pacmanUpImage= new ImageIcon(getClass().getResource("./Images/pacmanUp.png")).getImage();
        pacmanDownImage= new ImageIcon(getClass().getResource("./Images/pacmanDown.png")).getImage();
        pacmanLeftImage= new ImageIcon(getClass().getResource("./Images/pacmanLeft.png")).getImage();
        pacmanRightImage= new ImageIcon(getClass().getResource("./Images/pacmanRight.png")).getImage();

        loadMap();
        for(Block ghost: ghosts){
            // move ghost when pacman object is created by assigning random direction
            char newDirection= directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        // this refers to pacman object which has properties of ActionListener
        // 1000/50 = 20fps, here ActionPerformed is called after 50ms 
        gameLoop= new Timer(50,this);   
        gameLoop.start();
    }

    public void loadMap(){
        walls= new HashSet<Block>();
        foods= new HashSet<Block>();
        ghosts= new HashSet<Block>();

        for(int r=0; r < rowCount; r++){
            for(int c=0; c < columnCount; c++){
                String row= tileMap[r];
                char tileMapChar= row.charAt(c);

                int x= c*tileSize;
                int y= r*tileSize;

                if(tileMapChar == 'X'){     // block wall
                    Block wall= new Block(wallImage,x,y,tileSize,tileSize);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b'){        // blue ghost
                    Block ghost= new Block(blueGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'o'){        // orange ghost
                    Block ghost= new Block(orangeGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'p'){        // pink ghost
                    Block ghost= new Block(pinkGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'r'){        // red ghost
                    Block ghost= new Block(redGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'P'){        // pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if(tileMapChar == ' '){        //food
                    Block food= new Block(null,x+14,y+14,4,4);
                    foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        // draw pacman
        g.drawImage(pacman.img, pacman.x, pacman.y,pacman.width,pacman.height, null);      
        
        // draw ghosts
        for(Block ghost: ghosts){
            g.drawImage(ghost.img, ghost.x, ghost.y,ghost.width,ghost.height ,null);
        }

        // draw wall
        for(Block wall: walls){
            g.drawImage(wall.img, wall.x, wall.y, wall.width,wall.height,null);
        }

        // draw food
        g.setColor(Color.WHITE);
        for(Block food: foods){
            g.fillRect( food.x, food.y, food.width,food.height);        // draw rectangle
        }

        // score
        g.setFont(new Font("ARIAL",Font.PLAIN,20));
        if(gameOver){
            g.setFont(new Font("sans serif",Font.BOLD,25));
            g.setColor(Color.RED);
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2, tileSize/2 + tileSize/3);
        }
        else{
            g.drawString("x"+String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2);
        }
    }

    // move method is called by gameloop, move() is for 1 single frame 
    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // check wall collision
        for(Block wall: walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // check ghost collision
        for(Block ghost: ghosts){

            if(collision(ghost, pacman)){

                if(ghostEat == null){
                    ghostEat= new SoundTrack("./Sound/pacman_eatghost.wav").getClip();
                }
                ghostEat.setFramePosition(0);
                ghostEat.start();
                
                lives -= 1;
                if(lives == 0){
                    
                    if(dieClip == null){
                        dieClip= new SoundTrack("./Sound/pacman_death.wav").getClip();
                    }
                    dieClip.setFramePosition(0);
                    dieClip.start();
    
                    gameOver= true;
                    return;
                }
                resetPositions();
            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            for(Block wall: walls){
                if(collision(ghost, wall)){
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;

                    char newDirection= directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
            
        }

        // check food collision
        Block foodEaten= null;
        for(Block food: foods){
            if(collision(pacman, food)){
                if(eatClip == null){
                    eatClip= new SoundTrack("./Sound/pacman_eatfruit.wav").getClip();
                }
                eatClip.setFramePosition(0);
                eatClip.start();
                
                foodEaten= food;
                score += 10;
            }
        }
        foods.remove(foodEaten);


        if(foods.isEmpty()){
            if(levelUpClip == null){
                levelUpClip= new SoundTrack("./Sound/pacman_beginning.wav").getClip();
            }
            levelUpClip.setFramePosition(0);
            levelUpClip.start();
            
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    public void resetPositions(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        for(Block ghost: ghosts){
            ghost.reset();
            char newDirection= directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }


    // used for gameloop, called after every 50ms
    @Override
    public void actionPerformed(ActionEvent e) {
        // calls paint 
        move();     // update position
        repaint();  // draw

        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // when gameover and keypressed reset
        if(gameOver){
            loadMap();
            resetPositions();
            lives= 3;
            score= 0;
            gameOver= false;
            gameLoop.start();
        }
        // System.out.println("keyEvent"+ e.getKeyCode());
        if(e.getKeyCode() == KeyEvent.VK_UP){
            pacman.updateDirection('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacman.updateDirection('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacman.updateDirection('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacman.updateDirection('R');
        }

        if(pacman.direction == 'U'){
            pacman.img= pacmanUpImage;
        }
        else if(pacman.direction == 'D'){
            pacman.img = pacmanDownImage;
        }
        else if(pacman.direction == 'L'){
            pacman.img = pacmanLeftImage;
        }
        else if(pacman.direction == 'R'){
            pacman.img = pacmanRightImage;
        }
    }
}