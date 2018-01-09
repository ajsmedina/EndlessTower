
/** 
 * Map
 * 
 * A Map is a single floor in the tower.
 */

public class Map {
  /** 
   * True if the floor contains a starting tile.
   */
  private static boolean startExists;
  
  /** 
   * A map containing all tiles of the floor.
   */
  private static Tile[][] map= new Tile[7][7];
  
  /** 
   * The player's coordinates on the floor.
   */
  private static int[] playerPosition = new int[2];
  
  /** 
   * Constructor for Map
   */
  public Map() {
    int[] coords=  new int[2];
    resetMap();
    coords[0] = (int)(Math.random()* map.length);
    coords[1] = (int)(Math.random()* map.length);
    
    startExists=false;
    
    do{
      resetMap();
      setTile(coords[0],coords[1], Tile.EXIT);
      generateMap(coords[0], coords[1]);
    }while(!startExists);
    coords = findStartPosition();
    setPlayerPosition(coords[0], coords[1]);
  }
  
  
  /** 
   * Gets the current position of the player.
   * 
   * @return The player's position as an array. [0]=x, [1]=y.
   */
  public int[] getPlayerPosition(){
    return playerPosition;
  }
  
  
  /** 
   * Gets the tile the player is currently standing on.
   * 
   * @return The tile type the player is standing on.
   */
  public Tile getPlayerPosTile(){
    return map[playerPosition[0]][playerPosition[1]];
  }
  
  /** 
   * Sets the tile the player is currently standing on.
   * 
   * @param x The x coordinate of the tile.
   * @param y The y coordinate of the tile.
   */
  public void setPlayerPosition(int x, int y){
    playerPosition[0]=x;
    playerPosition[1]=y;
  }
  
  
  /** 
   * Moves the player in the direction specified.
   * 
   * @param dir The direction to move.
   * @return True if the player moved, False if the direction was blocked.
   */
  public boolean movePlayer(String dir){
    int[] pos = getPlayerPosition();
    boolean moveSuccessful = false;
    
    if(dir=="up" && pos[1]>0 && map[pos[0]][pos[1]-1] != Tile.WALL){
      setPlayerPosition(pos[0],pos[1]-1);
      moveSuccessful = true;
    } else if(dir=="down" && pos[1]<map.length-1 && map[pos[0]][pos[1]+1] != Tile.WALL){
      setPlayerPosition(pos[0],pos[1]+1);
      moveSuccessful = true;
    } else if(dir=="left" && pos[0]>0 && map[pos[0]-1][pos[1]] != Tile.WALL){
      setPlayerPosition(pos[0]-1,pos[1]);
      moveSuccessful = true;
    } else if(dir=="right" && pos[0]<map.length-1 && map[pos[0]+1][pos[1]] != Tile.WALL){
      setPlayerPosition(pos[0]+1,pos[1]);
      moveSuccessful = true;
    }
    return   moveSuccessful; 
  }
  
  /** 
   * Returns a string containing the map and all tiles on it.
   * 
   * @return The map of the floor.
   */
  public String toString(){
    String output = "\n";
    
    for(int i=0; i<map.length; i++){
      for(int j=0; j<map.length; j++){
        if(j==getPlayerPosition()[0] && i==getPlayerPosition()[1]){
          output+="[O] ";
        }else{
          output+="["+getTileDisplay(map[j][i])+"] ";
        }
      }
      output+="\n";
    }
    
    return output;
  }
  
  /** 
   * Resets all tiles on the map to unknown.
   */
  public static void resetMap(){
    for(int i=0; i<map.length; i++){
      for(int j=0; j<map.length; j++){
        setTile(j,i,Tile.UNKNOWN);
      }
    }
  }
  
  /** 
   * Sets the type of tile at a specific location.
   * 
   * @param x The x coordinate of the tile to change.
   * @param y The y coordinate of the tile to change.
   * @param tileType The type of tile to set at the location.
   */
  private static void setTile(int x, int y, Tile tileType){
    map[x][y]=tileType;
  }
  
  /** 
   * Gets the display icon of the tile.
   * 
   * @param tileType The type of tile to get the icon of.
   * @return The tile's icon.
   */
  public static String getTileDisplay(Tile tileType){
    if(tileType==Tile.FLOOR){
      return "_";
    } else if(tileType==Tile.WALL){
      return "X";
    } else if(tileType==Tile.EXIT){
      return "S";
    } else if(tileType==Tile.AV_BATTLE || tileType==Tile.WK_BATTLE || tileType==Tile.ST_BATTLE){
      return "E";
    } else if(tileType==Tile.START){
      return "_";
    } else if(tileType==Tile.CHEST){
      return "T";
    } else{
      return "?";
    }
  }
  
  /** 
   * Generates the tile at the current location and, if the tile is not a wall, recursively calls itself for all
   * adjacent tiles that do not have a tile type. 
   * 
   * @param tilex The x coordinate of the tile to generate.
   * @param tiley The y coordinate of the tile to generate.
   */
  public static void generateMap(int tilex, int tiley){ 
    if (map[tilex][tiley]==Tile.UNKNOWN){
      setTile(tilex, tiley, generateTile());
    }
    
    if(map[tilex][tiley]!=Tile.WALL){
      if(tilex+1<map.length && map[tilex+1][tiley]==Tile.UNKNOWN){
        generateMap(tilex+1,tiley);
      }
      if(tiley+1<map.length&& map[tilex][tiley+1]==Tile.UNKNOWN){
        generateMap(tilex,tiley+1);
      }
      if(tilex-1>=0&& map[tilex-1][tiley]==Tile.UNKNOWN){
        generateMap(tilex-1,tiley);
      }
      if(tiley-1>=0&& map[tilex][tiley-1]==Tile.UNKNOWN){
        generateMap(tilex,tiley-1);
      }
    }
  }
  
  /** 
   * Randomly gets a tile type. If there is already an entrance on the map, calls itself to get a different type.
   * 
   * @return A random tile type.
   */
  public static Tile generateTile(){
    int rand = (int)(Math.random()* 80); 
    
    if(rand<=35){
      return Tile.WALL;
    } else if(rand<=70){
      return Tile.FLOOR;
    } else if (rand<=74){
      return Tile.AV_BATTLE;
    } else if (rand<=75){
      return Tile.WK_BATTLE;
    } else if (rand<=76){
      return Tile.ST_BATTLE;
    } else if (rand<=78){
      return Tile.CHEST;
    }else{
      if(!startExists){
        startExists=true;
        return Tile.START;
      } else{
        return generateTile();
      }
    }
  }
  
  /** 
   * Resets the tile the player is on to floor.
   * Used to delete battle/treasure events after they have been resolved.
   */
  public static void resetPlayerTile(){
    setTile(playerPosition[0],playerPosition[1],Tile.FLOOR);
    
  }
  
  /** 
   * Linearly searches the map to find and get the coordinates of the start position.
   * 
   * @return The coordinates of the start position.
   */
  public static int[] findStartPosition(){
    int[] coords = new int[2];
    
    for(int i=0; i<map.length; i++){
      for(int j=0; j<map.length; j++){
        if(map[j][i]==Tile.START){
          coords[0] = j;
          coords[1] = i;
          break;
        }
      }
    }
    
    return coords;
  }
}