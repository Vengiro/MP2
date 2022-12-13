package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {

    protected ICRogueRoom[][] map;
    private DiscreteCoordinates destination;
    private final static DiscreteCoordinates bossPosition = new DiscreteCoordinates(0,0);
    private String roomName;
    private boolean isWon = false;

    public Level(){
    }
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).setDestination(destination);
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).closeState();
        map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).setDestination(destination);

    }

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        /*Area connectorArea = map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).getConnectorArea();
        Orientation connectorOrientation = map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).getOrientation();
        List<DiscreteCoordinates> connectorPosition = map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).getCurrentCells();*/

        map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).lockState();
        map[coords.x][coords.y].setOfConnector.get(connector.getIndex()).setKeyID(keyId);
    }

    /*protected abstract void setRoomName(DiscreteCoordinates coords);*/
    public String getTitleStartingRoom(DiscreteCoordinates startingRoom){return map[startingRoom.x][startingRoom.y].getTitle();}

    public void isWon(){
        if(map[bossPosition.x][bossPosition.y].isOn() & !(isWon)){
            System.out.println("Win");
            isWon = true;
        }
    }
}
