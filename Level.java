package ch.epfl.cs107.play.game.icrogue.area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import java.util.Random;


public abstract class Level {

    protected static ICRogueRoom[][] map;
    private DiscreteCoordinates destination;
    private final static DiscreteCoordinates bossPosition = new DiscreteCoordinates(0,0);
    private String roomName;
    private boolean isWon = false;
    private boolean randomMap;
    private DiscreteCoordinates startPosition;
    private int[] roomsDistribution;
    private int width;
    private int height;

    public Level(boolean randomMap, DiscreteCoordinates startPosition,int[] roomsDistribution, int width, int height) {
        this.randomMap = randomMap;
        this.startPosition = startPosition;
        this.roomsDistribution = roomsDistribution;
        this.width = width;
        this.height = height;
        if (!randomMap) {
            map = new ICRogueRoom[width][height];
            generateFixedMap();
        } else {
            generateRandomMap(roomsDistribution);
        }
    }

    public abstract void generateFixedMap();
    protected void generateRandomMap(int [] roomsDistribution){
        int nbRooms = 0;
        if (roomsDistribution !=null){
            for (int i = 0; i < roomsDistribution.length; ++i){
                nbRooms += roomsDistribution[i];
            }
        }
        int carteAleatoire = nbRooms * nbRooms;
    }
    protected enum MapState {
        NULL,
        PLACED,
        BOSS_ROOM,
        CREATED;
        @Override
        public String toString() {
            return Integer.toString(ordinal()); }
    }

    protected MapState[][] generateRandomRoomPlacement(MapState state){
        MapState[][] carteDesPlacements = new MapState[map.length][map[0].length];
        for(int i = 0; i<carteDesPlacements.length; ++i){
            for(int j = 0; j<carteDesPlacements[0].length; ++j){
                carteDesPlacements[i][j] = MapState.NULL;
            }
        }
        carteDesPlacements[carteDesPlacements.length/2][carteDesPlacements[0].length/2] = MapState.PLACED;
        int roomsToPlace = carteDesPlacements.length*carteDesPlacements[0].length-1;
        while(roomsToPlace>0){
            int freeSlots = 0;
            if(carteDesPlacements[0][0] == MapState.NULL){
                freeSlots +=1;
            }else if(carteDesPlacements[carteDesPlacements.length-1][0] == MapState.NULL){
                freeSlots +=1;
            }else if(carteDesPlacements[0][carteDesPlacements[0].length-1] == MapState.NULL){
                freeSlots +=1;
            }else if(carteDesPlacements[carteDesPlacements.length-1][carteDesPlacements[0].length-1] == MapState.NULL){
                freeSlots +=1;
            }
        }
        Random random = new Random();
        int nombreSalles = random.nextInt(carteDesPlacements.length*carteDesPlacements[0].length);
        
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
