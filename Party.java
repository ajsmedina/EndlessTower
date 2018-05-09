
import java.util.ArrayList;

/**
 * Party
 * 
 * A party is a team of player characters (PartyMembers).
 */
public class Party extends Team {
  /**
   * The gold currently in the party's possession.
   */
  private int gold=0;
  
  /**
   * The current floor the party is on.
   */
  private int floor=1;
  
  /**
   * An array of all items in the game.
   */
  private Item[] inventory = new Item[20];
  
  /**
   *Constructor for Party.
   *
   *@param xparty The array of party members in the party.
   */
  public Party(PartyMember[] xparty){
    setMembers(xparty);
    initializeItems();
  }
  
  /**
   *Create all items in the party's inventory with zero quantity.
   */
  private void initializeItems(){
    inventory[0] = new Item("Potion",30,TargetType.ONE_ALLY, ItemEffect.HEAL,"Heal 30% HP to one ally.");
    inventory[1] = new Item("Great Potion",60,TargetType.ONE_ALLY, ItemEffect.HEAL,"Heal 60% HP to one ally.");
    inventory[2] = new Item("Super Potion",100,TargetType.ONE_ALLY, ItemEffect.HEAL,"Heal all HP to one ally.");
    inventory[3] = new Item("Large Potion",50,TargetType.ALL_ALLIES, ItemEffect.HEAL,"Heal 50% HP to all allies.");
    inventory[4] = new Item("Ultimate Potion",100,TargetType.ALL_ALLIES, ItemEffect.HEAL,"Heal all HP to all allies.");
    inventory[5] = new Item("Candy",30,TargetType.ONE_ALLY, ItemEffect.MPUP,"Heal 30% MP to one ally.");
    inventory[6] = new Item("Great Candy",60,TargetType.ONE_ALLY, ItemEffect.MPUP,"Heal 60% MP to one ally.");
    inventory[7] = new Item("Super Candy",100,TargetType.ONE_ALLY, ItemEffect.MPUP,"Heal all MP to one ally.");
    inventory[8] = new Item("Large Candy",50,TargetType.ALL_ALLIES, ItemEffect.MPUP,"Heal 50% MP to all allies.");
    inventory[9] = new Item("Ultimate Candy",100,TargetType.ALL_ALLIES, ItemEffect.MPUP,"Heal all MP to all allies.");
    inventory[10] = new Item("Dove Feather",100,TargetType.ONE_ALLY, ItemEffect.FULLHEAL,"Heal all HP and MP to one ally.");
    inventory[11] = new Item("Phoenix Feather",100,TargetType.ALL_ALLIES, ItemEffect.FULLHEAL,"Heal all HP and MP to all allies.");
    inventory[12] = new Item("Ring of Strength",3,TargetType.ONE_ALLY, ItemEffect.STRUP,"Increase one ally's strength by 3.");
    inventory[13] = new Item("Ring of Valor",7,TargetType.ONE_ALLY, ItemEffect.STRUP,"Increase one ally's strength by 7.");
    inventory[14] = new Item("Ring of Magic",3,TargetType.ONE_ALLY, ItemEffect.MAGUP,"Increase one ally's magic by 3.");
    inventory[15] = new Item("Ring of Wisdom",7,TargetType.ONE_ALLY, ItemEffect.MAGUP,"Increase one ally's magic by 7.");
    inventory[16] = new Item("Ring of Vitality",3,TargetType.ONE_ALLY, ItemEffect.VITUP,"Increase one ally's vitality by 3.");
    inventory[17] = new Item("Ring of Fortitude",7,TargetType.ONE_ALLY, ItemEffect.VITUP,"Increase one ally's vitality by 7.");
    inventory[18] = new Item("Ring of Dexterity",3,TargetType.ONE_ALLY, ItemEffect.DEXUP,"Increase one ally's dexterity by 3.");
    inventory[19] = new Item("Ring of Stealth",7,TargetType.ONE_ALLY, ItemEffect.DEXUP,"Increase one ally's dexterity by 7.");
  }
  
  /**
   *Gets the amount of gold the party holds.
   *
   *@return The amount of gold the party holds.
   */
  public int getGold(){
    return gold;
  }
  
  
  /**
   *Changes the gold the party holds. Gold cannot be less than zero.
   *
   *@param change The amount to change the gold.
   */
  public void changeGold(int change){
    gold = gold+change < 0 ? 0 : gold+change;
  }
  
  /**
   * Removes KO from all KOed party members.
   */
  public void reviveDeadMembers(){
    for(int i=0; i<getLength();i++){
      if(getMember(i).getKO()){
        getMember(i).revive();
        getMember(i).changeStat(Stats.HP,1);
      }
    }
  }
  
  /**
   *Fully restores party's HP and MP.
   */
  public void recoverParty(){
    for(int i=0; i<getLength();i++){
      getMember(i).fullRestore();
    }
  }
  
  
  /**
   *Gets the current floor the party is on.
   *
   *@return The floor the party is on.
   */
  public int getFloor(){
    return floor;
  }
  
  /**
   *Sets the current floor the party is on.
   *
   *@return The floor the party is on.
   */ 
  public void setFloor(int newFloor){
    floor = newFloor;
  }
  
  /**
   *Increase the floor number by one.
   */
  public void floorUp(){
    floor++;
  }
  
  /**
   *Adds to the item's quantity.
   *
   *@param itemID The item's index in the inventory.
   *@param quantity The amount of quantity to add.
   */
  public void addItem(int itemID, int quantity){
    inventory[itemID].addQuantity(quantity);
  }
  
  /**
   *Reduce the item's quantity by one.
   *
   *@param itemID The item's index in the inventory.
   */
  public void consumeItem(int itemID){
    inventory[itemID].consumeItem();
  }
  
  /**
   *Gets the quantity of the item in the inventory.
   *
   *@param itemID The item's index in the inventory.
   *@return The item's quantity.
   */  
  public int getQuantity(int itemID){
    return inventory[itemID].getQuantity();
  }
  
  /**
   *Gets the name of the item in the inventory.
   *
   *@param itemID The item's index in the inventory.
   *@return The item's name.
   */  
  public String getItemName(int itemID){
    return inventory[itemID].getName();
  }
  
  /**
   *Gets an arraylist of items that have a quantity of 1 or more.
   *
   *@param return The arraylist of items in the inventory.
   */
  
  public ArrayList<Item> getBagItems(){
    ArrayList<Item> bag = new ArrayList<Item>();
    
    for(int i=0; i < inventory.length; i++){
      if(inventory[i].getQuantity()>0){
        bag.add(inventory[i]);
      }
    }
    return bag;
  }
  
  
  
  
}
