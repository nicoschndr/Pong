import processing.core.PApplet;

import java.io.IOException;
import java.net.UnknownHostException;

abstract class MainServer {
        public static void main(String[] args) throws IOException {
            var p = Pong.newServer("localhost", 8080);
            PApplet.runSketch(new String[]{"Pong"}, p);
        }
}
