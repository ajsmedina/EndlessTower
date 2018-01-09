
/** 
 * Team
 * 
 * A team is a collection of battle members.
 */

public abstract class Team{
  /** 
   * The members that make up the team.
   */
  private BattleMember[] members;
  
  /** 
   * Sets the members of the team.
   * 
   * @param mems The members of the team.
   */
  public void setMembers(BattleMember[] mems){
    members = mems;
  }
  
  /** 
   * Gets the team member at the specified index.
   * 
   * @param memberID The index of the member to get.
   * @return The member at the index.
   */
  public BattleMember getMember(int memberID){
    return members[memberID];
  }
  
  /** 
   * Gets an array of all team members.
   * 
   * @return An array of all team members.
   */
  
  public BattleMember[] getAllMembers(){
    return members;
  }
  
  /** 
   * Gets the size of the team.
   * 
   * @return The size of the team.
   */
  
  public int getLength(){
    return members.length;
  }
  
  /** 
   * Checks if all team members are KOd.
   * 
   * @return True if KOd, False if at least one member is not KOd.
   */
  public boolean allMembersKO(){
    boolean allKO = true;
    for(int i=0; i<members.length;i++){
      if(!members[i].getKO()){
        allKO = false;
        break;
      }
    }
    
    return allKO;
  }
  
}