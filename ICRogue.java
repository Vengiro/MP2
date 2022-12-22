package ch.epfl.cs107.play.game.icrogue;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ICRogue extends AreaGame {
    public final static float CAMERA_SCALE_FACTOR = 10.f;
    private ICRoguePlayer player;
    private Level0 Level;

    private  Keyboard keyboard;


    /**
     * Add all the areas
     */

    private void initLevel(){
        Level = new Level0(true);
        Level.initArea(this);
        String  startingRoomTitle = Level.getTitleStartingRoom(Level0.getStartingRoomPosition());
        ICRogueRoom startingRoom = (ICRogueRoom) setCurrentArea(startingRoomTitle, true);
        DiscreteCoordinates coords = startingRoom.getPlayerSpawnPosition();
        player = new ICRoguePlayer(startingRoom, Orientation.UP, coords);
        player.enterArea(startingRoom, coords);

    }

    /**
     * Method that initializes the window & the keyboard
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return boolean
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {

        if (super.begin(window, fileSystem)) {
            initLevel();
            keyboard = this.getCurrentArea().getKeyboard();
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {

        /*ICRogueRoom area = (ICRogueRoom)setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new Player(area, Orientation.DOWN, coords,"zelda/player");
        player.enterArea(area, coords);*/

    }

    /**
     * Evolving method.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        /*if(player.isWeak()){
            switchArea();
        }*/
        super.update(deltaTime);
        reset(keyboard.get(Keyboard.R));
        switchRoom();
        Level.isWon();
    }

    @Override
    public void end() {
    }

    /**
     * Method that will return the name "ICRogue"
     * @return String
     */
    @Override
    public String getTitle() {
        return "ICRogue";
    }

    /** Reset the objects on the area (Background, items etc.) */
    private void reset(Button b){
        if (b.isDown()){
            initLevel();
        }
    }

    private void switchRoom() {
        if(player.getWillOfTransition()){
            player.leaveArea();
            ICRogueRoom currentArea = (ICRogueRoom) setCurrentArea(player.getDestination(), false);
            player.enterArea(currentArea, player.getSpawnPosition());
        }
    }

}
