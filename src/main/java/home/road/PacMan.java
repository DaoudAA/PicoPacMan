package home.road;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;
import java.util.Random;

public class PacMan extends  JPanel implements ActionListener, KeyListener{
    private final int rewCount = 21 ;
    private final int columnCount = 19;
    private final int tileSize = 32 ;
    private final int boardWidth = columnCount * tileSize ;

    private final Image wallImage;

    private final Image pinkGhostImage;
    private final Image blueGhostImage;
    private final Image orangeGhostImage;
    private final Image redGhostImage;

    private final Image pacmanDownImage;
    private final Image pacmanUpImage;
    private final Image pacmanLeftImage;
    private final Image pacmanRightImage;

    //private Image scaredGhostImage;

    HashSet<Block> walls ;
    HashSet<Block> foods ;
    HashSet<MovableBlock> ghosts ;
    MovableBlock pico;
    Timer gameloop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;
    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
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
    PacMan(){
        int boardHeight = rewCount * tileSize;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        wallImage =  new ImageIcon(getClass().getResource("/images/wall.png")).getImage();

        blueGhostImage = new ImageIcon(getClass().getResource("/images/blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("/images/orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("/images/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("/images/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("/images/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("/images/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/images/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/images/pacmanRight.png")).getImage();

        //scaredGhostImage = new ImageIcon(getClass().getResource("/images/scaredGhost.png")).getImage();

        loadMap();
        for (MovableBlock ghost : ghosts) {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection, walls, tileSize);
        }
        gameloop = new Timer(60, this);
        gameloop.start();
    }
    public void loadMap(){
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for(int r = 0 ; r < rewCount ; r++){
            for(int c = 0 ; c < columnCount ; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);
                int x = c*tileSize;
                int y = r*tileSize;

                switch (tileMapChar){
                    case 'X':
                        Block wall = new Block(wallImage,x,y,tileSize,tileSize);
                        walls.add(wall);
                        break;
                    case ' ':
                        Block food = new Block(null,x+14,y+14,5,5);
                        foods.add(food);
                        break;
                    case 'o':
                        MovableBlock orangeGhost = new MovableBlock(orangeGhostImage,x,y,tileSize,tileSize);
                        ghosts.add(orangeGhost);
                        break;
                    case 'b':
                        MovableBlock blueGhost = new MovableBlock(blueGhostImage,x,y,tileSize,tileSize);
                        ghosts.add(blueGhost);
                        break;
                    case 'p':
                        MovableBlock pinkGhost = new MovableBlock(pinkGhostImage,x,y,tileSize,tileSize);
                        ghosts.add(pinkGhost);
                        break;
                    case 'r':
                        MovableBlock redGhost = new MovableBlock(redGhostImage,x,y,tileSize,tileSize);
                        ghosts.add(redGhost);
                        break;
                    default:
                        pico = new MovableBlock(pacmanRightImage, x, y, tileSize, tileSize);
                }
            }
        }
    }
    public void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pico.image, pico.x,pico.y,pico.width,pico.height,null);
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize/2, tileSize/2);
        }
        else {
            g.drawString("x" + lives + " Score: " + score, tileSize/2, tileSize/2);
        }
    }

    public void move(){
        if(pico.y == tileSize*9 &&
                ((pico.direction == 'R' && pico.x == tileSize*19)||
                        (pico.direction == 'L' && pico.x == 0))){
            pico.teleportPortal(tileSize);
        }
        pico.x += pico.velocityX;
        pico.y += pico.velocityY;
        for (Block wall : walls) {
            if (pico.collision(wall)) {
                pico.x -= pico.velocityX;
                pico.y -= pico.velocityY;
                break;
            }
        }
        for (MovableBlock ghost : ghosts) {
            if (ghost.collision(pico)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if (ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U', walls, tileSize);
            }
            if(ghost.y == tileSize*9 &&
                    ((ghost.direction == 'R' && ghost.x == tileSize*19)||
                            (ghost.direction == 'L' && ghost.x == 0))){
                ghost.teleportPortal(tileSize);
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for (Block wall : walls) {
                if (ghost.collision(wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection, walls, tileSize);
                }
            }
        }
        Block foodEaten = null;
        for (Block food : foods) {
            if (pico.collision( food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }
    public void resetPositions() {
        pico.reset();
        pico.velocityX = 0;
        pico.velocityY = 0;
        for (MovableBlock ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection, walls, tileSize);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("key released : "+ e.getKeyCode());
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameloop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pico.updateDirection('U', walls, tileSize);
            pico.image = pacmanUpImage;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pico.updateDirection('D', walls, tileSize);
            pico.image = pacmanDownImage;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pico.updateDirection('L', walls, tileSize);
            pico.image = pacmanLeftImage;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pico.updateDirection('R', walls, tileSize);
            pico.image = pacmanRightImage;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

//        if (pacman.direction == 'U') {
//        }
//        else if (pacman.direction == 'D') {
//        }
//        else if (pacman.direction == 'L') {
//        }
//        else if (pacman.direction == 'R') {
//        }
    }
}
