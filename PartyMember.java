
import java.util.ArrayList;

/** 
 * PartyMember
 * 
 * A player character that belongs in the player's party.
 */
public class PartyMember extends BattleMember {
  /** 
   * The party member's job. Their job determines their skills, subclasses, and stats.
   */
  private Jobs job;
  
  /** 
   * The levels of the party member's three subclasses.
   */
  private int[] subclassLevels = new int[]{1,1,1};
  
  /** 
   * The overall level of the party member. Increases by 1 each level up.
   */
  private int level=1;
  
  /** 
   * Constructor for party member.
   * 
   * @param xname The name of the party member.
   * @param xjob The job of the party member.
   */
  public PartyMember(String xname, Jobs xjob){
    super(xname);
    job = xjob;
    initializeJobStats();
    initializeClassSkills();
  }
  
  /** 
   * Sets the party member's stats based on their job.
   */
  private void initializeJobStats(){
    int[] newStats;
    if(job == Jobs.FIGHTER){
      newStats = new int[]{35, 35, 15, 15, 10, 5, 7, 7};
      
    } else if(job == Jobs.MAGE){
      newStats = new int[]{35, 35, 30, 30, 7, 10, 7, 5};
      
    } else if(job == Jobs.CLERIC){
      newStats = new int[]{50, 50, 21, 21, 5, 7, 10, 7};
    } else{
      newStats = new int[]{25, 25, 21, 21, 7, 7, 5, 10};
    }
    setStats(newStats);
  }  
  
  /** 
   * Gets the levels of the party member's subclasses.
   * 
   * @return Array containing the subclass levels.
   */
  public int[] getSubclassLevels(){
    return subclassLevels;
  }
  
  /** 
   * Get's the party member's job.
   * 
   * @return The job of the party member.
   */
  public Jobs getJob(){
    return job;
  }
  
  /** 
   * Adds stats based on the party member's job and which subclass they levelled up.
   * 
   * @param subclassUp The index of the subclass being levelled up.
   */
  private void addNewStats(int subclassUp){
    int[] newStats;
    if(job == Jobs.FIGHTER){
      changeStat(Stats.STR, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MAG, 1+subclassLevels[subclassUp]);
      changeStat(Stats.MMP, (1+subclassLevels[subclassUp])* 3);
      changeStat(Stats.VIT, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MHP, (2+subclassLevels[subclassUp])* 5);
      changeStat(Stats.DEX, 2+subclassLevels[subclassUp]);
      
      if(subclassUp==0){
        changeStat(Stats.STR, 3);
      } else if(subclassUp==1){
        changeStat(Stats.VIT, 3);
      } else{
        changeStat(Stats.DEX, 3);
      }
      
      
    } else if(job == Jobs.MAGE){
      changeStat(Stats.STR, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MAG, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MMP, (2+subclassLevels[subclassUp])* 3);
      changeStat(Stats.VIT, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MHP, (2+subclassLevels[subclassUp])* 5);
      changeStat(Stats.DEX, 1+subclassLevels[subclassUp]);
      
      if(subclassUp==0){
        changeStat(Stats.STR, 3);
      }else if(subclassUp==1){ 
        changeStat(Stats.MAG, 3);
      }else {
        changeStat(Stats.VIT, 3);
      } 
      
    } else if(job == Jobs.CLERIC){
      changeStat(Stats.STR, 1+subclassLevels[subclassUp]);
      changeStat(Stats.MAG, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MMP, (2+subclassLevels[subclassUp])* 3);
      changeStat(Stats.VIT, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MHP, (2+subclassLevels[subclassUp])* 5);
      changeStat(Stats.DEX, 2+subclassLevels[subclassUp]);
      
      if(subclassUp==0){ 
        changeStat(Stats.MAG, 3);
      }else if(subclassUp==1){
        changeStat(Stats.VIT, 3);
      } else {
        changeStat(Stats.DEX, 3);
      }
    } else{
      changeStat(Stats.STR, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MAG, 2+subclassLevels[subclassUp]);
      changeStat(Stats.MMP, (2+subclassLevels[subclassUp])* 3);
      changeStat(Stats.VIT, 1+subclassLevels[subclassUp]);
      changeStat(Stats.MHP, (1+subclassLevels[subclassUp])* 5);
      changeStat(Stats.DEX, 2+subclassLevels[subclassUp]);
      
      if(subclassUp==0){ 
        changeStat(Stats.STR, 3);
      }else if(subclassUp==1){
        changeStat(Stats.MAG, 3);
      } else {
        changeStat(Stats.DEX, 3);
      }
    }
  }
  
  /** 
   * Adds the 3 default skills based on the party member's job.
   */
  private void initializeClassSkills(){
    addSkill(getClassSkill(0,0));
    addSkill(getClassSkill(1,0));
    addSkill(getClassSkill(2,0));
  }
  
  /** 
   * Adds the new skill based on which subclass has been levelled up.
   * 
   * @param subclassUp The index of the subclass being levelled up.
   */
  private void addNewSkill(int subclassUp){
    addSkill(getClassSkill(subclassUp,subclassLevels[subclassUp]-1));
  }
  
  /** 
   * Gets a class skill from the complete list.
   * 
   * @param subclass The subclass of the skill.
   * @param skillLvl The level of the subclass corresponding to this skill.
   * @return The skill corresponding to the job, subclass, and subclass level.
   */
  public Skill getClassSkill(int subclass, int skillLvl){
    Skill[][][] allSkills = new Skill[4][3][4];
    int jobID;
    
    allSkills[0][0][0] = new Skill("Great Slash", 1.5, 10, false, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, "Slice one enemy with a physical attack.");
    allSkills[0][1][0] = new Skill("Ally Guard", 0, 10, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.FULLGUARD, "Nullify all damage to one ally until your next turn.");
    allSkills[0][2][0] = new Skill("Cross Cutter", 1.2, 20, false, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.NONE, "Physical damage to all enemies.");
    
    allSkills[0][0][1] = new Skill("Death Blade", 3, 33, false, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, "Heavy physical attack to one target.");
    allSkills[0][0][2] = new Skill("Champion Rush", 2, 45, false, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.NONE, "Heavy physical attack to all enemies.");
    allSkills[0][0][3] = new Skill("Sacrifice", 5, 60, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.SACRIFICE, "Costs a lot of HP and deals damage in relation to the HP lost.");
    
    allSkills[0][1][1] = new Skill("Holy Song", 0, 8, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.DEFUP, "Increase defence of all allies.");
    allSkills[0][1][2] = new Skill("Healing Light", 40, 6, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.HEAL, "Heal 40% of a party member's HP.");
    allSkills[0][1][3] = new Skill("Healing Prayer", 30, 12, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.HEAL, "Heal 30% HP to all allies.");
    
    allSkills[0][2][1] = new Skill("Life Trick", 1, 10, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.DRAINHP, "Drain HP from one target.");
    allSkills[0][2][2] = new Skill("Blade Rush", 1, 20, true, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.DRAINHP, "Drain HP from all targets.");
    allSkills[0][2][3] = new Skill("Grand Repose", 3, 30, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.DRAINHP, "Drain a lot of HP from one target.");
    
    allSkills[1][0][0] =new Skill("Fireball", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.FIRE, SkillEffect.NONE, "Launch a fireball at one enemy.");
    allSkills[1][1][0] =new Skill("Ice Shards", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.ICE, SkillEffect.NONE, "Launch ice shards at one enemy.");
    allSkills[1][2][0] =new Skill("Thunder Bolt", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.ELEC, SkillEffect.NONE, "Launch a thunderbolt at one enemy.");
    
    allSkills[1][0][1] =new Skill("Gravity Fist", 50, 15, true, TargetType.ONE_ENEMY, Elements.GRAV, SkillEffect.NONE, "50% chance to instantly kill one enemy. Higher STR increases the odds.");
    allSkills[1][0][2] =new Skill("Gravity Field", 30, 20, true, TargetType.ALL_ENEMIES, Elements.GRAV, SkillEffect.NONE, "30% chance to instantly kill all enemies. Higher STR increases the odds.");   
    allSkills[1][0][3] =new Skill("Zero Grav Drain", 1, 15, true, TargetType.ONE_ENEMY, Elements.NONE, SkillEffect.DRAINMP, "Drain MP from one target.");
    
    allSkills[1][1][1] =new Skill("Hellfire", 1.5, 15, true, TargetType.ALL_ENEMIES, Elements.FIRE, SkillEffect.NONE, "Fire damage to all enemies.");
    allSkills[1][1][2] =new Skill("Blizzard", 1.5, 15, true, TargetType.ALL_ENEMIES, Elements.ICE, SkillEffect.NONE, "Ice damage to all enemies.");
    allSkills[1][1][3] =new Skill("Thunderstorm", 1.5, 15, true, TargetType.ALL_ENEMIES, Elements.ELEC, SkillEffect.NONE, "Elec damage to all enemies.");
    
    allSkills[1][2][1] =new Skill("Blood Spear", 2, 20, false, TargetType.ONE_ENEMY, Elements.BLOOD, SkillEffect.NONE, "Blood damage to one enemy.");
    allSkills[1][2][2] =new Skill("Blood Whip", 1.5, 30, false, TargetType.ALL_ENEMIES, Elements.BLOOD, SkillEffect.NONE, "Blood damage to all enemies.");
    allSkills[1][2][3] =new Skill("Vampire", 1, 10, true, TargetType.ONE_ENEMY, Elements.NONE, SkillEffect.DRAINHP, "Drain HP from one target.");
    
    allSkills[2][0][0] =new Skill("Heal", 40, 6, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.HEAL, "Heal 40% of a party member's HP.");
    allSkills[2][1][0] =new Skill("Magic Force", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.NONE, SkillEffect.NONE, "Deal holy damage to a single enemy.");
    allSkills[2][2][0] =new Skill("Inspiration", 0, 8, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.ATKUP, "Increase all allies' attack.");
    
    allSkills[2][0][1] =new Skill("Dispel", 0, 15, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.DISPEL, "Remove stat buffs from enemies.");
    allSkills[2][0][2] =new Skill("Light Blast", 1.5, 15, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.NONE, "Holy damage to all enemies.");
    allSkills[2][0][3] =new Skill("Mana Wish", 0, 10, false, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.MPUP, "Restore MP to all allies based on user's MAG.");
    
    allSkills[2][1][1] =new Skill("Life Surge", 30, 12, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.HEAL, "Heal 30% HP to all allies.");
    allSkills[2][1][2] =new Skill("Cure", 100, 15, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.HEAL, "Heal all HP to one ally.");
    allSkills[2][1][3] =new Skill("Prayer", 100, 30, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.HEAL, "Heal all HP to all allies.");
    
    allSkills[2][2][1] =new Skill("Barrier", 0, 8, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.DEFUP, "Increase all allies' defence.");
    allSkills[2][2][2] =new Skill("Restoration", 0, 10, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.RESTORE, "Reset negative stat modifiers all allies.");
    allSkills[2][2][3] =new Skill("Revive", 50, 15, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.REVIVE, "Revive a KOd ally.");
    
    allSkills[3][0][0] =new Skill("Snipe Shot", 1.5, 10, false, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, "Launch an arrow at an enemy's weak point to deal physical damage.");
    allSkills[3][1][0] =new Skill("Fire Arrow", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.FIRE, SkillEffect.NONE, "Launch an arrow made of fire at a single enemy.");
    allSkills[3][2][0] =new Skill("Weakness Bomb", 0, 8, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.ATKDOWN, "Throw a poisonous bomb that lowers all enemies' attack.");
    
    allSkills[3][0][1] =new Skill("Break Arrow", 1.3, 15, false, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.DEFDOWN2, "Deal physical damage to one enemy and greatly lower defence.");
    allSkills[3][0][2] =new Skill("Squall", 1.5, 25, true, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.NONE, "Physical damage to all enemies.");
    allSkills[3][0][3] =new Skill("Weakness Arrow", 1.3, 15, false, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.ATKDOWN2, "Deal physical damage to one enemy and greatly lower attack.");
    
    allSkills[3][1][1] =new Skill("Ice Arrow", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.ICE, SkillEffect.NONE, "Launch an arrow made of ice at a single enemy.");
    allSkills[3][1][2] =new Skill("Shock Arrow", 1.5, 3, true, TargetType.ONE_ENEMY, Elements.ELEC, SkillEffect.NONE, "Launch an arrow made of electricity at a single enemy.");
    allSkills[3][1][3] =new Skill("Healing Wind", 30, 12, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.HEAL, "Heal 30% HP to all allies.");
    
    allSkills[3][2][1] =new Skill("Break Bomb", 0, 8, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.DEFDOWN, "Throw a poisonous bomb that lowers all enemies' defence.");
    allSkills[3][2][2] =new Skill("First Aid", 40, 6, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.HEAL, "Heal 40% of a party member's HP.");
    allSkills[3][2][3] =new Skill("Resuscitate", 50, 15, true, TargetType.ONE_ALLY, Elements.NONE, SkillEffect.REVIVE, "Revive a KOd ally.");
    
    if(job==Jobs.FIGHTER){
      jobID = 0;
    }else if(job==Jobs.MAGE){
      jobID = 1;
    }else if(job==Jobs.CLERIC){
      jobID = 2;
    }else {
      jobID = 3;
    }
    
    return allSkills[jobID][ subclass][skillLvl];
  } 
  
  /** 
   * Gets the party member's menu information.
   * 
   * @return String containing information to display to the menu.
   */
  public String getMenuInformation(){
    return String.format("%-15s",getName())+" "+String.format("%-10s",getJobName())+" HP:"+String.format("%3d",getStat(Stats.HP))+"/"+String.format("%3d",getStat(Stats.MHP))
      +" MP:"+String.format("%3d",getStat(Stats.MP))+"/"+String.format("%3d",getStat(Stats.MMP));
  }
  
  /** 
   * Gets an arraylist of all skills that can be used in the menu.
   * Only healing skills can be used from the menu.
   * 
   * @return ArrayList of skills that can be used from the menu.
   */
  public ArrayList<Skill> getMenuSkills (){
    ArrayList<Skill> skills = getSkills();
    ArrayList<Skill> menuSkills = new ArrayList<Skill>();
    
    
    for(int i=0;i<skills.size();i++){
      if(skills.get(i).getEffect()==SkillEffect.HEAL){
        menuSkills.add(skills.get(i));
      }
    }
    
    return menuSkills;
    
  }
  
  /** 
   * Gets the party member's status information.
   * This information contains all stats and skills of the party member. 
   * 
   * @return String containing the status information.
   */
  public String getStatusInformation(){
    String output="";
    ArrayList<Skill> skills = getSkills();
    
    output += String.format("%-15s",getName())+" "+String.format("%-10s",getJobName())+"\n"+" Level: "+level+"\n";
    output += " HP: "+String.format("%3d",getStat(Stats.HP))+"/"+String.format("%3d",getStat(Stats.MHP))+"   ";
    output +=" MP: "+String.format("%3d",getStat(Stats.MP))+"/"+String.format("%3d",getStat(Stats.MMP))+"\n";
    output +=" STR: "+String.format("%2d",getStat(Stats.STR))+"    MAG: "+String.format("%2d",getStat(Stats.MAG))+"    ";
    output +=" VIT: "+String.format("%2d",getStat(Stats.VIT))+"    DEX: "+String.format("%2d",getStat(Stats.DEX))+"\n";
    
    output +="\n Skills:\n";
    for(int i=0;i<skills.size();i++){
      output+=String.format("%-15s",skills.get(i).getName())+":   "+skills.get(i).getDescription()+" "+skills.get(i).getCostString(this)+"\n";
    }
    return output;
  }
  
  /** 
   * Gets the name of the party member's job.
   * 
   * @param The name of the job.
   */
  private String getJobName(){
    String name;
    if(job == Jobs.FIGHTER){
      name = "Fighter";
      
    } else if(job == Jobs.MAGE){
      name = "Mage";
      
    } else if(job == Jobs.CLERIC){
      name = "Cleric";
    } else{
      name = "Archer";
    }
    
    return name;
  }
  
  /** 
   * Increases the party member's level.
   * 
   * @param subclassUp The subclass that is being levelled up.
   */
  public void levelUp(int subclassUp){
    level++;
    subclassLevels[subclassUp]++;
    
    addNewStats(subclassUp);
    
    if(subclassLevels[subclassUp]<=4){
      addNewSkill(subclassUp);
    }
  }
}