import processing.core.PGraphics;

import java.io.Serializable;

public class Racket implements Serializable{

    int x, y;
    int fillColor;
    int racketSizeX;
    int racketSizeY;
    boolean top = false;
    boolean bot = false;

    Racket(int x, int y, int fillColor, int racketSizeX, int racketSizeY){
        this.x = x;
        this.y = y;
        this.fillColor=fillColor;
        this.racketSizeX=racketSizeX;
        this.racketSizeY=racketSizeY;
    }

    void draw(PGraphics g) {
        g.fill(this.fillColor);
        g.rect(this.x, this.y, racketSizeX, racketSizeY);
    }

    void topCollision() {
        if (y <= 0) {
            top = true;
        }
        if(y > 0){
            top = false;
        }
        if(y >= 800 - racketSizeY){
            bot = true;
        }
        if(y < 800 - racketSizeY){
            bot = false;
        }
    }
}
