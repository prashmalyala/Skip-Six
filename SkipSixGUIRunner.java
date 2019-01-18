/**
 * This is a class that plays the GUI version of the Elevens game.
 * See accompanying documents for a description of how Elevens is played.
 */
public class SkipSixGUIRunner {

 /**
  * Plays the GUI version of Elevens.
  * @param args is not used.
  */
  public static void main(String[] args) {
    OpeningScreen bob = new OpeningScreen();
    int s = bob.getPlayer();
  Board board = new SkipSixBoard(s); //insert a number in the constructor so we receive a player size
  CardGameGUI gui = new CardGameGUI(board);
  gui.displayGame();
 }
}
