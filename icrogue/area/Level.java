package ch.epfl.cs107.play.game.icrogue.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.level0.Level0;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Level {

    protected ICRogueRoom[][] map;
    private DiscreteCoordinates destination;
    private DiscreteCoordinates bossPosition = new DiscreteCoordinates(0,0);
    private String roomName;
    private boolean isWon = false;

    private boolean randomMap;
    private DiscreteCoordinates startPosition;
    private int[] roomsDistribution;
    private int width;
    private int height;

    private final Random random = new Random();

    public Level(boolean randomMap, DiscreteCoordinates startPosition,int[] roomsDistribution, int width, int height) {
        this.randomMap = randomMap;
        this.startPosition = startPosition;
        this.roomsDistribution = roomsDistribution;
        this.width = width;
        this.height = height;
        /*if (!randomMap) {
            map = new ICRogueRoom[width][height];
            generateFixedMap();
        } else {
            generateRandomMap(roomsDistribution);
        }*/
    }
    public Level(boolean randomMap, DiscreteCoordinates startPosition, int width, int height) {
        this.randomMap = randomMap;
        this.startPosition = startPosition;
        this.width = width;
        this.height = height;
        /*if (!randomMap) {
            map = new ICRogueRoom[width][height];
            generateFixedMap();
        } else {
            generateRandomMap(roomsDistribution);
        }*/
    }

    protected void setBossPosition(DiscreteCoordinates coords){bossPosition = coords;}
    protected DiscreteCoordinates getBossPosition(){return bossPosition;}
    protected void setRoomsDistribution(int[] roomsDistribution){this.roomsDistribution  = roomsDistribution;}
    protected void setRoom(DiscreteCoordinates coords, ICRogueRoom room){
        map[coords.x][coords.y] = room;
    }

    protected void setRoomConnectorDestination(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].getSetOfConnectors().get(connector.getIndex()).setDestination(destination);
    }

    protected void setRoomConnector(DiscreteCoordinates coords, String destination, ConnectorInRoom connector){
        map[coords.x][coords.y].getSetOfConnectors().get(connector.getIndex()).closeState();
        map[coords.x][coords.y].getSetOfConnectors().get(connector.getIndex()).setDestination(destination);

    }

    protected void lockRoomConnector(DiscreteCoordinates coords, ConnectorInRoom connector, int keyId){
        map[coords.x][coords.y].getSetOfConnectors().get(connector.getIndex()).lockState();
        map[coords.x][coords.y].getSetOfConnectors().get(connector.getIndex()).setKeyID(keyId);
    }

    protected abstract void setRoomName(DiscreteCoordinates coords);
    public String getTitleStartingRoom(DiscreteCoordinates startingRoom){return map[startingRoom.x][startingRoom.y].getTitle();}

    public abstract void generateFixedMap();

    protected void generateRandomMap(int [] roomsDistribution){
        int totalRooms = 0;
        for (int numberOfRoom : roomsDistribution) {
            totalRooms += numberOfRoom;
        }
        map = new ICRogueRoom[totalRooms][totalRooms];
    }

    protected enum MapState {
        NULL,
        PLACED,
        EXPLORED,
        BOSS_ROOM,
        CREATED;
        @Override
        public String toString() {
            return Integer.toString(ordinal());}
    }

    private int numberOfFreeSlots(MapState[][] carteDesPlacements ,int xPositionOfRoom, int yPositionOfRoom, List<Integer> nearRoom) {
        int freeSlots = 0;

        if (xPositionOfRoom < map.length-1 && carteDesPlacements[yPositionOfRoom][xPositionOfRoom + 1] == MapState.NULL) {
            //Right
            freeSlots += 1;
            nearRoom.add(Orientation.RIGHT.ordinal());
        }
        if (0 < yPositionOfRoom  && carteDesPlacements[yPositionOfRoom - 1][xPositionOfRoom] == MapState.NULL) {
            //Up
            freeSlots += 1;
            nearRoom.add(Orientation.UP.ordinal());
        }
        if (0 < xPositionOfRoom && carteDesPlacements[yPositionOfRoom][xPositionOfRoom - 1] == MapState.NULL) {
            //Left
            freeSlots += 1;
            nearRoom.add(Orientation.LEFT.ordinal());
        }
        if (yPositionOfRoom < map.length-1 && carteDesPlacements[yPositionOfRoom +1][xPositionOfRoom] == MapState.NULL) {
            //Down
            freeSlots += 1;
            nearRoom.add(Orientation.DOWN.ordinal());
        }
        return freeSlots;
    }

    private void changeRoomState(MapState[][] mapStates, int xPos, int yPos, MapState state){
        mapStates[yPos][xPos] = state;
    }

    private List<Integer> randomlyPlace(MapState[][] carteDesPlacements ,int xPositionOfRoom, int yPositionOfRoom,
                               List<Integer> nearRoom, int roomsToPlace){

        int freeSlots = numberOfFreeSlots(carteDesPlacements, xPositionOfRoom, yPositionOfRoom, nearRoom);
        int randomNumberToPlace;
        if(freeSlots <= 1 || roomsToPlace <= 1){randomNumberToPlace = 1;} else {
            randomNumberToPlace = random.nextInt(1, Math.min(roomsToPlace, freeSlots));
        }
        List<Integer> newRoomToPlace = RandomHelper.chooseKInList(randomNumberToPlace, nearRoom);
        for (int placeOnMap : newRoomToPlace) {
            switch (placeOnMap) {
                //Up
                case 0 -> changeRoomState(carteDesPlacements, xPositionOfRoom, yPositionOfRoom - 1, MapState.PLACED);

                //Right
                case 1 -> changeRoomState(carteDesPlacements, xPositionOfRoom + 1, yPositionOfRoom, MapState.PLACED);

                //Down
                case 2 -> changeRoomState(carteDesPlacements, xPositionOfRoom, yPositionOfRoom + 1, MapState.PLACED);

                //Left
                case 3 -> changeRoomState(carteDesPlacements, xPositionOfRoom - 1, yPositionOfRoom, MapState.PLACED);
            }
        }
        return newRoomToPlace;
    }


    private void placeBossRoom(MapState[][] carteDesPlacements, int xPositionOfRoom, int yPositionOfRoom, List<Integer> nearRoom){
        int freeSlotsForBossRoom = numberOfFreeSlots(carteDesPlacements, xPositionOfRoom, yPositionOfRoom, nearRoom);
        int randomPlaceOfBoss;
        if (freeSlotsForBossRoom <= 1){randomPlaceOfBoss = nearRoom.get(0);}else {
            randomPlaceOfBoss = nearRoom.get(random.nextInt(1, freeSlotsForBossRoom));
        }
        switch (randomPlaceOfBoss) {
            //Up
            case 0 -> changeRoomState(carteDesPlacements, xPositionOfRoom, yPositionOfRoom - 1, MapState.BOSS_ROOM);

            //Right
            case 1 -> changeRoomState(carteDesPlacements, xPositionOfRoom + 1, yPositionOfRoom, MapState.BOSS_ROOM);

            //Down
            case 2 -> changeRoomState(carteDesPlacements, xPositionOfRoom, yPositionOfRoom + 1, MapState.BOSS_ROOM);

            //Left
            case 3 -> changeRoomState(carteDesPlacements, xPositionOfRoom - 1, yPositionOfRoom, MapState.BOSS_ROOM);
        }
    }



    protected MapState[][] generateRandomRoomPlacement(int size){
        MapState[][] carteDesPlacements = new MapState[size][size];
        List<Integer> nearRoom = new ArrayList<>();
        int numberOfRoom = size;
        for(int i = 0; i<numberOfRoom; ++i){
            for(int j = 0; j<numberOfRoom; ++j){
                carteDesPlacements[i][j] = MapState.NULL;
            }
        }
        changeRoomState(carteDesPlacements, numberOfRoom/2, numberOfRoom/2, MapState.PLACED);
        int xPositionOfRoom = numberOfRoom/2;
        int yPositionOfRoom = numberOfRoom/2;
        int roomsToPlace = numberOfRoom - 1;
        List<Integer> newRoomToPlace;
        while (roomsToPlace > 0) {
            newRoomToPlace = randomlyPlace(carteDesPlacements, xPositionOfRoom, yPositionOfRoom, nearRoom, roomsToPlace);
            changeRoomState(carteDesPlacements, xPositionOfRoom, yPositionOfRoom, MapState.EXPLORED);
            printMap(carteDesPlacements);
            int randomPathToFollow;
            if (newRoomToPlace.size()-1 <= 0){randomPathToFollow = newRoomToPlace.get(0);}else {
                randomPathToFollow = newRoomToPlace.get(random.nextInt(0, newRoomToPlace.size()-1));
            }
            switch (randomPathToFollow) {
                //Up
                case 0 -> yPositionOfRoom -= 1;

                //Right
                case 1 -> xPositionOfRoom += 1;

                //Down
                case 2 -> yPositionOfRoom += 1;

                //Left
                case 3 -> xPositionOfRoom -= 1;
            }

            nearRoom = new ArrayList<>();
            roomsToPlace = roomsToPlace - newRoomToPlace.size();
        }

        placeBossRoom(carteDesPlacements, xPositionOfRoom, yPositionOfRoom, nearRoom);
        /*for(int i = 0; i<numberOfRoom; ++i) {
            for (int j = 0; j < numberOfRoom; ++j) {
                if (carteDesPlacements[i][j] == MapState.EXPLORED && numberOfFreeSlots(carteDesPlacements, i, j, numberOfRoom, nearRoom) != 0) {
                    randomlyPlace(carteDesPlacements, xPositionOfRoom, yPositionOfRoom)
                }
            }
        }    */
        printMap(carteDesPlacements);
        return carteDesPlacements;
    }

    private void printMap(MapState[][] map) {
        System.out.println("Generated map:");
        System.out.print(" | ");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print(j + " "); }
        System.out.println(); System.out.print("--|-");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print("--"); }
        System.out.println();
        for (int i = 0; i < map.length; i++) { System.out.print(i + " | ");
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " "); }
            System.out.println(); }
        System.out.println(); }




    public void isWon(){
        if(map[bossPosition.x][bossPosition.y].isOn() & !(isWon)){
            System.out.println("Win");
            isWon = true;
        }
    }
}
