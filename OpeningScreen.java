import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

//NEW CLASS

public class OpeningScreen {
  
  private int numPlyr;
  
  public OpeningScreen(){
    //JFrame frame = new JFrame("Skip Six");
    //frame.pack();
    //frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
    //frame.setVisible(true);
    Object[] options = {"4",
                    "3",
                    "2"};
    int n = JOptionPane.showOptionDialog(new JFrame("Skip Six"),
    "How many players? Note: if you close this window, the game will assume 2 players by default.",
    "Skip Six",
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.QUESTION_MESSAGE,
    null,
    options,
    options[2]);
    //frame.setVisible(true);
    if(n == JOptionPane.YES_OPTION){
      numPlyr = 4;
      //make board w 4 players
    }
    else if(n == JOptionPane.NO_OPTION){
      numPlyr = 3;
    }
    else if(n == JOptionPane.CANCEL_OPTION){
      numPlyr = 2;
    }
    else{
      numPlyr = 2;
    }
  }
  
  public int getPlayer(){
    return numPlyr;
  }
}