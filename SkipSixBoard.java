import java.util.List;
import java.util.ArrayList;

/**
 * The ElevensBoard class represents the board in a game of Elevens.
 */
public class SkipSixBoard extends Board {
  
  /**
   * The size (number of cards) on the board.
   */
  private static final int BOARD_SIZE = 7;
  
  /**
   * The ranks of the cards for this game to be sent to the deck.
   */
  private static final String[] RANKS =
  {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
  
  /**
   * The suits of the cards for this game to be sent to the deck.
   */
  private static final String[] SUITS =
  {"spades", "hearts", "diamonds", "clubs"};
  
  /**
   * The values of the cards for this game to be sent to the deck.
   */
  private static final int[] POINT_VALUES =
  {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 0, 0};
  
  /**
   * Flag used to control debugging print statements.
   */
  private static final boolean I_AM_DEBUGGING = false;
  
  
  /**
   * Creates a new <code>ElevensBoard</code> instance.
   */
  public SkipSixBoard(int num) {
    super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES, num);
  }
  
  /**
   * Determines if the selected cards form a valid group for removal.
   * In Elevens, the legal groups are (1) a pair of non-face cards
   * whose values add to 11, and (2) a group of three cards consisting of
   * a jack, a queen, and a king in some order.
   * @param selectedCards the list of the indices of the selected cards.
   * @return true if the selected cards form a valid group for removal;
   *         false otherwise.
   */
  @Override
  public boolean isLegal(int selectedCard) {
    /* *** TO BE IMPLEMENTED IN ACTIVITY 9 *** */
    boolean bool = false;
    //Special Cards included here
    if(getCurrentPlayer().cardAt(selectedCard).matches(getDiscardPileTopCard()) || 
       getCurrentPlayer().cardAt(selectedCard).rank().equals("ace") || 
       getCurrentPlayer().cardAt(selectedCard).pointValue() == 6 || 
       (getCurrentPlayer().cardAt(selectedCard).rank().equals("king") 
          && getCurrentPlayer().cardAt(selectedCard).suit().equals("hearts"))){
         bool = true;
    }
       
    return bool;
  } 
  
  
  /**
   * Determine if there are any legal plays left on the board.
   * In Elevens, there is a legal play if the board contains
   * (1) a pair of non-face cards whose values add to 11, or (2) a group
   * of three cards consisting of a jack, a queen, and a king in some order.
   * @return true if there is a legal play left on the board;
   *         false otherwise.
   */
  @Override
  public boolean anotherPlayIsPossible() {
    for (int i = 0; i < getCurrentPlayer().size(); i++) {
      if (isLegal(i)){
        return true;
      }
    }
    return false;
  }
  
  /**
   * Check for an 11-pair in the selected cards.
   * @param selectedCards selects a subset of this board.  It is list
   *                      of indexes into this board that are searched
   *                      to find an 11-pair.
   * @return true if the board entries in selectedCards
   *              contain an 11-pair; false otherwise.
   */
  private boolean containsPairSum11(List<Integer> selectedCards) {
    /* *** TO BE IMPLEMENTED IN ACTIVITY 9 *** */
    if(selectedCards.size() >= 2) {
      for(int i = 0; i < selectedCards.size(); i++) {
        for(int j = i; j < selectedCards.size(); j++){
          if(getCurrentPlayer().cardAt(selectedCards.get(i)).pointValue() + getCurrentPlayer().cardAt(selectedCards.get(j)).pointValue() == 11) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Check for a JQK in the selected cards.
   * @param selectedCards selects a subset of this board.  It is list
   *                      of indexes into this board that are searched
   *                      to find a JQK group.
   * @return true if the board entries in selectedCards
   *              include a jack, a queen, and a king; false otherwise.
   */
  private boolean containsJQK(List<Integer> selectedCards) {
    /* *** TO BE IMPLEMENTED IN ACTIVITY 9 *** */
    boolean containsJ = false;
    boolean containsQ = false;
    boolean containsK = false;
    if(selectedCards.size() >= 3) {
      for(int i = 0; i < selectedCards.size(); i++) {
        if(!containsJ && getCurrentPlayer().cardAt(selectedCards.get(i)).rank().equals("jack")) {
          containsJ = true;
        }
        else if(!containsQ && getCurrentPlayer().cardAt(selectedCards.get(i)).rank().equals("queen")) {
          containsQ = true;
        }
        else if(!containsK && getCurrentPlayer().cardAt(selectedCards.get(i)).rank().equals("king")) {
          containsK = true;
        }
      }
    }
    return (containsJ && containsQ && containsK);
  }
}

