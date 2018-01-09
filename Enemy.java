
import java.util.ArrayList;

/** 
 * Enemy
 * 
 * A battle member that belongs in a troop.
 * Fights the player's party.
 */

public class Enemy extends BattleMember {
  /** 
   * The gold held by the enemy. Rewarded to the player if they win the battle.
   */
  private int gold;
  
  /** 
   * The enemy's affinity to fire, ice, and elec attakcs. 
   * A higher value leads to higher damage dealt by those attacks.
   */
  private double[] elementalAffinity = new double[]{1,1,1};
  
  
  /** 
   * Constructor for Enemy
   * 
   * @param xname The name of the enemy.
   * @param xstats The enemy's stats.
   * @param xgold The gold the enemy is holding.
   */
  public Enemy(String xname, int[] xstats, int xgold){
    super(xname);
    setStats(xstats);
    gold = xgold;
  }
  
  /** 
   * Gets the amount of gold the enemy holds.
   * 
   * @return The amount of gold the enemy holds.
   */
  public int getGold(){
    return gold;
  }
  
  /** 
   * Changes the gold the enemy holds.
   * 
   * @param change The amount to change the gold.
   */
  public void changeGold(int change){
    gold+=change;
  }
  
  
  /** 
   * Changes the enemy's elemental affinity.
   * Affinity cannot exceed 3 (take triple damage) and cannot be less than -1 (drain damage).
   * Affinity only exists for Fire, Ice, and Elec elements.
   * 
   * @param elem The element to change the affinity of.
   * @param change The amount to change the affinity
   */
  public void changeAffinity(Elements elem, double change){
    int elemint = convertElemToInt(elem);
    
    if(elemint!=-1){
      elementalAffinity[elemint] += change;
      if (elementalAffinity[elemint] < -1){
        elementalAffinity[elemint] = -1;
      }
      else if (elementalAffinity[elemint] >3){
        elementalAffinity[elemint] = 3;
      }
    }
  }
  
  /** 
   * Gets the index in the elementalAffinity array corresponding to the given element.
   * Returns -1 if it is an element with no affinity.
   * 
   * @param elem The element to change the affinity of.
   * @return The index of the element.
   */
  private int convertElemToInt(Elements elem){
    if(elem == Elements.FIRE){
      return 0;
    } else if(elem == Elements.ICE){
      return 1;
    } else if(elem == Elements.ELEC){
      return 2;
    } else {
      return -1;
    }
  }
  
  
  /** 
   * Gets the elemental affinity to the element.
   * Returns 1 if the element is not affected by affinity (not fire, ice, or elec)
   * 
   * @param elem The element to get the affinity of
   * @return The affinity to the element.
   */
  public double getElementalAffinity(Elements elem){
    int elemint = convertElemToInt(elem);
    if(elemint==-1){
      return 1;
    } else{
      return elementalAffinity[elemint];
    }
  }
  
  /** 
   * Adds a trait before the enemy's name.
   * 
   * @param newTitle The trait to add before the name.
   */
  public void addName(String newTitle){
    setName(newTitle+" "+getName());
  }  
  
  /** 
   * Checks if the enemy already possesses a trait.
   * 
   * @param title The trait to check.
   * @return True if they do possess the trait, false otherwise
   */
  
  public boolean checkTrait(String title){
    return getName().indexOf(title+" ")==-1 ? true : false;
  }
  
  
  /** 
   * Gets the menu information of the enemy.
   * 
   * @return The String containing the information to display
   */
  public String getMenuInformation(){
    int hpPercent = (int)((((double)(getStat(Stats.HP)))/(getStat(Stats.MHP)))* 100);
    
    if(hpPercent==0 && getStat(Stats.HP)>0){
      hpPercent = 1;
    }
    
    return String.format("%-15s",getName())+" HP: "+hpPercent+"%";
  }
}