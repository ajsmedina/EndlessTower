
import java.util.ArrayList;

/** 
 * Troop
 * 
 * A troop is a team consisting of enemies.
 * The enemies in a troop are randomly generated based on the troop's power.
 */
public class Troop extends Team{
  /** 
   * The strength of the encounter. Number is lowered as enemies are added.
   */
  private int power;
  
  /** 
   * The original strength of the encounter.
   */
  private int ogpower;
  
  /** 
   * True if this troop is a boss battle, False if not.
   */
  private boolean isBoss;
  
  /** 
   * Constructor for the Troop.
   * 
   * @param xpower The strength of the troop
   * @param xisBoss True if this troop is a boss battle.
   */
  public Troop(int xpower, boolean xisBoss){
    power = xpower<=5 ? 4 : xpower;
    ogpower = power;
    isBoss = xisBoss;
    if(isBoss){
      createBoss(); //Boss has no maximum number of traits, they just need to meet the power. They also have a selection of legendary traits 
    }else{
      createEnemies();
    }
  }
  
  /** 
   * Creates an enemy party of one enemy (the boss) and assign traits to it based on its power.
   */
  private void createBoss(){
    Enemy newEnemy; 
    
    do{
      newEnemy = selectBase();
      selectLegendaryTrait(newEnemy);
      selectTrait(newEnemy);
    }while(power>=2);
    setMembers(new Enemy[]{newEnemy});
  }
  
  /** 
   * Creates an enemy party of up to four enemies and assigns traits based on the troop's power.
   */
  private void createEnemies(){
    Enemy newEnemy; 
    boolean repeatsExist=false;;
    ArrayList<Enemy> enemyMembers = new ArrayList<Enemy>();
    
    do{
      enemyMembers.clear();
      power = ogpower;
      while(power>=2 && enemyMembers.size()<=4){
        newEnemy = selectBase();
        
        for(int i=0;i<3;i++){
          selectTrait(newEnemy);
        }
        
        enemyMembers.add(newEnemy);
        repeatsExist = checkRepeats(enemyMembers);
      }
    }while(repeatsExist);
    setMembers(enemyMembers.toArray(new Enemy[enemyMembers.size()]));
  }
  
  
  /** 
   * Checks if any enemies in the troop have the EXACT same name.
   * Similar names are OK (ex. Can have Fiery Buff Slime and Buff Fiery Slime,
   * but can't have both Slime and Slime).
   * 
   * @return True if repeats exist, false if they don't.
   */
  private boolean checkRepeats(ArrayList<Enemy> enemyMembers){
    Enemy first, second;
    boolean repeats = false;
    
    for(int i=0; i<enemyMembers.size()-1;i++){
      first = enemyMembers.get(i);
      for(int j=i+1; j<enemyMembers.size();j++){
        second = enemyMembers.get(j);
        if(first.getName().equals(second.getName())){
          repeats = true;
          break;
        }
      }
    }
    
    return repeats;
  }
  
  /** 
   * Gets the gold held by every enemy in the troop.
   * 
   * @return The troop's gold.
   */
  public int getGold(){
    int gold=0;
    for(int i=0; i<getLength();i++){
      gold+=((Enemy)getMember(i)).getGold();
    }
    
    return gold;
  }
  
  /** 
   * Checks if the current troop is a boss battle.
   * 
   * @return True if it's a boss battle, False if it isn't
   */
  public boolean checkBossBattle(){
    return isBoss;
  }
  
  
  /** 
   * Selects the base for the enemy. This determines default features before traits.
   * 
   * @return The enemy object without any traits.
   */
  private Enemy selectBase(){
    Enemy newEnemy = new Enemy("x", new int[]{10,10,10,10,7,5,5,5}, 5);
    boolean baseSelected=false;
    int randomInt;
    
    while(!baseSelected){
      randomInt = (int)(Math.random()* 7);
      
      switch(randomInt){
        case 0:
          if(power>=2){
          newEnemy = new Enemy("Skeleton", new int[]{20,20,10,10,7,5,7,5}, 20);
          newEnemy.addSkill(new Skill("Bone Rush", 1.3, 5, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, "Basic physical attack."));
          baseSelected=true;
          power-=2;
          break;
        }
        case 1:
          if(power>=2){
          newEnemy = new Enemy("Slime", new int[]{20,20,10,10,5,7,7,5}, 20);
          newEnemy.addSkill(new Skill("Poison Breath", 0.7, 5, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.NONE, "Light poison damage all enemies."));
          power-=2;
          baseSelected=true;
          break;
        }
        case 2:
          if(power>=2){
          newEnemy = new Enemy("Rat", new int[]{20,20,10,10,5,5,7,7}, 20);
          newEnemy.addSkill(new Skill("Crush Claw", 1, 5, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.DEFDOWN, "Lowers defense of one target."));
          power-=2;
          baseSelected=true;
          break;
        }
        case 3:
          if(power>=3){
          newEnemy = new Enemy("Zombie", new int[]{30,30,20,20,10,5,10,10}, 30);
          newEnemy.addSkill(new Skill("Bite", 1.5, 5, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, ""));
          newEnemy.addSkill(new Skill("Rush", 1.3, 15, true, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.NONE, ""));
          power-=3;
          baseSelected=true;
          break;
        }
        case 4:
          if(power>=5){
          newEnemy = new Enemy("Mummy", new int[]{50,50,30,30,12,12,10,10}, 60);
          newEnemy.addSkill(new Skill("Death Stroke", 2, 5, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, ""));
          newEnemy.addSkill(new Skill("Curse", 1, 5, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.DEFDOWN2, ""));
          newEnemy.addSkill(new Skill("Hex", 1, 5, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.ATKDOWN2, ""));
          power-=5;
          baseSelected=true;
          break;
        }
        case 5:
          if(power>=5){
          newEnemy = new Enemy("Ghost", new int[]{50,50,30,30,2,14,10,15}, 60);
          newEnemy.addSkill(new Skill("Ghostly Mist", 1.5, 5, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.NONE, ""));
          newEnemy.addSkill(new Skill("Curse", 1, 5, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.DEFDOWN2, ""));
          newEnemy.addSkill(new Skill("Hex", 1, 5, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.ATKDOWN2, ""));
          power-=5;
          baseSelected=true;
          break;
        }
        case 6:
          if(power>=5){
          newEnemy = new Enemy("Goblin", new int[]{50,50,30,30,15,1,12,12}, 60);
          newEnemy.addSkill(new Skill("Goblin Slash", 1.5, 5, true, TargetType.ONE_ENEMY, Elements.PHYS, SkillEffect.NONE, ""));
          newEnemy.addSkill(new Skill("Goblin Squall", 1.5, 10, true, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.NONE, ""));
          power-=5;
          baseSelected=true;
          break;
        }
        default:
          newEnemy = new Enemy("Dog", new int[]{10,10,10,10,5,5,5,5}, 5);
          break;
          
      }
      
    }
    
    return newEnemy;
  }
  
  /** 
   * Adds boss-exclusive traits to the boss based on its power.
   * 
   * @param The enemy object to add trait to.
   */
  private void selectLegendaryTrait(Enemy newEnemy){
    int randomInt;
    
    if(power >=2){
      randomInt = (int)(Math.random()* 5); //0-2
      
      switch(randomInt){
        //no trait if 0
        case 1:
          if(power >= 5 && newEnemy.checkTrait("Holy")){
          newEnemy.addName("Holy");
          newEnemy.changeGold(+50);
          newEnemy.changeStat(Stats.MAG, +20);
          newEnemy.changeStat(Stats.VIT, +10);
          newEnemy.changeStat(Stats.DEX, +10);
          newEnemy.addSkill(new Skill("Holy Shine", 2, 25, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.NONE, ""));
          power -=5;
        }
          break;
        case 2:
          if(power >= 5 && newEnemy.checkTrait("Burning")){
          newEnemy.addName("Burning");
          newEnemy.changeGold(+50);
          newEnemy.changeStat(Stats.MAG, +20);
          newEnemy.changeStat(Stats.VIT, +10);
          newEnemy.changeStat(Stats.DEX, +10);
          newEnemy.changeAffinity(Elements.FIRE, -0.5);
          newEnemy.changeAffinity(Elements.ICE, +1);
          newEnemy.addSkill(new Skill("Fireswarm", 1.5, 10, true, TargetType.ALL_ENEMIES, Elements.FIRE, SkillEffect.NONE, ""));
          power -=5;
        }
          break;
        case 3:
          if(power >= 5 && newEnemy.checkTrait("Freezing")){
          newEnemy.addName("Freezing");
          newEnemy.changeGold(+50);
          newEnemy.changeStat(Stats.MAG, +20);
          newEnemy.changeStat(Stats.DEX, +10);
          newEnemy.changeStat(Stats.VIT, +10);
          newEnemy.changeAffinity(Elements.ICE, -0.5);
          newEnemy.changeAffinity(Elements.FIRE, +1);
          newEnemy.addSkill(new Skill("Avalanche", 1.5, 10, true, TargetType.ALL_ENEMIES, Elements.ICE, SkillEffect.NONE, ""));
          power -=5;
        }
          break;
        case 4:
          if(power >= 5 && newEnemy.checkTrait("Berserk")){
          newEnemy.addName("Berserk");
          newEnemy.changeGold(+50);
          newEnemy.changeStat(Stats.MMP, +50);
          newEnemy.changeStat(Stats.STR, +10);
          newEnemy.changeStat(Stats.DEX, +10);
          newEnemy.changeStat(Stats.VIT, +10);
          newEnemy.changeAffinity(Elements.FIRE, +0.5);
          newEnemy.changeAffinity(Elements.ICE, +0.5);
          newEnemy.addSkill(new Skill("Frenzy", 1.5, 10, true, TargetType.ALL_ENEMIES, Elements.PHYS, SkillEffect.NONE, ""));
          power -=5;
        }
          break;
        default:
          break;
          
      }
      
    }
    
  }
  
  
  /** 
   * Adds generic traits to the enemy based on the troop's power.
   * 
   * @param The enemy object to add trait to.
   */
  private void selectTrait(Enemy newEnemy){
    int randomInt;
    
    if(power >=2){
      randomInt = (int)(Math.random()* 9); //0-2
      
      switch(randomInt){
        //no trait if 0
        case 1:
          if(power >= 2 && newEnemy.checkTrait("Buff")){
          newEnemy.addName("Buff");
          newEnemy.changeGold(+20);
          newEnemy.changeStat(Stats.VIT, +10);
          newEnemy.changeStat(Stats.STR, +10);
          newEnemy.addSkill(new Skill("Heavy Strike", 1.5, 6, true, TargetType.ONE_ENEMY, Elements.NONE, SkillEffect.NONE, ""));
          power -=2;
        }
          break;
        case 2:
          if(power >= 2 && newEnemy.checkTrait("Fiery")){
          newEnemy.addName("Fiery");
          newEnemy.changeGold(+20);
          newEnemy.changeStat(Stats.MAG, +10);
          newEnemy.changeAffinity(Elements.FIRE, -0.5);
          newEnemy.changeAffinity(Elements.ICE, +1);
          newEnemy.addSkill(new Skill("Fireball", 1.5, 5, true, TargetType.ONE_ENEMY, Elements.FIRE, SkillEffect.NONE, ""));
          power -=2;
        }
          break;
        case 3:
          if(power >= 7 && newEnemy.checkTrait("Great")){
          newEnemy.addName("Great");
          newEnemy.changeGold(+50);
          newEnemy.changeStat(Stats.STR, +10);
          newEnemy.changeStat(Stats.MAG, +10);
          newEnemy.changeStat(Stats.VIT, +10);
          newEnemy.changeStat(Stats.DEX, +10);
          newEnemy.addSkill(new Skill("War Cry", 0, 10, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.ATKUP, ""));
          power -=7;
        }
        case 4:
          if(power >= 5 && newEnemy.checkTrait("Flying")){
          newEnemy.addName("Flying");
          newEnemy.changeGold(+20);
          newEnemy.changeStat(Stats.MAG, +10);
          newEnemy.changeStat(Stats.DEX, +10);
          newEnemy.changeAffinity(Elements.ELEC, +1);
          newEnemy.addSkill(new Skill("Turbulent Winds", 1.5, 10, true, TargetType.ALL_ENEMIES, Elements.NONE, SkillEffect.NONE, ""));
          power -=5;
        }
          break;
        case 5:
          if(power >= 4 && newEnemy.checkTrait("Armored")){
          newEnemy.addName("Armored");
          newEnemy.changeGold(+20);
          newEnemy.changeStat(Stats.VIT, +15);
          newEnemy.changeStat(Stats.DEX, -5);
          newEnemy.changeAffinity(Elements.ELEC, +0.5);
          newEnemy.addSkill(new Skill("Iron Defence", 0, 10, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.DEFUP, ""));
          power -=4;
        }
          break;
        case 6:
          if(power >= 15 && newEnemy.checkTrait("Super")){
          newEnemy.addName("Super");
          newEnemy.changeGold(+50);
          newEnemy.changeStat(Stats.STR, +25);
          newEnemy.changeStat(Stats.MAG, +25);
          newEnemy.changeStat(Stats.VIT, +25);
          newEnemy.changeStat(Stats.DEX, +25);
          newEnemy.addSkill(new Skill("War Cry", 0, 10, true, TargetType.ALL_ALLIES, Elements.NONE, SkillEffect.ATKUP, ""));
          power -=15;
        }
        case 7:
          if(power >= 3 && newEnemy.checkTrait("Icy")){
          newEnemy.addName("Icy");
          newEnemy.changeGold(+15);
          newEnemy.changeStat(Stats.MAG, +10);
          newEnemy.changeAffinity(Elements.ICE, -0.5);
          newEnemy.changeAffinity(Elements.FIRE, +1);
          newEnemy.addSkill(new Skill("Icicle Rain", 1.3, 10, true, TargetType.ALL_ENEMIES, Elements.ICE, SkillEffect.NONE, ""));
          power -=3;
        }
          break;
        default:
          break;
          
      }
      
    }
    
  }
  
  
}