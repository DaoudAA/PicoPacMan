package home.road;

import java.awt.*;
import java.util.HashSet;

public class MovableBlock extends Block{
    char direction = 'U';
    int velocityX = 0 ;
    int velocityY= 0;

    MovableBlock(Image image, int x, int y, int width, int height) {
        super(image, x, y, width, height);
    }

    void updateDirection(char direction, HashSet<Block> walls, int tileSize) {
        char prevDirection = this.direction;
        this.direction = direction;
        updateVelocity(tileSize);
        this.x += this.velocityX;
        this.y += this.velocityY;
        for (Block wall : walls) {
            if (this.collision(wall)) {
                this.x -= this.velocityX;
                this.y -= this.velocityY;
                this.direction = prevDirection;
                updateVelocity(tileSize);
            }
        }
    }
    void updateVelocity(int tileSize) {
        if (this.direction == 'U') {
            this.velocityX = 0;
            this.velocityY = -tileSize/4;
        }
        else if (this.direction == 'D') {
            this.velocityX = 0;
            this.velocityY = tileSize/4;
        }
        else if (this.direction == 'L') {
            this.velocityX = -tileSize/4;
            this.velocityY = 0;
        }
        else if (this.direction == 'R') {
            this.velocityX = tileSize/4;
            this.velocityY = 0;
        }
    }
    public boolean collision(Block b) {
        return  this.x < b.x + b.width &&
                this.x + this.width > b.x &&
                this.y < b.y + b.height &&
                this.y + this.height > b.y;
    }
    void reset() {
        this.x = this.startX;
        this.y = this.startY;
    }
}
