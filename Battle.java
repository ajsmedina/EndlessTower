

/** 
 * Battle
 * 
 * A battle contains a party and a troop.
 * The battle manages the turn order.
 */

public class Battle{
  /** 
   * The party of player characters participating in the battle.
   */
  Party battleParty;
  
  /** 
   * The troop of enemies participating in the battle.
   */
  Troop battleTroop;
  
  /** 
   * The current turn index of the turnOrder array. This determines which participant can act.
   */
  int turnIndex=0;
  
  /** 
   * An array containing all battle participants. Used to determine whose turn it is.
   */
  BattleMember[] turnOrder;
  
  /** 
   * Constructor for Battle.
   * 
   * @param xparty The player party participating in the battle.
   * @param xtroop The enemy troop participating in the battle.
   */
  public Battle(Party xparty, Troop xtroop){
    battleParty = xparty;
    battleTroop = xtroop;
    createTurnOrder();
  }
  
  
  /** 
   * Get the party participating in the battle.
   * 
   * @return The party in the battle.
   */
  public Party getParty(){
    return battleParty;
  }
  
  /** 
   * Get the enemy troop participating in the battle.
   * 
   * @return The troop in the battle.
   */
  public Troop getTroop(){
    return battleTroop;
  }
  
  /** 
   * Sets the turn order but putting all players and enemies into the array and sorting it by descending
   * DEX stats.
   */
  public void createTurnOrder(){
    turnOrder=new BattleMember[battleParty.getLength()+battleTroop.getLength()];
    
    for(int i=0; i<battleParty.getLength();i++){
      turnOrder[i] = battleParty.getMember(i);
    }
    
    for(int i=0; i<battleTroop.getLength();i++){
      turnOrder[i+battleParty.getLength()] = battleTroop.getMember(i);
    }
    
    sortTurnOrder();
  }
  
  /** 
   * Gets the status information of all player and enemy characters and returns it as a string.
   * 
   * @return String containing status information of all allies and enemies.
   */
  public String getBattleStatus(){
    String output = "";
    for(int i=0; i<battleTroop.getLength();i++){
      output+=battleTroop.getMember(i).getMenuInformation()+"\n";
    }
    
    output+="=============\n";
    for(int i=0; i<battleParty.getLength();i++){
      output+=battleParty.getMember(i).getMenuInformation()+"\n";
    }
    
    return output;
  }
  
  /** 
   * Gets the battle member whose turn it is (player or enemy) and changes the turnIndex to the next character.
   */  
  public BattleMember getCurrentTurn(){
    BattleMember turnMember =  turnOrder[turnIndex];
    
    turnIndex = turnIndex+1==turnOrder.length ? 0 : turnIndex+1;
    
    return turnMember;
  }
  
  /** 
   * Sorts the turn order by descending DEX stats
   */
  public  void sortTurnOrder() {
    int compare;
    
    for (int i = 0; i < turnOrder.length - 1; i++) {
      compare = i;
      
      for (int index = i + 1; index < turnOrder.length; index++) {
        if (turnOrder[index].getStat(Stats.DEX) > turnOrder[compare].getStat(Stats.DEX)) {
          swap(compare, index);
        }
      }
    }
  }
  
  /** 
   * Swaps the order of the battle members in the turn order.
   * 
   * @param first The index of the first member.
   * @param second The index of the second member.
   */
  private  void swap(int first, int second) {
    BattleMember hold = turnOrder[first];
    turnOrder[first] = turnOrder[second];
    turnOrder[second] = hold;
  }
  
  
}