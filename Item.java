
/** 
 * Item
 * 
 * An item is a type of action that can only be used outside of battle.
 * Items have a quantity, so they are limited.
 */
public class Item extends Action{
  /** 
   * The item's effect.
   */
  private ItemEffect effect;
  
  /** 
   * The amount of the item currently in the user's inventory.
   */
  private int quantity=0;
  
  
  /** 
   * Constructor for Item
   * 
   * @param xname The name of the item.
   * @param xpower The power of the item.
   * @param xtarget The target of the item.
   * @param xeffect The effect of the item.
   * @param xdescription A description of what the item does.
   */
  public Item(String xname, double xpower, TargetType xtarget, ItemEffect xeffect, String xdescription){
    super(xname, xpower, xtarget, xdescription);
    effect = xeffect;
  }
  
  /** 
   * Gets the effect of the item.
   * 
   * @return The item's effect
   */ 
  public ItemEffect getEffect(){
    return effect;
  }
  
  /** 
   * Gets the quantity of the item in the inventory.
   * 
   * @return The quantity of the item.
   */
  public int getQuantity(){
    return quantity;
  }
  
  /** 
   * Lowers the quantity of the item by 1.
   * Quantity cannot be less than zero.
   */
  public void consumeItem(){
    quantity = quantity-1 < 0 ? 0 : quantity-1;
    
  }
  
  /** 
   * Adds to the quantity of the item.
   * 
   * @param amount The amount to add to the quantity.
   */
  public void addQuantity(int amount){
    quantity+=amount;
  }
  
}