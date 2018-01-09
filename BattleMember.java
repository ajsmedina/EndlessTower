
import java.util.ArrayList;


/** 
 * BattleMember
 * 
 * A particpant in battles.
 */

public abstract class BattleMember {
  /** 
   * The stats of the battle member.
   */
  private int[] stats = new int[8];
  
  /** 
   * The name of the battle member.
   */
  private String name;
  
  /** 
   * The skills the battle member knows.
   */
  private ArrayList<Skill> skills = new ArrayList<Skill>();
  
  /** 
   * True if the battlemember is knocked out.
   */
  private boolean isKO = false;
  
  /** 
   * The multiplier used when the battlemember is guarding.
   */
  private double guardMod = 1;
  
  /** 
   * Multipliers used in damage calculation. A higher atkMod & lower defMod lead to higher damage.
   */
  private double atkMod = 1, defMod = 1;
  
  /** 
   * Constructor for BattleMember
   * 
   * @param xname The name of the battle member.
   */
  public BattleMember(String xname){
    name = xname;
    addSkill(new Skill("Attack", 1, 0, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, "Basic physical attack."));
  }
  
  
  
  /** 
   * Get the name of the battle member.
   * 
   * @return The member's name.
   */
  public String getName(){
    return name;
  }
  
  /** 
   * Get whether the target is KOd or not.
   * 
   * @return True if KOd, False if not
   */
  public boolean getKO(){
    return isKO;
  }
  
  /** 
   * Fully restores the battle member's HP and MP.
   */
  public void fullRestore(){
    changeStat(Stats.HP,getStat(Stats.MHP));
    changeStat(Stats.MP,getStat(Stats.MMP));
  }
  
  /** 
   * Revives the target from KO.
   */
  public void revive(){
    isKO = false;
  }
  
  /** 
   * Sets the name of the battle member.
   * 
   * @param newName The member's name
   */
  public void setName(String newName){
    name = newName;
  }
  
  /** 
   * Get the value of the object's stat.
   * 
   * @param type The type of stat to look at.
   * @return The value of the specified stat
   */
  public int getStat(Stats type){
    return stats[convertStatToInt(type)];
  }
  
  /** 
   * Gets the guard modifier of the battle member.
   * 
   * @return The battle member's guard modifier.
   */
  
  public double getGuard(){
    return guardMod;
  }
  
  /** 
   * Gets the attack modifier of the battle member.
   * 
   * @return The battle member's attack modifier.
   */  
  public double getAtkMod(){
    return atkMod;
  }
  
  /** 
   * Gets the defence modifier of the battle member.
   * 
   * @return The battle member's defence modifier.
   */  
  public double getDefMod(){
    return defMod;
  }
  
  /** 
   * Resets negative attack/defence modifiers.
   */
  public void restore(){
    if(defMod<1){
      defMod = 1;
    } 
    if(atkMod<1){
      atkMod = 1;
    }
  }
  
  /** 
   * Resets positive attack/defence modifiers.
   */
  public void dispel(){
    if(defMod>1){
      defMod = 1;
    } 
    if(atkMod>1){
      atkMod = 1;
    }
  }
  
  /** 
   * Changes the battle member's defence modifier by the indicated amount.
   * Sets the upper limit to 2.0 and the lower limit to 0.5.
   * 
   * @param change The amount to change the defence modifier by. A negative number lowers the modifier.
   */
  public void changeDefMod(double change){
    defMod += change;
    
    if(defMod<0.5){
      defMod = 0.5;
    } else if(defMod>2){
      defMod = 2;
    }
  }
  
  /** 
   * Changes the battle member's attack modifier by the indicated amount.
   * Sets the upper limit to 2.0 and the lower limit to 0.5.
   * 
   * @param change The amount to change the attack modifier by. A negative number lowers the modifier.
   */
  public void changeAtkMod(double change){
    atkMod += change;
    
    if(atkMod<0.5){
      atkMod = 0.5;
    } else if(atkMod>2){
      atkMod = 2;
    }
  }
  
  /** 
   * Sets the guard modifier of the battle member.
   * 
   * @param grd The battle member's guard modifier.
   */
  public void setGuard(double grd){
    guardMod = grd;
  }
  
  /** 
   * Gets all of the battle member's stats
   * 
   * @return The battle member's stats.
   */
  public int[] getAllStats(){
    return stats;
  }
  
  /** 
   * Sets all of the battle member's stats
   * 
   * @param xstats The battle member's stats.
   */
  public void setStats(int[] xstats){
    stats = xstats;
  }
  
  /** 
   * Turns the given Stats type and converts it to the corresponding index of the stats array.
   * 
   * @param type The stat that needs to be converted to an index.
   * @return The index in the stats array of the given stat.
   */
  private int convertStatToInt(Stats type){
    int returnint;
    switch(type){
      case HP:
        returnint = 0;
        break;
      case MHP:
        returnint = 1;
        break;
      case MP:
        returnint =  2;
        break;
      case MMP:
        returnint =  3;
        break;
      case STR:
        returnint =  4;
        break;
      case MAG:
        returnint =  5;
        break;
      case VIT:
        returnint =  6;
        break;
      case DEX:
        returnint =  7;
        break;
      default:
        returnint =  0;
        break;
    }
    
    return returnint;
  }
  
  /** 
   * Adds the given skill to the battle member's skill list.
   * 
   * @param newSkill The skill to add.
   */
  public void addSkill(Skill newSkill){
    skills.add(newSkill);
  }
  
  /** 
   * Gets all of the battle member's skills
   * 
   * @return The battle member's skills
   */
  public ArrayList<Skill> getSkills(){
    return skills;
  }
  
  
  /** 
   * Changes one of the battle member's stats by the given amount.
   * HP and MP are set to not exceed MHP and MMP.
   * HP and MP are set not to be lower than 0, other stats cannot be lower than 1.
   * If HP is less than zero, sets the member to a KO state.
   * 
   * @param type The stat to change.
   * @param change The amount to change the stat.
   */  
  
  public void changeStat(Stats type, int change){
    int statint = convertStatToInt(type);
    
    if(type==Stats.HP && getStat(Stats.HP)+change > getStat(Stats.MHP)){
      change=getStat(Stats.MHP)-getStat(Stats.HP);
    } else if(type==Stats.MP && getStat(Stats.MP)+change > getStat(Stats.MMP)){
      change=getStat(Stats.MMP)-getStat(Stats.MP);
    }
    
    
    
    if(type==Stats.HP){
      stats[statint] = stats[statint]+change;
      if( stats[statint]<=0){
        stats[statint]=0;
        isKO=true;
      } 
    }else if(type==Stats.MP){
      stats[statint] = stats[statint]+change < 0 ? 0 : stats[statint]+change;
    }else{
      stats[statint] = stats[statint]+change < 1 ? 1 : stats[statint]+change;
      
      if(type==Stats.MAG){
        changeStat(Stats.MMP, change* 3);
      } else if(type==Stats.VIT){
        changeStat(Stats.MHP, change* 5);
      }
      
      if(type==Stats.MMP){
        changeStat(Stats.MP, change);
      } else if(type==Stats.MHP){
        changeStat(Stats.HP, change);
      }
    }
  }        
  
  /** 
   * Gets the menu information of the battle member.
   * 
   * @return The String containing the information to display
   */
  public abstract String getMenuInformation();
  
  
}