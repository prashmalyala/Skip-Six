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


/**
 * This class provides a GUI for solitaire games related to Elevens.
 */
public class CardGameGUI extends JFrame implements ActionListener {
  
  /** Height of the game frame. */
  private static final int DEFAULT_HEIGHT = 520; /* MODIFIED */
  /** Width of the game frame. */
  private static final int DEFAULT_WIDTH = 800;
  /** Width of a card. */
  private static final int CARD_WIDTH = 73;
  /** Height of a card. */
  private static final int CARD_HEIGHT = 97;
  /** Row (y coord) of the upper left corner of the first card. */
  private static final int LAYOUT_TOP = 30;
  /** Column (x coord) of the upper left corner of the first card. */
  private static final int LAYOUT_LEFT = 30;
  /** Distance between the upper left x coords of
    *  two horizonally adjacent cards. */
  private static final int LAYOUT_WIDTH_INC = 100;
  /** Distance between the upper left y coords of
    *  two vertically adjacent cards. */
  private static final int LAYOUT_HEIGHT_INC = 125;
  /** y coord of the "Replace" button. */
  private static final int BUTTON_TOP = 30;
  /** x coord of the "Replace" button. */
  private static final int BUTTON_LEFT = 570;
  /** Distance between the tops of the "Replace" and "Restart" buttons. */
  private static final int BUTTON_HEIGHT_INC = 50;
  /** y coord of the "n undealt cards remain" label. */
  private static final int LABEL_TOP = 160;
  /** x coord of the "n undealt cards remain" label. */
  private static final int LABEL_LEFT = 540;
  /** Distance between the tops of the "n undealt cards" and
    *  the "You lose/win" labels. */
  private static final int LABEL_HEIGHT_INC = 35;
  
  /** The board (Board subclass). */
  private Board board;
  
  /** The main panel containing the game components. */
  private JPanel panel;
  /** The Draw button. */
  private JButton drawButton;
  /** The Restart button. */
  private JButton playButton;
  //instructions for the game
  private JButton helpButton;
  /** The "number of undealt cards remain" message. */
  private JLabel statusMsg;
  /** The "you've won n out of m games" message. */
  private JLabel totalsMsg;
  /** The card displays. */
  private JLabel[] displayCards;
  private JLabel topCard; /* MODIFIED */
  /** The win message. */
  private JLabel winMsg;
  /** The loss message. */
  private JLabel lossMsg;
  /** The coordinates of the card displays. */
  private Point[] cardCoords;
  
  /** kth element is true iff the user has selected card #k. */
  private boolean[] selections;
  /** The number of games won. */
  private int totalWins;
  /** The number of games played. */
  private int totalGames;
  //to start a new game
  private boolean startNewGame;
  
  
  /**
   * Initialize the GUI.
   * @param gameBoard is a <code>Board</code> subclass.
   */
  public CardGameGUI(Board gameBoard) {
    board = gameBoard;
    totalWins = 0;
    totalGames = 0;
    startNewGame = false;
    
    // Initialize cardCoords using 5 cards per row
    //cardCoords = new Point[board.size()];
    cardCoords = new Point[52]; /* MODIFIED */
    int x = LAYOUT_LEFT;
    int y = LAYOUT_TOP + LAYOUT_HEIGHT_INC + 60; /* MODIFIED */
    for (int i = 0; i < cardCoords.length; i++) {
      cardCoords[i] = new Point(x, y);
      if (i % 5 == 4) {
        x = LAYOUT_LEFT;
        y += LAYOUT_HEIGHT_INC;
      } else {
        x += LAYOUT_WIDTH_INC;
      }
    }
    
    //selections = new boolean[board.size()];
    selections = new boolean[52]; /* MODIFIED */
    initDisplay();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    repaint();
  }
  
  /**
   * Run the game.
   */
  public void displayGame() {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        setVisible(true);
      }
    });
  }
  
  /**
   * Draw the display (cards and messages).
   */
  public void repaint() {
    /* MODIFIED: New for Discard Pile top card */
    String cardImageFileName =
      imageFileName(board.getDiscardPileTopCard(), false);
    URL imageURL = getClass().getResource(cardImageFileName);
    if (imageURL != null) {
      ImageIcon icon = new ImageIcon(imageURL);
      topCard.setIcon(icon);
      topCard.setVisible(true);
    } else {
      throw new RuntimeException(
                                 "Card image not found: \"" + cardImageFileName + "\"");
    }
    
    for (int k = 0; k < board.getCurrentPlayer().size(); k++) {
      cardImageFileName =
        imageFileName(board.getCurrentPlayer().cardAt(k), selections[k]);
      imageURL = getClass().getResource(cardImageFileName);
      if (imageURL != null) {
        ImageIcon icon = new ImageIcon(imageURL);
        displayCards[k].setIcon(icon);
        displayCards[k].setVisible(true);
      } else {
        throw new RuntimeException(
                                   "Card image not found: \"" + cardImageFileName + "\"");
      }
    }
    
    // Show the Draw button only if the deck is not empty
    if ( board.deckSize() == 0 ) {
      panel.remove( drawButton );
    }
    
    statusMsg.setText(board.deckSize() + " undealt cards remain.");
    statusMsg.setVisible(true);
    /* totalsMsg.setText("It is Player " + (board.getPlayerTurn() + 1)
                        + "'s turn."); */
    //totalsMsg.setVisible(true);
    pack();
    panel.repaint();
  }
  
  /**
   * MODIFIED: Update the display
   */
  private void updateDisplay() {
    // Calculate number of rows of cards (5 cards per row)
    // and adjust JFrame height if necessary
    int numCardRows = (board.getCurrentPlayer().size() + 4) / 5;
    int height = DEFAULT_HEIGHT;
    if (numCardRows > 2) {
      height += (numCardRows - 2) * LAYOUT_HEIGHT_INC;
    }
    
    this.setSize(new Dimension(DEFAULT_WIDTH, height));
    panel.setLayout(null);
    panel.setPreferredSize(
                           new Dimension(DEFAULT_WIDTH - 20, height - 20));
    
    winMsg = new JLabel();
    winMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
    winMsg.setFont(new Font("SansSerif", Font.BOLD, 25));
    winMsg.setForeground(Color.GREEN);
    winMsg.setText("Player " + (board.getPlayerTurn() + 1) + " wins!");
    panel.add(winMsg);
    winMsg.setVisible(false);
    
    lossMsg = new JLabel();
    lossMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
    lossMsg.setFont(new Font("SanSerif", Font.BOLD, 25));
    lossMsg.setForeground(Color.RED);
    lossMsg.setText("Sorry, you lose.");
    panel.add(lossMsg);
    lossMsg.setVisible(false);
    
    totalsMsg.setVisible(false);
    totalsMsg = new JLabel("It is Player " + (board.getPlayerTurn() + 1)
                        + "'s turn.");
    totalsMsg.setBounds(LABEL_LEFT, LABEL_TOP + 2 * LABEL_HEIGHT_INC,
                        250, 30);
    panel.add(totalsMsg);
    /*
     if (!board.anotherPlayIsPossible()) {
     signalLoss();
     }
     */
    pack();
    getContentPane().add(panel);
    getRootPane().setDefaultButton(drawButton);
    panel.setVisible(true);
    
  }
  
  /**
   * Initialize the display.
   */
  private void initDisplay() {
    panel = new JPanel() {
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
      }
    };
    
    // If board object's class name follows the standard format
    // of ...Board or ...board, use the prefix for the JFrame title
    String className = board.getClass().getSimpleName();
    int classNameLen = className.length();
    int boardLen = "Board".length();
    String boardStr = className.substring(classNameLen - boardLen);
    if (boardStr.equals("Board") || boardStr.equals("board")) {
      int titleLength = classNameLen - boardLen;
      setTitle(className.substring(0, titleLength));
    }
    
    // Calculate number of rows of cards (5 cards per row)
    // and adjust JFrame height if necessary
    int numCardRows = (board.getCurrentPlayer().size() + 4) / 5;
    int height = DEFAULT_HEIGHT;
    if (numCardRows > 2) {
      height += (numCardRows - 2) * LAYOUT_HEIGHT_INC;
    }
    
    this.setSize(new Dimension(DEFAULT_WIDTH, height));
    panel.setLayout(null);
    panel.setPreferredSize(
                           new Dimension(DEFAULT_WIDTH - 20, height - 20));
    
    /* MODIFIED */
    topCard = new JLabel();
    panel.add(topCard);
    topCard.setBounds(LAYOUT_LEFT, LAYOUT_TOP, CARD_WIDTH, CARD_HEIGHT);
    
    /* MODIFIED */
    displayCards = new JLabel[52];
    for (int k = 0; k < 52; k++) {
      displayCards[k] = new JLabel();
      panel.add(displayCards[k]);
      displayCards[k].setBounds(cardCoords[k].x, cardCoords[k].y,
                                CARD_WIDTH, CARD_HEIGHT);
      displayCards[k].addMouseListener(new MyMouseListener());
      selections[k] = false;
    }
    drawButton = new JButton();
    drawButton.setText("Draw");
    panel.add(drawButton);
    drawButton.setBounds(BUTTON_LEFT, BUTTON_TOP, 100, 30);
    drawButton.addActionListener(this);
    
    helpButton = new JButton ();
    helpButton.setText("Help");
    panel.add(helpButton);
    helpButton.setBounds(BUTTON_LEFT, 350, 100, 30);
    helpButton.addActionListener(this);
    
    playButton = new JButton();
    playButton.setText("Play");
    panel.add(playButton);
    playButton.setBounds(BUTTON_LEFT, BUTTON_TOP + BUTTON_HEIGHT_INC,
                         100, 30);
    playButton.addActionListener(this);
    
    statusMsg = new JLabel(board.deckSize() + " undealt cards remain.");
    panel.add(statusMsg);
    statusMsg.setBounds(LABEL_LEFT, LABEL_TOP, 250, 30);
    
    winMsg = new JLabel();
    winMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
    winMsg.setFont(new Font("SansSerif", Font.BOLD, 25));
    winMsg.setForeground(Color.GREEN);
    winMsg.setText("Player " + (board.getPlayerTurn() + 1) + " wins!");
    panel.add(winMsg);
    winMsg.setVisible(false);
    
    lossMsg = new JLabel();
    lossMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
    lossMsg.setFont(new Font("SanSerif", Font.BOLD, 25));
    lossMsg.setForeground(Color.RED);
    lossMsg.setText("Sorry, you lose.");
    panel.add(lossMsg);
    lossMsg.setVisible(false);
    
    totalsMsg = new JLabel("It is Player " + (board.getPlayerTurn() + 1)
                        + "'s turn.");
    totalsMsg.setBounds(LABEL_LEFT, LABEL_TOP + 2 * LABEL_HEIGHT_INC,
                        250, 30);
    panel.add(totalsMsg);
    pack();
    getContentPane().add(panel);
    getRootPane().setDefaultButton(drawButton);
    panel.setVisible(true);
  }
  
  /**
   * Deal with the user clicking on something other than a button or a card.
   */
  private void signalError() {
    Toolkit t = panel.getToolkit();
    t.beep();
  }
  
  /**
   * Returns the image that corresponds to the input card.
   * Image names have the format "[Rank][Suit].GIF" or "[Rank][Suit]S.GIF",
   * for example "aceclubs.GIF" or "8heartsS.GIF". The "S" indicates that
   * the card is selected.
   *
   * @param c Card to get the image for
   * @param isSelected flag that indicates if the card is selected
   * @return String representation of the image
   */
  private String imageFileName(Card c, boolean isSelected) {
    String str = "cards/";
    if (c == null) {
      return "cards/back1.GIF";
    }
    str += c.rank() + c.suit();
    if (isSelected) {
      str += "S";
    }
    str += ".GIF";
    return str;
  }
  
  /**
   * Respond to a button click (on either the "Draw" button
   * or the "Restart" button).
   * @param e the button click action event
   */
  public void actionPerformed(ActionEvent e) {
    String Instructions = "SKIP SIX INSTRUCTIONS:\n" + 
      "-This game is played with a standard deck of 52 cards.\n" + 
      "-There can be 2 to 4 players.\n" + 
      "-The objective for a player is to get rid of all their cards first.\n"+ 
      "-You can play a card if it has the same suite, same number, or is a unique card.\n" +
      "-TO PLAY A CARD: select the card and click \"play\".\n" +
      "-IF YOU CANNOT PLAY A CARD: click draw; it is now the next player's turn.\n" + 
      "-Even if you can play a turn, you can choose to draw a single additional card anyway\n" +
      "You can play the following unique cards AT ALL TIMES:\n" +
      "-Ace of any suite: lets you play one additional card that same turn.\n" +
      "-Six of any suite: lets you skip the turn of the following player.\n" +
      "-King of Hearts: lets you search through the deck of undrawn cards and swap one of the cards with the King of Hearts.\n";
    String HowToPlay = "";
    
    if (e.getSource().equals(drawButton)) { /* MODIFIED */
      if ( startNewGame == true ) {
        // Bring the Draw and Play buttons back
        drawButton.setText( "Draw" );
        panel.add( playButton );
        winMsg.setVisible(false);
        lossMsg.setVisible(false);
        
        // Start a new game
        board.newGame();
        startNewGame = false;
      }else{
        board.getCurrentPlayer().drawOneCard();
        for (int i = 0; i < selections.length; i++) {
          selections[i] = false;
        }
        board.nextPlayerTurn();
        updateDisplay();
        repaint();
        JOptionPane.showMessageDialog(null, "It's now Player " + (board.getPlayerTurn()+1) + "'s turn!", "Notice!", JOptionPane.INFORMATION_MESSAGE);
      }
    } else if (e.getSource().equals(playButton)) {
      int count = 0;
      for (int i=0; i<selections.length; i++) {
        if(selections[i] == true){
          break;
        }
        count++;
      }
      //NEW TO REMOVE SELECTIONS
      for(int i=0; i < selections.length; i++){
        selections[i] = false;
      }
      //check 
      if (board.isLegal(count)){
        if((board.getCurrentPlayer().cardAt(count).rank().equals("ace"))){
          board.setDiscardPileTopCard(board.getCurrentPlayer().cardAt(count));
          board.getCurrentPlayer().removeCard(board.getCurrentPlayer().cardAt(count));
          displayCards[board.getCurrentPlayer().size()].setVisible(false);
          
          if (board.getCurrentPlayer().isEmpty()) {
            signalWin();
          } else if (!board.anotherPlayIsPossible() && board.deckSize()==0) {
            signalLoss();
          }
          
          updateDisplay();
          repaint();
          JOptionPane.showMessageDialog(null, "It's now Player " + (board.getPlayerTurn()+1) + "'s turn!", "Notice!", JOptionPane.INFORMATION_MESSAGE);
          return; //LETS CURRENT PLAYER PLAY AGAIN BY NEVER CALLING NEXT TURN
        }
        else if(board.getCurrentPlayer().cardAt(count).pointValue() == 6){
          board.setDiscardPileTopCard(board.getCurrentPlayer().cardAt(count));
          board.getCurrentPlayer().removeCard(board.getCurrentPlayer().cardAt(count));
          displayCards[board.getCurrentPlayer().size()].setVisible(false);
          
          if (board.getCurrentPlayer().isEmpty()) {
            signalWin();
          } else if (!board.anotherPlayIsPossible() && board.deckSize()==0) {
            signalLoss();
          }
          
          //CALLING TWICE SKIPS THE NEXT PLAYER'S TURN
          board.nextPlayerTurn();
          board.nextPlayerTurn();
          updateDisplay();
          repaint();
          JOptionPane.showMessageDialog(null, "It's now Player " + (board.getPlayerTurn()+1) + "'s turn!", "Notice!", JOptionPane.INFORMATION_MESSAGE);
        }
        else if((board.getCurrentPlayer().cardAt(count).rank().equals("king") 
                   && board.getCurrentPlayer().cardAt(count).suit().equals("hearts"))){
          
          board.setDiscardPileTopCard(board.getCurrentPlayer().cardAt(count));
          board.getCurrentPlayer().removeCard(board.getCurrentPlayer().cardAt(count));
          displayCards[board.getCurrentPlayer().size()].setVisible(false);
          //SHOULD SWAP WITH THE KING OF HEARTS NEED TO REVISE ALL OF THE ABOVE
          
          if (board.getCurrentPlayer().isEmpty()) {
            signalWin();
          } else if (!board.anotherPlayIsPossible() && board.deckSize()==0) {
            signalLoss();
          }
          
          board.nextPlayerTurn();
          updateDisplay();
          repaint();
          JOptionPane.showMessageDialog(null, "It's now Player " + (board.getPlayerTurn()+1) + "'s turn!", "Notice!", JOptionPane.INFORMATION_MESSAGE);
          
        }
        else if(board.getCurrentPlayer().cardAt(count).matches(board.getDiscardPileTopCard())){
          board.setDiscardPileTopCard(board.getCurrentPlayer().cardAt(count));
          board.getCurrentPlayer().removeCard(board.getCurrentPlayer().cardAt(count));
          displayCards[board.getCurrentPlayer().size()].setVisible(false);
          
          if (board.getCurrentPlayer().isEmpty()) {
            signalWin();
          } else if (!board.anotherPlayIsPossible() && board.deckSize()==0) {
            signalLoss();
          }
          
          board.nextPlayerTurn();
          updateDisplay();
          repaint();
          JOptionPane.showMessageDialog(null, "It's now Player " + (board.getPlayerTurn()+1) + "'s turn!", "Notice!", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
          //do nothing
        }
        
        /*
         * board.nextPlayerTurn();
         updateDisplay();
         repaint();
         */
        
      }
      else{
        JOptionPane.showMessageDialog(null, "Sorry, that move is illegal!", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else if (e.getSource().equals(helpButton)){
      JOptionPane.showMessageDialog(null, Instructions, "How to Play: " 
                                      + HowToPlay, JOptionPane.INFORMATION_MESSAGE);
    } else {
      signalError();
      return;
    }
    
  }
  
  /**
   * Display a win.
   */
  private void signalWin() {
    getRootPane().setDefaultButton(playButton);
    winMsg.setVisible(true);
    totalWins++;
    totalGames++;
  }
  
  /**
   * Display a loss.
   */
  private void signalLoss() {
    getRootPane().setDefaultButton(playButton);
    lossMsg.setVisible(true);
    totalGames++;
  }
  
  /**
   * Receives and handles mouse clicks.  Other mouse events are ignored.
   */
  private class MyMouseListener implements MouseListener {
    
    /**
     * You can now only select one card at a time. Cards are unselected
     * after chosing another card.
     * @param e the mouse event.
     */
    public void mouseClicked(MouseEvent e) {
      for(int j = 0; j < board.getCurrentPlayer().size(); j++) {
        selections[j] = false;
      }
      for (int k = 0; k < board.getCurrentPlayer().size(); k++) {
        if (e.getSource().equals(displayCards[k])
              && board.getCurrentPlayer().cardAt(k) != null) {
          selections[k] = true;
          repaint();
          return;
        }
      }
      signalError();
    }
    
    /**
     * Ignore a mouse exited event.
     * @param e the mouse event.
     */
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Ignore a mouse released event.
     * @param e the mouse event.
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * Ignore a mouse entered event.
     * @param e the mouse event.
     */
    public void mouseEntered(MouseEvent e) {
    }
    
    /**
     * Ignore a mouse pressed event.
     * @param e the mouse event.
     */
    public void mousePressed(MouseEvent e) {
    }
  }
}