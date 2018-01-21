

import javax.swing.* ;
import java.awt.Font;
import java.util.ArrayList;
import java.io.* ;

public class EndlessTower {
  
  public static void main(String[] args)throws IOException {
    setFont();
    runMainMenu();
  }
  
  /** 
   * Sets the font of the OptionsPand to a monospaced font.
   */
  public static void setFont(){
    javax.swing.UIManager.put("OptionPane.messageFont", (new Font("Monospaced", Font.BOLD, 12)));
  }
  
  /** 
   * Runs the main menu of the game.
   */
  public static void runMainMenu()throws IOException{
    int menuChoice;
    Map newFloor;
    Party party;
    
    while(true){
      menuChoice = getMenuOption("Endless Tower\n\nChoose an option:", new String[]{"Explore the Tower","View High Score","How to Play","Quit"});
      if(menuChoice==0){
        displayIntro();
        party = createParty();
        runMap(party);
      }
      else if(menuChoice==1){
        displayHighScore();
      }
      else if(menuChoice==2){
        displayHelp();
      }
      else{
        break;
      }
      
    }
  }
  
  /** 
   * Displays the game's instructions.
   */
  public static void displayHelp()throws IOException{
    BufferedReader readFile = new BufferedReader(new FileReader("RPG/help.txt"));;
   boolean continueReadingHelp = true;
   String helptext="";
    String fileLine;
    
    while(continueReadingHelp) {
      fileLine = readFile.readLine();
      
      if(fileLine == null){
        continueReadingHelp=false;
      }else{
       helptext+=fileLine+"\n";
      }
    }
    
    displayMessage(helptext);
  }
  /** 
   * Displays the high score information to the screen.
   */
  public static void displayHighScore()throws IOException{
    BufferedReader readFile;
    String display="";
    
    try{
      readFile = new BufferedReader(new FileReader("ETHS.txt"));
      display+="High Score:\n\n";
      display+=String.format("%-10s","Floor: ")+readFile.readLine()+"\n";
      display+=String.format("%-10s","Player: ")+readFile.readLine()+"\n";
      display+=String.format("%-10s","Fighter: ")+readFile.readLine()+"\n";
      display+=String.format("%-10s","Mage: ")+readFile.readLine()+"\n";
      display+=String.format("%-10s","Cleric: ")+readFile.readLine()+"\n";
      display+=String.format("%-10s","Archer: ")+readFile.readLine()+"\n";
      
      readFile.close();
      displayMessage(display);
    }
    catch(FileNotFoundException fnfe){
      displayMessage("Error displaying the high scores.");
    }
  }
  
  
  /** 
   * Checks if the user has set a new high score.
   */
  public static boolean checkNewHighScore(int floor) throws IOException{
    BufferedReader readFile;
    String display="";
    int oldHS;
    
    try{
      readFile = new BufferedReader(new FileReader("ETHS.txt"));
      oldHS=Integer.parseInt(readFile.readLine());
      readFile.close();
      return floor>oldHS;
    }
    catch(FileNotFoundException fnfe){
      displayMessage("Error comparing high scores.");
      return false;
    }
  }
  
  /** 
   * Ends the game. If the user has set a high score, save the score.
   * 
   * @param party The player's party.
   * @param retired True if the player chose to end the game at a shrine.
   */
  public static void gameOver(Party party, boolean retired) throws IOException{
    
    if(retired){
      displayMessage("The party has decided to retire their expedition of the Endless Tower at floor "+party.getFloor()+".");
    } else{
      displayMessage("The party has fallen in the Endless Tower at floor "+party.getFloor()+".");
    }
    if(checkNewHighScore(party.getFloor())){
      saveHighScore(party);
      displayHighScore();
    }
  }
  
  /** 
   * Saves the user's high score in the high score file. Overwrites the old high score.
   * 
   * @param party The player's party.
   */
  public static void saveHighScore(Party party) throws IOException{
    String name="";
    PrintWriter fileOut = new PrintWriter(new FileWriter("ETHS.txt"));
    
    displayMessage("You've set a new high score!");
    while(name==null || name.length()==0){
      name = JOptionPane.showInputDialog("Please enter your name:");
    }
    
    fileOut.println(party.getFloor());
    fileOut.println(name);
    for(int i=0; i<party.getLength();i++){
      fileOut.println(party.getMember(i).getName());
    }
    fileOut.close();
  }
  
  /** 
   * Creates a new party consisting of a user-named fighter, mage, cleric, and rogue.
   * 
   * @return The new party.
   */
  public static Party createParty(){
    PartyMember[] members = new PartyMember[4];
    String[] names= new String[4];
    
    while(names[0]==null || names[0].length()==0){
      names[0] = JOptionPane.showInputDialog("Please enter the name of your valiant fighter:");
    }
    while(names[1]==null || names[1].length()==0){
      names[1] = JOptionPane.showInputDialog("Please enter the name of your intelligent mage:");
    }
    while(names[2]==null || names[2].length()==0){
      names[2] = JOptionPane.showInputDialog("Please enter the name of your devout cleric:");
    }
    while(names[3]==null || names[3].length()==0){
      names[3] = JOptionPane.showInputDialog("Please enter the name of your agile archer:");
    }
    
    members[0] = new PartyMember(names[0],Jobs.FIGHTER);
    members[1] = new PartyMember(names[1],Jobs.MAGE);
    members[2] = new PartyMember(names[2],Jobs.CLERIC);
    members[3] = new PartyMember(names[3],Jobs.ARCHER);
    
    return new Party(members);
  }
  
  
  /** 
   * Displays the game's welcome text.
   */
  public static void displayIntro(){
    JOptionPane.showMessageDialog(null, "Welcome to the endless tower!\nPlease name your explorers.", "Endless Tower", JOptionPane.INFORMATION_MESSAGE);
  }
  
  
  /** 
   * Runs the floor exploration of the game.
   * 
   * @param party The player's party.
   */
  public static void runMap(Party party) throws IOException{
    int menuOption, itemFound;
    Tile event=Tile.UNKNOWN;
    String display="";
    boolean partyAlive=true;
    boolean quit = false;
    Map floorMap = new Map();
    
    while(!quit){
      if(event==Tile.WALL){
        display+="There's a wall in that direction!\n";
      } else if (event==Tile.EXIT){
        party.floorUp();
        
        if(party.getFloor()%5==0){
          quit = enterShrine(party);
          if(quit){
            gameOver(party, true);
            break;
          }
        }
        
        if(party.getFloor()%10==0){
          partyAlive=startBattle(party, party.getFloor(), true);
          
          if(!partyAlive){
            break;
          }
        }
        floorMap = new Map();
      } else if (event==Tile.AV_BATTLE){
        partyAlive=startBattle(party, party.getFloor(), false);
        floorMap.resetPlayerTile();
      }else if (event==Tile.WK_BATTLE){
        partyAlive=startBattle(party, party.getFloor()/2, false);
        floorMap.resetPlayerTile();
      }else if (event==Tile.ST_BATTLE){
        partyAlive=startBattle(party, (int)(party.getFloor()* 2), false);
        floorMap.resetPlayerTile();
      } else if (event==Tile.CHEST){
        itemFound = (int)(Math.random()* 21);
        if(itemFound==20){
          party.changeGold(party.getFloor()* 20);
          displayMessage("You found a treasure chest!\n"+"The chest contains "+party.getFloor()* 20+" gold!");
        }else{
          party.addItem(itemFound, 1);
          displayMessage("You found a treasure chest!\n"+"The chest contains a "+party.getItemName(itemFound)+".");
        }
        floorMap.resetPlayerTile();
      }
      
      if(!partyAlive){
        gameOver(party, false);
        break;
      }
      
      
      display+=floorMap.toString();
      display+="\nWhat would you like to do?";
      
      menuOption = getMenuOption(display, new String[]{"Left","Up","Down","Right","Menu","Legend"});
      event=Tile.FLOOR;
      display="";
      if(menuOption==0){
        event=moveMapPos(floorMap, "left");
      }
      else if(menuOption==1){
        event=moveMapPos(floorMap, "up");
      }
      else if(menuOption==2){
        event=moveMapPos(floorMap, "down");
      }
      else if(menuOption==3){
        event=moveMapPos(floorMap, "right");
      }
      else if(menuOption==4){
        quit = displayStatusMenu(party);
      } else{
        displayLegend();
      }
      
    }
  }
  
  /** 
   * Displays a legend of all map tiles to aid the user's exploration.
   */
  public static void displayLegend(){
    String legend="Legend:\n\n";
    
    legend+="[O] - Player\n";
    legend+="[_] - Floor\n";
    legend+="[X] - Wall\n";
    legend+="[E] - Enemy Encounter\n";
    legend+="[T] - Treasure Chest\n";
    legend+="[S] - Stairs to Next Flood\n";
    
    displayMessage(legend);
  }
  
  /** 
   * Runs the shrine menu that allows the player to level up or give up.
   * 
   * @param party The player's party.
   * @return True if the user is retiring the party.
   */
  public static boolean enterShrine(Party party){
    int menuOption;
    boolean quit;
    String shrineText;
    
    party.recoverParty();
    while(true){
      shrineText = "Welcome to the shrine!\n";
      shrineText += "Floor: "+party.getFloor()+"\n";
      shrineText += "Gold: "+party.getGold()+"\n";
      shrineText += "\nWhat would you like to do?:";
      menuOption = getMenuOption(shrineText, new String[]{"Level Up","Next Floor","Retire Expedition"});
      
      if(menuOption==0){
        displayLevelUpMenu(party);
      }else if(menuOption==1){
        quit = false;
        break;
      } else {
        quit = confirmRetire();
        if(quit){
          break;
        }
      }
    }
    
    return quit;
  }
  
  
  /** 
   * Displays the level up menu. Allows the user to spend gold to level up characters.
   * 
   * @param party The player's party.
   */
  public static void displayLevelUpMenu(Party party){
    String status;
    PartyMember target;
    int menuOption;
    String[] options = new String[5];
    
    options[4] = "Back";
    
    while(true){
      status = "";
      for(int i=0; i<party.getAllMembers().length;i++){
        target = (PartyMember)(party.getMember(i));
        status+=target.getMenuInformation()+"\n";
        options[i] = target.getName();
      }
      
      menuOption = getMenuOption("Choose a party member to level up:\n"+status+"Gold: "+party.getGold(), options);
      if(menuOption<4){
        displayClassSelectionMenu(party, (PartyMember)party.getMember(menuOption));
      }else{
        break;
      }
    }
  }
  
  
  /** 
   * Displays the class selection menu. Allows the player to choose a subclass to level up.
   * 
   * @param party The player's party.
   * @param candidate The party member to level up.
   */
  public static void displayClassSelectionMenu(Party party, PartyMember candidate){
    String display;
    PartyMember target;
    int menuOption;
    String[] options;
    
    while(true){
      display = getLevelUpDisplay(candidate);
      
      if(candidate.getJob()==Jobs.FIGHTER){
        options = new String[]{"Champion", "Paladin", "Blade Master", "Back"};
      } else if(candidate.getJob()==Jobs.MAGE){
        options = new String[]{"Gravity Sorcerer", "Ruinist", "Blood Mage", "Back"};
      } else if(candidate.getJob()==Jobs.CLERIC){
        options = new String[]{"Exorcist", "Diviner", "Summoner", "Back"};
      } else {
        options = new String[]{"Hunter", "Druid", "Plague Doctor", "Back"};
      }
      
      menuOption = getMenuOption(display, options);
      if(menuOption<3){
        confirmLevelUp(party, candidate, menuOption, options[menuOption]);
      }else{
        break;
      }
    }
  }
  
  
  /** 
   * Displays the cost of the level up and has the player confirm their decision.
   * If the player does not have enough gold, display that message.
   * 
   * @param party The player's party.
   * @param candidate The party member to level up.
   * @param subclassUp The index of the subclass to level up.
   * @param subclassName The name of the subclass to level up.
   */
  public static void confirmLevelUp(Party party, PartyMember candidate, int subclassUp, String subclassName){
    String display="";
    int subclassLvl = candidate.getSubclassLevels()[subclassUp];
    int cost, menuOption;
    
    cost = 25 * (int)Math.pow(3,subclassLvl-1);
    
    display+="Levelling "+subclassName+" to level "+subclassLvl+".\n";
    display+="Cost: "+cost+" Gold \n\n";
    display+="Gold: "+party.getGold()+" Gold \n\n";
    display+="Level up?\n";
    
    if(party.getGold()<cost){
      displayMessage(display+"Not enough gold!");
    } else {
      menuOption = getMenuOption(display, new String[]{"Yes","No"});
      if(menuOption==0){
        candidate.levelUp(subclassUp);
        party.changeGold(-cost);
        displayMessage("Level Up!\n"+candidate.getStatusInformation());
      }
    }
    
  }
  
  
  /** 
   * Gets the text to display at the level up screen.
   * 
   * @param candidate The party member trying to level up.
   * @return String the text to display to the screen.
   */
  public static String getLevelUpDisplay(PartyMember candidate){
    String display;
    Skill nextSkill;
    int[] subclassLevels;
    subclassLevels = candidate.getSubclassLevels();
    
    display = "";
    
    if(candidate.getJob()==Jobs.FIGHTER){
      display+="Champion (STR) - Current Level: ";
    } else if(candidate.getJob()==Jobs.MAGE){
      display+="Gravity Sorcerer (STR) - Current Level: ";
    } else if(candidate.getJob()==Jobs.CLERIC){
      display+="Exorcist (MAG) - Current Level: ";
    } else {
      display+="Hunter (STR) - Current Level: ";
    }
    
    display+=subclassLevels[0]+"\n";
    
    for(int i=subclassLevels[0]; i<4;i++){
      nextSkill = candidate.getClassSkill(0, i);
      display+="Level "+(i+1)+": "+nextSkill.getName()+" - "+nextSkill.getDescription()+"\n";
    }
    
    display+="\n";
    
    if(candidate.getJob()==Jobs.FIGHTER){
      display+="Paladin (VIT) - Current Level:";
    } else if(candidate.getJob()==Jobs.MAGE){
      display+="Ruinist (MAG) - Current Level:";
    } else if(candidate.getJob()==Jobs.CLERIC){
      display+="Diviner (VIT) - Current Level:";
    } else {
      display+="Druid (MAG) - Current Level:";
    }
    
    display+=subclassLevels[1]+"\n";
    
    for(int i=subclassLevels[1]; i<4;i++){
      nextSkill = candidate.getClassSkill(1, i);
      display+="Level "+(i+1)+": "+nextSkill.getName()+" - "+nextSkill.getDescription()+"\n";
    }
    
    display+="\n";
    
    if(candidate.getJob()==Jobs.FIGHTER){
      display+="Blademaster (DEX) - Current Level:";
    } else if(candidate.getJob()==Jobs.MAGE){
      display+="Blood Mage (VIT) - Current Level:";
    } else if(candidate.getJob()==Jobs.CLERIC){
      display+="Summoner (DEX) - Current Level:";
    } else {
      display+="Plague Doctor (DEX) - Current Level:";
    }
    
    display+=subclassLevels[2]+"\n";
    
    for(int i=subclassLevels[2]; i<4;i++){
      nextSkill = candidate.getClassSkill(2, i);
      display+="Level "+(i+1)+": "+nextSkill.getName()+" - "+nextSkill.getDescription()+"\n";
    }
    
    display += "\nChoose a subclass to level up:\n\n";
    return display;
  }
  
  
  /** 
   * Displays the exploration status menu. Allows the user to view party member status and use items.
   * 
   * @param party The player's party.
   */
  public static boolean displayStatusMenu(Party party){
    String status;
    PartyMember target;
    boolean quit = false;
    int menuOption;
    String[] options = new String[7];
    
    options[4] = "Items";
    options[5] = "Quit";
    options[6] = "Back";
    
    while(true){
      status = "Gold: "+party.getGold()+"\n";
      status += "Floor: "+party.getFloor()+"\n";
      for(int i=0; i<party.getAllMembers().length;i++){
        target = (PartyMember)(party.getMember(i));
        status+=target.getMenuInformation()+"\n";
        options[i] = target.getName();
      }
      
      status += "\nChoose a menu option:";
      
      menuOption = getMenuOption(status, options);
      if(menuOption<4){
        displayStatus((PartyMember)(party.getMember(menuOption)), party);
      }else if(menuOption==4){
        displayItemMenu(party);
      }else if(menuOption==5){
        quit = confirmQuit();
        if(quit){
          break;
        }
      }else{
        break;
      }
    }
    
    return quit;
  }
  
  /** 
   * Allows the user to quit the game.
   * Quitting in the middle of exploration does not count as a retired expedition and will not
   * create a high score.
   * 
   * @return True if the user is quitting.
   */
  public static boolean confirmQuit(){
    int menuOption;
    menuOption = getMenuOption("Are you sure you want to quit?\nAll progress will be lost. No high score will be saved.", new String[]{"Yes, I want to quit.","No"});
    return menuOption==0;
  }
  
  /** 
   * Allows the user to retire their expedition and quit the game.
   * 
   * @return True if the user is retiring.
   */
  public static boolean confirmRetire(){
    int menuOption;
    menuOption = getMenuOption("Are you sure you want to retire the expedition?\nAll progress will be lost.\nA High Score can be saved.", new String[]{"Yes, I want to retire.","No"});
    return menuOption==0;
  }
  
  
  /** 
   * Starts a battle with an enemy troop and returns the results.
   * 
   * @param battleParty The player's party.
   * @param troopPower The strength of the enemy.
   * @param isBoss True if this is a boss battle.
   * @return True if the player is alive, false if they have died.
   */
  public static boolean startBattle(Party battleParty, int troopPower, boolean isBoss){
    boolean victory;
    Troop battleTroop = new Troop(troopPower, isBoss);
    Battle newBattle = new Battle(battleParty, battleTroop);
    
    if(isBoss){
      displayMessage("The guardian of floor "+battleParty.getFloor()+" attacks!");
    }
    
    victory = executeBattleFlow(newBattle);
    
    return victory;
  }
  
  /** 
   * Runs a battle with an enemy troop and returns the results.
   * 
   * @return True if the player is alive, false if they have died.
   */
  public static boolean executeBattleFlow(Battle battle){
    BattleMember currentTurn;
    int target;
    int currentAction;
    Skill currentSkill;
    boolean turnDone=false, skillSelect=false, run = false;
    
    String output ="Enemies:\n";
    for(int i=0; i<battle.getTroop().getLength();i++){
      output+=battle.getTroop().getMember(i).getName()+"\n";
    }
    
    displayMessage(output);
    
    while(!battle.getParty().allMembersKO() && !battle.getTroop().allMembersKO() && !run){
      currentTurn = battle.getCurrentTurn();
      
      currentTurn.setGuard(1);
      
      if(currentTurn instanceof PartyMember){
        if(currentTurn.getKO()){
          displayMessage(currentTurn.getName()+" is knocked out!");
        }else{
          turnDone=false;
          while(!turnDone){
            currentAction = getCurrentAction((PartyMember)currentTurn, battle);
            
            if(currentAction==0){
              skillSelect=true;
              while(!turnDone){
                currentSkill = getSkillAction((PartyMember)currentTurn);
                if(currentSkill == null){
                  break;
                }
                while(true){
                  target = chooseTarget((PartyMember)currentTurn, currentSkill, getTargetTeam(currentSkill, battle));
                  if(target !=-1){
                    useAction(currentTurn, currentSkill, getTargetTeam(currentSkill, battle), target);
                    turnDone=true;
                  }
                  break;
                }
              }
            } else if(currentAction==1){
              currentTurn.setGuard(0.5);
              displayMessage(currentTurn.getName()+" is guarding.");
              turnDone=true;
            } else{
              if(battle.getTroop().checkBossBattle()){
                displayMessage("Can't run from a boss battle!");
              } else {
                run=true;
                turnDone=true;
              }
            }
          }
        }
      }else if(!currentTurn.getKO()){
        currentSkill = getEnemySkill((Enemy)currentTurn);
        displayMessage(currentTurn.getName()+" uses "+currentSkill.getName()+"!");
        target = getEnemyTarget(getEnemyTargetTeam(currentSkill, battle));
        useAction(currentTurn, currentSkill, getEnemyTargetTeam(currentSkill, battle), target);
      }
    }
    
    battle.getParty().reviveDeadMembers();
    if(run){
      displayMessage("Ran Away!");
      return true;
    } else if (battle.getTroop().allMembersKO()){
      battle.getParty().changeGold(battle.getTroop().getGold());
      displayMessage("Victory!\nEarned "+battle.getTroop().getGold()+" gold!");
      return true;
    } else{
      displayMessage("GAME OVER");
      return false;
    }
  }
  
  /** 
   * Gets a skill for the enemy to use.
   * 
   * @param enemy The enemy that is using the skill
   * @return The skill the enemy will use.
   */
  public static Skill getEnemySkill(Enemy enemy){
    ArrayList<Skill> enemySkills = enemy.getSkills();
    Skill useSkill;
    
    while(true){
      useSkill = enemySkills.get((int)(Math.random()* enemySkills.size()));
      
      if(useSkill.skillUsesMP()){
        if(enemy.getStat(Stats.MP)>=useSkill.getCost(enemy)){
          break;
        }
      } else{
        if(enemy.getStat(Stats.HP)>=useSkill.getCost(enemy)){
          break;
        }
      }
    }
    
    return useSkill;
  }
  
  /** 
   * Gets a random target for the enemy's skill.
   * 
   * @param targetTeam The team being affected by the skill.
   * @return The index of the targeted team member.
   */
  public static int getEnemyTarget(Team targetTeam){
    return (int)(Math.random()* targetTeam.getLength());
  }
  
  /** 
   * Displays a menu for the user to choose an action for the character: fight, guard, or run.
   * 
   * @param user The party member whose turn it is.
   * @param battle The current battle.
   * @return The number corresponding to the chosen action.
   */
  public static int getCurrentAction(PartyMember user, Battle battle){
    int menuOption;
    String battleStatus=battle.getBattleStatus();
    
    menuOption = getMenuOption(battleStatus+"\n\nCurrent Turn: "+user.getName()+"\nChoose an action:", new String[]{"Fight", "Guard", "Run"});
    
    return menuOption;
  }
  
  /** 
   * Displays a list of the party member's skills and returns the chosen skill.
   * 
   * @param user The party member whose turn it is.
   * @return The chosen skill to use. Returns null if the user wants to go back to the previous menu.
   */
  
  public static Skill getSkillAction(PartyMember user){
    Skill act;
    String status;
    int menuOption;
    ArrayList<Skill> skillList = user.getSkills();
    String[] options = new String[skillList.size()+1];
    
    for(int i=0; i< skillList.size();i++){
      options[i] = skillList.get(i).getName();
    }
    
    options[skillList.size()] = "Back";
    
    
    while(true){
      status = user.getStatusInformation();
      menuOption = getMenuOption(status+"\nChoose a skill:", options);
      if(menuOption<skillList.size()){
        if(skillList.get(menuOption).skillUsesMP()){ 
          if(user.getStat(Stats.MP)>=skillList.get(menuOption).getRawCost()){
            act = skillList.get(menuOption);
            break;
          }else{
            displayMessage("Not enough MP!");
          }
        } else{
          if(user.getStat(Stats.HP)>skillList.get(menuOption).getRawCost()){
            act = skillList.get(menuOption);
            break;
          }else{
            displayMessage("Not enough HP!");
          }
        }
        
      }else{
        act = null;
        break;
      }
    }
    
    return act;
    
  }
  
  
  /** 
   * Displays the party member's status information and allows the user to use healing skills.
   * 
   * @param subject The party member to view the status of.
   * @param party The player's party.
   */
  public static void displayStatus(PartyMember subject, Party party){
    String status;
    int menuOption;
    int target;
    ArrayList<Skill> menuSkills = subject.getMenuSkills();
    String[] options = new String[menuSkills.size()+1];
    
    for(int i=0; i< menuSkills.size();i++){
      options[i] = menuSkills.get(i).getName();
    }
    
    options[menuSkills.size()] = "Back";
    
    while(true){
      status = subject.getStatusInformation();
      status += "\nChoose a skill:";
      menuOption = getMenuOption(status, options);
      if(menuOption<menuSkills.size()){
        if(subject.getStat(Stats.MP)>=menuSkills.get(menuOption).getRawCost()){
          target = chooseTarget(subject, menuSkills.get(menuOption), party);
          if(target>=0){
            useAction(subject, menuSkills.get(menuOption), party, target);
          }
        }else{
          displayMessage("Not enough MP!");
        }
      }else{
        break;
      }
    }
  }
  
  
  
  /** 
   * Displays a list of all items in the player's inventory and allows the player to use the items.
   * 
   * @param subject The party member to view the status of.
   * @param party The player's party.
   */
  public static void displayItemMenu(Party party){
    String itemList;
    int menuOption;
    int target;
    ArrayList<Item> bagItems = party.getBagItems();
    String[] options = new String[bagItems.size()+1];
    
    for(int i=0; i< bagItems.size();i++){
      options[i] = bagItems.get(i).getName();
    }
    
    options[bagItems.size()] = "Back";
    
    while(true){
      itemList = "Inventory: \n";
      for(int i=0; i< bagItems.size();i++){
        itemList+=bagItems.get(i).getName()+": "+bagItems.get(i).getDescription()+" (Have: "+bagItems.get(i).getQuantity()+")\n";
      }
      
      menuOption = getMenuOption(itemList+"\nChoose an item:", options);
      if(menuOption<bagItems.size()){
        if(bagItems.get(menuOption).getQuantity()>0){
          target = chooseTarget((PartyMember)(party.getMember(0)), bagItems.get(menuOption), party);
          if(target>=0){
            useAction((PartyMember)(party.getMember(0)), bagItems.get(menuOption), party, target);
          }
          
        }else{
          displayMessage("You don't have any more of that item!");
        }
      }else{
        break;
      }
    }
  }
  
  
  /** 
   * Gets the team being targeted by the player's action.
   * 
   * @param action The action the player is using.
   * @param battle The battle currently being resolved.
   */
  public static Team getTargetTeam(Action action, Battle battle){
    if(action.getTarget()==TargetType.ALL_ALLIES || action.getTarget()==TargetType.ONE_ALLY){
      return battle.getParty();
    } else{
      return battle.getTroop();
    } 
  }
  
  /** 
   * Gets the team being targeted by the enemy's action.
   * 
   * @param action The action the enemy is using.
   * @param battle The battle currently being resolved.
   */
  public static Team getEnemyTargetTeam(Action action, Battle battle){
    if(action.getTarget()==TargetType.ALL_ALLIES || action.getTarget()==TargetType.ONE_ALLY){
      return battle.getTroop();
    } else{
      return battle.getParty();
    } 
  }
  
  
  /** 
   * Displays a list of targets and has the player choose a target.
   * If the skill targets all allies/enemies, the player only has one target choice: "All Targets".
   * 
   * @param user The partymember currently acting.
   * @param action The action the player is using.
   * @param targetTeam The team being targeted.
   * @return The index of the chosen target. Returns -1 if the user wishes to go back.
   */
  public static int chooseTarget(PartyMember user, Action action, Team targetTeam){
    String output;
    String[] options = new String[targetTeam.getLength()+1];
    int target;
    BattleMember player;
    int menuOption;
    boolean allTargets = (action.getTarget()==TargetType.ALL_ALLIES || action.getTarget()==TargetType.ALL_ENEMIES);
    
    options[options.length-1] = "Back";
    
    while(true){
      output = "";
      if(action instanceof Skill){
        output+=user.getName()+" uses "+action.getName()+": "+action.getDescription()+"\nCost: "+((Skill)(action)).getCostString(user);
      }else{
        output+="Using "+action.getName()+": "+action.getDescription()+" (Have: "+((Item)(action)).getQuantity()+")";
      }
      output+="\nChoose a target:\n";
      
      for(int i=0; i<targetTeam.getAllMembers().length;i++){
        player = (targetTeam.getMember(i));
        output+=player.getMenuInformation()+"\n";
        options[i] = player.getName();
      }
      
      
      menuOption = allTargets ? getMenuOption(output, new String[]{"All Targets", "Back"}) : getMenuOption(output, options);
      if(menuOption==options.length-1 || (allTargets && menuOption==1)){
        target = -1;
      }else{
        target = menuOption;
      }
      break;
    }
    
    return target;
  }
  
  /** 
   * Deducts the cost of the action and executes the effects to the specified target(s).
   * 
   * @param user The partymember currently acting.
   * @param action The action the player is using.
   * @param group The team being targeted.
   * @param targetID The index number of the target.
   */
  public static void useAction(BattleMember user, Action action, Team group, int targetID){
    String output = "";
    
    if(action instanceof Skill){
      if(((Skill)(action)).skillUsesMP()){
        user.changeStat(Stats.MP,-((Skill)(action)).getCost(user));
      } else{
        user.changeStat(Stats.HP,-((Skill)(action)).getCost(user));
      }
    } else {
      ((Item)action).consumeItem();
    }
    
    if(action.getTarget()==TargetType.ONE_ALLY || action.getTarget()==TargetType.ONE_ENEMY){
      output+= executeEffects(user, action, group.getMember(targetID));
    }else {
      for(int i=0; i<group.getLength();i++){
        output+= executeEffects(user, action, group.getMember(i));
      }
    }
    
    displayMessage(output);
  }
  
  
  /** 
   * Applies the effects of the action used.
   * 
   * @param user The partymember currently acting.
   * @param action The action the player is using.
   * @param target The recipient of the action.
   */
  public static String executeEffects(BattleMember user, Action action, BattleMember target){
    int value=0;
    Skill actSkill;
    Item actItem;
    String output="";
    double affinity;
    
    if(action instanceof Skill){
      actSkill = ((Skill)action);
      if(actSkill.getEffect() == SkillEffect.REVIVE){
        if(target.getKO()){
          value = (int)(Math.round((target.getStat(Stats.MP)/100.0)* actSkill.getPower()));
          target.changeStat(Stats.HP, value);
          target.revive();
          output+=target.getName()+" recovered from KO!\n";  
        }else{
          output+=target.getName()+" isn't knocked out!\n";    
        } 
      }else if (!target.getKO()){
        if(actSkill.getEffect() == SkillEffect.HEAL){
          value = (int)(Math.round((target.getStat(Stats.MHP)/100.0)* actSkill.getPower()));
          target.changeStat(Stats.HP, value);
          output+=target.getName()+" recovered "+value+" HP!\n";     
        }else if(actSkill.getEffect() == SkillEffect.MPUP){
          value = user.getStat(Stats.MMP)/4;
          target.changeStat(Stats.MP, value);
          output+=target.getName()+" recovered "+value+" MP!\n";     
        }else if(actSkill.getPower() != 0 &&(actSkill.getElement()==Elements.PHYS)){
          if(actSkill.getEffect() == SkillEffect.SACRIFICE){
            value = user.getStat(Stats.HP)/2;
          }else{
            value = user.getStat(Stats.STR)* 2-target.getStat(Stats.VIT)/2-target.getStat(Stats.DEX)/2;
          }
          output += damageTarget(user, target, actSkill, value);
        }else if(actSkill.getElement()==Elements.BLOOD){
          value = user.getStat(Stats.VIT)* 2-target.getStat(Stats.VIT)/2-target.getStat(Stats.DEX)/2;
          output += damageTarget(user, target, actSkill, value);
        }else if(actSkill.getElement()==Elements.GRAV){
            value = (int)(Math.random()* 100)+1+Math.min(user.getStat(Stats.STR)/4,20) <= (int)actSkill.getPower() ? 999999999 : 0;
          output += damageTarget(user, target, actSkill, value);
        }else  if(actSkill.getPower() != 0) {
          value = user.getStat(Stats.MAG)* 2-target.getStat(Stats.VIT)/2-target.getStat(Stats.DEX)/2;
          output += damageTarget(user, target, actSkill, value);
        }
        
        if(actSkill.getEffect() == SkillEffect.ATKUP){
          target.changeAtkMod(0.25);
          output+=target.getName()+"'s attack is up!\n";   
        } else  if(actSkill.getEffect() == SkillEffect.ATKUP2){
          target.changeAtkMod(0.5);
          output+=target.getName()+"'s attack is way up!\n"; 
        } else if(actSkill.getEffect() == SkillEffect.ATKDOWN){
          target.changeAtkMod(-0.25);
          output+=target.getName()+"'s attack is down!\n"; 
        } else  if(actSkill.getEffect() == SkillEffect.ATKDOWN2){
          target.changeAtkMod(-0.5);
          output+=target.getName()+"'s attack is way down!\n"; 
        } else if(actSkill.getEffect() == SkillEffect.DEFUP){
          target.changeDefMod(0.25);
          output+=target.getName()+"'s defence is up!\n"; 
        } else  if(actSkill.getEffect() == SkillEffect.DEFUP2){
          target.changeDefMod(0.5);
          output+=target.getName()+"'s defence is way up!\n"; 
        } else if(actSkill.getEffect() == SkillEffect.DEFDOWN){
          target.changeDefMod(+0.25);
          output+=target.getName()+"'s defence is down!\n"; 
        } else  if(actSkill.getEffect() == SkillEffect.DEFDOWN2){
          target.changeDefMod(+0.5);
          output+=target.getName()+"'s defence is way down!\n"; 
        }else if (actSkill.getEffect() ==SkillEffect.FULLGUARD){
          target.setGuard(0);
          output+=user.getName()+" is protecting "+target.getName()+"!\n"; 
        }else if (actSkill.getEffect() ==SkillEffect.DRAINHP){
          user.changeStat(Stats.HP, value/2);
          output+=user.getName()+" recovered "+(value/2)+"HP!\n"; 
        }else if (actSkill.getEffect() ==SkillEffect.DRAINMP){
          user.changeStat(Stats.HP, value/10);
          output+=user.getName()+" recovered "+(value/10)+"MP!\n"; 
        }else if (actSkill.getEffect() ==SkillEffect.DISPEL){
          target.dispel();
          output+=user.getName()+"'s positive effects were dispelled!\n"; 
        }else if (actSkill.getEffect() ==SkillEffect.RESTORE){
          target.restore();
          output+=user.getName()+"'s negative effects were restored!\n"; 
        }
        
        if(target.getKO()){
          output+=target.getName()+" was knocked out!\n"; 
        }
      
        if(target instanceof Enemy){
          affinity =((Enemy)target).getElementalAffinity(actSkill.getElement());
          if(affinity>1){
            output+="Weak point hit!\n";
          } else if(affinity<1){
            output+="The attack was resisted!\n";
          }
        }
      }else{
        output+=target.getName()+" is knocked out!\n"; 
      }
    }
    else {
      actItem = ((Item)action);
      if(actItem.getEffect() == ItemEffect.HEAL){
        value = (int)(Math.round((target.getStat(Stats.MHP)/100.0)* actItem.getPower()));
        target.changeStat(Stats.HP, value);
        output+=target.getName()+" recovered "+value+" HP!\n";     
      } else if(actItem.getEffect() == ItemEffect.MPUP){
        value = (int)(Math.round((target.getStat(Stats.MMP)/100.0)* actItem.getPower()));
        target.changeStat(Stats.MP, value);
        output+=target.getName()+" recovered "+value+" MP!\n";     
      }else if(actItem.getEffect() == ItemEffect.STRUP){
        value = (int)actItem.getPower();
        target.changeStat(Stats.STR, value);
        output+=target.getName()+"'s STR increased by "+value+"!\n";     
      }else if(actItem.getEffect() == ItemEffect.MAGUP){
        value = (int)actItem.getPower();
        target.changeStat(Stats.MAG, value);
        output+=target.getName()+"'s MAG increased by "+value+"!\n";   
        output+=target.getName()+"'s Max MP increased by "+value* 3+"!\n";     
      }else if(actItem.getEffect() == ItemEffect.VITUP){
        value = (int)actItem.getPower();
        target.changeStat(Stats.VIT, value);
        output+=target.getName()+"'s VIT increased by "+value+"!\n";   
        output+=target.getName()+"'s Max HP increased by "+value* 5+"!\n";       
      }else if(actItem.getEffect() == ItemEffect.DEXUP){
        value = (int)actItem.getPower();
        target.changeStat(Stats.DEX, value);
        output+=target.getName()+"'s DEX increased by "+value+"!\n";     
      }else{
        target.fullRestore();
        output+=target.getName()+"'s HP and MP were fully restored!\n"; 
      }
    }
    return output;
  }
  
  /** 
   * Deals damage to the target and applies the necessary modifiers. Damage cannot be less than 1.
   * 
   * @param user The battle member using the action.
   * @param target The battle member taking damage.
   * @param act The action being used.
   * @param dmg The base damage being taken.
   */
  public static String damageTarget(BattleMember user, BattleMember target, Action act, int dmg){
    int fullDmg = dmg;
    fullDmg *= act.getPower(); 
    fullDmg *= user.getAtkMod()/target.getDefMod();
    fullDmg *= target.getGuard(); 
    
    if(target instanceof Enemy){
      fullDmg *= ((Enemy)target).getElementalAffinity(((Skill)act).getElement());
    }
    
    fullDmg = fullDmg<1 ? 1 : fullDmg;
    
    target.changeStat(Stats.HP, -fullDmg );
    return target.getName()+" took "+fullDmg +" damage!\n";    
  }
  
  
  /** 
   * Moves the player and returns the tile type the player is on to resolve events.
   * 
   * @param floorMap The current floor the player is on.
   * @param dir The direction to move the player.
   * @return The tile type the player is on.
   */
  public static Tile moveMapPos(Map floorMap, String dir){
    boolean success;
    Tile event;
    int menuOption;
    int itemFound;
    
    success = floorMap.movePlayer(dir);
    
    if(!success){
      event = Tile.WALL;
    } else {
      event = floorMap.getPlayerPosTile();
      if(event==Tile.EXIT){
        menuOption = getMenuOption("You found the stairs to the next floor!\nWill you go up?",new String[]{"Go up","Stay here"});
        if(menuOption!=0){
          event=Tile.FLOOR;
        }
      } else if(event==Tile.AV_BATTLE){
        displayMessage("There are some enemies here!");
      } else if(event==Tile.WK_BATTLE){
        displayMessage("There are weak enemies here!");
      } else if(event==Tile.ST_BATTLE){
        displayMessage("There are strong enemies here!");
      }
    }
    return event;
  }
  
  /** 
   * Displays a message to the screen to give the user information.
   * 
   * @param message The message to display.
   */
  public static void displayMessage(String message){
    JOptionPane.showMessageDialog(null, message, "Endless Tower", JOptionPane.INFORMATION_MESSAGE);
  }
  
  
  /** 
   * Displays a message to the screen and gives the user options to choose.
   * 
   * @param message The message to display.
   * @param options An array of Strings. Each String is an option.
   * @return The index of the option chosen.
   */
  public static int getMenuOption(String prompt, String[] options){
    return JOptionPane.showOptionDialog(null,prompt,"Choose an option", 
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, 
                                        null,
                                        options, 
                                        null);
  }
  
}