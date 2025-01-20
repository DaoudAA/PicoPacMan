package home.road;

import java.awt.*;

public class Block {
    int x ;
    int y ;
    int width;
    int height;
    Image image;
    int startX;
    int startY;


    Block(Image image , int x , int y , int width , int height){
        this.image = image ;
        this.height = height;
        this.width = width ;
        this.y = y ;
        this.x = x ;
        this.startY = y ;
        this.startX = x ;
    }


}
