import processing.core.PGraphics;

import java.io.Serializable;

public class Ball implements Serializable {
    int x, y;
    int fillColor;
    int widthBound, heightBound;
    float ballSize = 40f;
    float xVel, yVel;


    Ball(int fillColor, int widthBound, int heightBound, int x, int y) {
        this.fillColor = fillColor;
        this.widthBound  = widthBound;
        this.heightBound = heightBound;
        this.x = x;
        this.y = y;
    }

    void draw(PGraphics g) {
        g.fill(this.fillColor);
        g.ellipse(this.x, this.y, ballSize, ballSize);
        x += xVel;
        y += yVel;
    }


    void checkRacketCollision(Racket other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        if(x + 3 * xVel >= other.x && x + 3 * xVel <=other.x + other.racketSizeX && y + yVel >= other.y && y + yVel <= other.y + other.racketSizeY) {
            this.xVel = -xVel;
        }
    }
}