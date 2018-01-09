

/** 
 * Skill
 * 
 * A skill is an action that can be used by players and enemies to fight in battle.
 */

public class Skill extends Action {
  /** 
   * The cost of the skill. If the skill uses HP, this is the percent of max HP that it costs.
   */
  private int cost;
  
  /** 
   * True if the skill consumes MP, false if it consumes HP.
   */
  private boolean useMP;
  
  /** 
   * The secondary effect of the skill.
   */
  private SkillEffect effect;
  
  /** 
   * The element of the skill.
   */
  private Elements elem;
  
  
  /** 
   * Constructor for Skill
   * 
   * @param xname The name of the skill.
   * @param xpower The power of the skill.
   * @param xcost The cost of the skill.
   * @param xuseMP True if the skill costs MP.
   * @param xtarget The target of the skill.
   * @param xelem The element of the skill.
   * @param xeffect The effect of the skill.
   * @param xdescription A description of what the skill does.
   */
  public Skill(String xname, double xpower, int xcost, boolean xuseMP, TargetType xtarget, Elements xelem, SkillEffect xeffect, String xdescription){
    super(xname, xpower, xtarget, xdescription);
    useMP = xuseMP;
    cost = xcost;
    elem = xelem;
    effect = xeffect;
  }
  
  /** 
   * Gets the cost of the skill.
   * If the skill costs HP, calculates the cost based on the user's maxHP.
   * 
   * @param user The battle member using the skill.
   * @return The cost of the skill.
   */
  public int getCost(BattleMember user){
    if(useMP){
      return cost;
    } else {
      return (int)Math.floor((user.getStat(Stats.MHP)/100.0)* cost);
    }
    
  }
  
  /** 
   * Gets the he raw cost of the skill. 
   * If the skill costs HP, returns the percentage of HP it will cost.
   * 
   * @return The cost of the skill.
   */
  public int getRawCost(){
    return cost;
  }
  
  /** 
   * Gets the element of the skill.
   * 
   * @return The skill's element.
   */
  public Elements getElement(){
    return elem;
  }
  
  
  /** 
   * Gets the effect of the skill.
   * 
   * @return The skill's effect.
   */
  public SkillEffect getEffect(){
    return effect;
  }
  
  
  /** 
   * Checks whether or not the skill costs MP to use.
   * 
   * @return If the skill costs MP, return True. If the skill costs HP, return false.
   */
  public boolean skillUsesMP(){
    return useMP;
  }
  
  
  /** 
   * Gets the cost of the skill along with the appropriate cost type (HP or MP).
   * 
   * @param user The battle member using the skill.
   * @return The cost of the skill with the cost type.
   */
  public String getCostString(BattleMember user){
    if(useMP){
      return getCost(user)+" MP";
    } else{
      return getCost(user)+" HP";
    }
  }
  
}