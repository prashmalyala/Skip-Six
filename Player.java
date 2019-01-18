import java.util.ArrayList;

public class Player {
  
    private ArrayList <Card> cards;
    private Deck deck;
    private int size;
  
    public Player (Deck myDeck, int s) {
      size = s;
      cards = new ArrayList <Card>(s);
      deck=myDeck;
      dealMyCards();
    }
  
  /**
   * Deal cards to this board to start the game.
   */
  public void dealMyCards() {
    for (int k = 0; k < this.size; k++) {
      cards.add(deck.deal());
    }
  }
  
   /**
   * Accesses the size of the board.
   * Note that this is not the number of cards it contains,
   * which will be smaller near the end of a winning game.
   * @return the size of the board
   */
  public int size() {
    return size;
  }
  
  //increases size of cards by 1
  public void resize () {
    size++;
  }
  
   /**
   * Determines if the board is empty (has no cards).
   * @return true if this board is empty; false otherwise.
   */
  public boolean isEmpty() {
    for (int k = 0; k < size; k++) {
      if (cards.get(k) != null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Accesses a card on the board.
   * @return the card at position k on the board.
   * @param k is the board position of the card to return.
   */
  public Card cardAt(int k) {
    return cards.get(k);
  }
  

  public void drawOneCard() {
    cards.add(deck.deal());
    size++;
    for (int k = 0; k < size; k++) {
      System.out.println(cards.get(k).toString());
    }
  }
  
  public int findCard (Card theCard) {
    for(int i=0; i<size; i++) {
      if (cards.get(i)==theCard){
        return i;
      }
    }
    return -1;
  }
  
  public void removeCard (Card toBeRemoved) {
    int index = findCard(toBeRemoved);
    if (index ==-1)
      return;
    else {
      cards.remove(index);
      size--;
    }
  }
  
}