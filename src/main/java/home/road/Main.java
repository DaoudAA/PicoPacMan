package home.road;
import javax.swing.JFrame;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int rewCount = 21 ;
        int columnCount = 19;
        int tileSize = 32 ;
        int boardWidth = columnCount * tileSize ;
        int boardHeight = rewCount * tileSize ;

        JFrame jFrame = new JFrame("PAca MAn Level 1 ") ;
        //jFrame.setVisible(true);
        jFrame.setSize(boardWidth, boardHeight);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);

        PacMan pcGame= new PacMan();
        jFrame.add(pcGame);
        jFrame.pack();
        jFrame.setVisible(true);

    }

}