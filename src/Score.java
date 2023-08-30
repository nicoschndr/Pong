import java.io.Serializable;

public class Score implements Serializable {
    int left;
    int right;

    public Score(int left, int right){
        this.left=left;
        this.right=right;
    }
}
