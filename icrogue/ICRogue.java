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
import ch.epfl.cs107.play.game.tutosSolution.actor.Player;
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
    public final static float CAMERA_SCALE_FACTOR = 13.f;

    private ICRoguePlayer player;
    private Level0 Level;

    private  Keyboard keyboard;


    /**
     * Add all the areas
     */


    private void initLevel(){
        Level = new Level0();
        Level.initArea(this);
        String  startingRoomTitle = Level.getTitleStartingRoom(Level0.getStartingRoomPosition());
        ICRogueRoom startingRoom = (ICRogueRoom) setCurrentArea(startingRoomTitle, true);
        DiscreteCoordinates coords = startingRoom.getPlayerSpawnPosition();
        player = new ICRoguePlayer(startingRoom, Orientation.UP, coords);
        player.enterArea(startingRoom, coords);

    }

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
            ICRogueRoom currentArea = (ICRogueRoom) setCurrentArea(player.getDestination(), true);
            player.enterArea(currentArea, player.getSpawnPosition());
        }
    }

}
