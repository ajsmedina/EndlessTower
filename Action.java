

/** 
 *Action
 *
 *An Action is an action that a battle member can use to deal damage or heal.
 */

public abstract class Action{
  /** 
   *The effectiveness of the action. Determines damage or healing potency.
   */
  private double power;
  
  /** 
   *The name of the action.
   */
  private String name;
  
  /** 
   *A description of what the action does.
   */
  private String description;
  
  /** 
   *The type of target the action affects.
   */
  private TargetType target;
  
  /** 
   * Constructor for Action.
   * 
   * @param xname The name of the action.
   * @param xpower The power of the action
   * @param xtarget The target of the action.
   * @param xdescription A description of what the action does.
   */
  public Action(String xname, double xpower, TargetType xtarget, String xdescription){
    name = xname;
    power = xpower;
    target = xtarget;
    description = xdescription;
  }
  
  /** 
   * Get the description of the action.
   * 
   * @return The description of the action.
   */
  public String getDescription(){
    return description;
  }
  
  /** 
   * Get the power of the action.
   * 
   * @return The power of the action.
   */
  public double getPower(){
    return power;
  }
  
  /** 
   * Get the name of the action.
   * 
   * @return The name of the action.
   */
  
  public String getName(){
    return name;
  }
  
  
  /** 
   * Get the target type of the action.
   * 
   * @return The target type of the action.
   */
  public TargetType getTarget(){
    return target;
  }
}