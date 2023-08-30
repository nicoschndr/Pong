import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.*;

public class ClientServerThread extends Thread{
    private ServerSocket serversocket;
    private Socket socket;
    private Pong pong;
    private ObjectOutputStream oos;

    static InetAddress inetAddress;
    static SocketAddress socketAddress;

    public static ClientServerThread newServer(String ip, int port, Pong pong) throws IOException {
        var cst = new ClientServerThread(pong);
        try {
            cst.serversocket = new ServerSocket(port);
        } catch (IOException e) {
            throw e;
        }
        return cst;
    }

    private ClientServerThread(Pong p) {this.pong = p;}

    public static ClientServerThread newClient(String ip, int port, Pong pong){
        var cst = new ClientServerThread(pong);
        try{
            cst.socket = new Socket(ip, port);
            cst.oos = new ObjectOutputStream(cst.socket.getOutputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return cst;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void offerTrade (Point p){
        try{
            if(oos != null)
                oos.writeObject(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void offerScore(Score s){
        try{
            if(oos != null)
                oos.writeObject(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void offerRacket (Racket r){
        try{
            if(oos != null)
                oos.writeObject(r);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void ball (Ball b){
        try{
            if(oos != null)
                oos.writeObject(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            // If this is a server accept one client
            if(socket == null) {
                socket = serversocket.accept();
                oos = new ObjectOutputStream(socket.getOutputStream());
            }

            // Read objects
            var ois = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object obj = ois.readObject();
                if(obj instanceof Point) {
                    pong.setNewPosClient((Point)obj);
                }
                if(obj instanceof Ball) {
                    pong.setNewBallPos((Ball)obj);
                }
                if(obj instanceof Racket){
                    pong.setNewPosServer((Racket)obj);
                }
                if(obj instanceof Score){
                    pong.setNewScoreClient((Score)obj);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
