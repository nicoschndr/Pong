import processing.core.PApplet;
import processing.core.PGraphics;

import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

public class Pong extends PApplet {

    int scoreLeft = 0;
    int scoreRight = 0;

    float ballSize = 40f;

    Ball ball = new Ball(255, super.width, super.height,400,400);

    Racket racketRight;
    Racket racketLeft;



    static boolean server = false;
    static boolean client = false;

    private ClientServerThread thread;

    public static Pong newServer(String ip, int port) throws IOException {
        var p = new Pong();
        try {
            p.thread = ClientServerThread.newServer(ip, port,p);
            p.thread.start();
            server = true;
        }catch(IOException e){
            newClient(ip, port,p);
        }
        return p;
    }

    public static Pong newClient(String ip, int port, Pong p) {
        p.thread = ClientServerThread.newClient(ip, port, p);
        p.thread.start();
        client = true;
        return p;
    }


    public void settings(){
        size(1000,800);
    }

    public void setup(){
        background(0);
        racketRight = new Racket(super.width-50,super.height/2-60,255,15,120);
        racketLeft = new Racket(35,super.height/2-60,255,15,120);
        if(server) {
            randomPos();
        }
    }
    public void draw(){
            moveRacket();
            checkCollision();
            ball.checkRacketCollision(racketRight);
            ball.checkRacketCollision(racketLeft);
            racketLeft.topCollision();
            racketRight.topCollision();
            super.background(0);
            textSize(100);
            text(scoreLeft, 200, 80);
            text(scoreRight, 800, 80);
            for (int i = 0; i < 10; i++) {
                int middleRectSizeX = 10;
                int middleRectSizeY = 60;
                int middleRectX = super.width / 2;
                int middleRectY = 10;
                fill(40);
                rect(middleRectX, middleRectY + i * 90, middleRectSizeX, middleRectSizeY);
            }
            if (server) {
                sendBallServer();
                sendScore();
            }
            if (thread.isConnected()) {
                ball.draw(super.g);
            }
            racketRight.draw(super.g);
            racketLeft.draw(super.g);

    }

    void checkCollision(){
        if (ball.x  > super.width - ballSize/2) {
            destroyBall();
            scoreLeft();
        }
        if(ball.x < ballSize/2){
            destroyBall();
            scoreRight();
        }
        if (ball.y + ball.yVel > super.height - 40f/2 || ball.y + ball.yVel  < 40f/2) {
            ball.yVel = -ball.yVel;
        }
    }

        void randomPos () {
        ball.x = (int) super.width / 2;
        ball.y = (int) (Math.random() * (super.height - 40f)) + (int) 40f / 2;

        int leftOrRight = (int) (Math.random() * 2);
        if (leftOrRight == 0) {
            ball.xVel = 8;
            ball.yVel = 8;
        }
        if (leftOrRight == 1) {
            ball.xVel = -8;
            ball.yVel = -8;
        }
    }

    public void scoreLeft(){
        scoreLeft++;
    }

    public void scoreRight(){
        scoreRight++;
    }

    public void destroyBall(){
        ball = null;
        ball = new Ball(255, super.width, super.height,400, 400);
        randomPos();
    }

    public void moveRacket(){
        if(keyPressed) {
            if(server) {
                if (racketLeft.top) {
                    if (key == 's') {
                        racketLeft.y = racketLeft.y + 10;
                        sendPosServer();
                    }
                } else if (racketLeft.bot) {
                    if (key == 'w') {
                        racketLeft.y = racketLeft.y - 10;
                        sendPosServer();
                    }
                } else {
                    if (key == 'w') {
                        racketLeft.y = racketLeft.y - 10;
                        sendPosServer();
                    }
                    if (key == 's') {
                        racketLeft.y = racketLeft.y + 10;
                        sendPosServer();
                    }
                }
            }if(client){
                if (racketRight.top) {
                    if (key == 's') {
                        racketRight.y = racketRight.y + 10;
                        sendPosClient();
                    }
                } else if (racketRight.bot) {
                    if (key == 'w') {
                        racketRight.y = racketRight.y - 10;
                        sendPosClient();
                    }
                } else {
                    if (key == 'w') {
                        racketRight.y = racketRight.y - 10;
                        sendPosClient();
                    }
                    if (key == 's') {
                        racketRight.y = racketRight.y + 10;
                        sendPosClient();
                    }
                }
            }
        }
    }

    public void sendPosServer(){
        if(server){
            Point p = new Point(racketLeft.x, racketLeft.y);
            thread.offerTrade(p);
        }
    }


    public void sendBallServer(){
        if(server) {
            Ball b = new Ball(255, super.width, super.height,ball.x,ball.y);
            thread.ball(b);
        }
    }

    public void setNewBallPos(Ball b){
        if(client){
            ball =b;
        }
    }

    public void setNewPosClient(Point p){
        if(client){
            racketLeft.x = p.x;
            racketLeft.y = p.y;
        }
    }

    public void sendPosClient(){
        if(client){
            Racket r = new Racket(racketRight.x, racketRight.y,255,15,120);
            thread.offerRacket(r);
        }
    }

    public void setNewPosServer(Racket p){
        if(server){
            racketRight.x = p.x;
            racketRight.y = p.y;
        }
    }

    public void sendScore(){
        if(server){
            Score s = new Score(scoreLeft,scoreRight);
            thread.offerScore(s);
        }
    }

    public void setNewScoreClient(Score s){
        if(client){
            scoreLeft = s.left;
            scoreRight = s.right;
        }
    }
}