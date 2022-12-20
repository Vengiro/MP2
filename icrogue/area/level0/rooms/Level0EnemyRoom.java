package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level0EnemyRoom extends Level0Room implements Logic {
    private Turret[] enemiesList;
    public Level0EnemyRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
    }
    protected void setEnemiesList(Turret[] enemiesList) {
        this.enemiesList = enemiesList;
    }
    @Override
    public void createArea(){
        super.createArea();
        for(Turret enemy : enemiesList){
            this.registerActor(enemy);
        }
    }

    @Override
    public boolean isOn() {
        for (Turret enemy : enemiesList){
            if (!(enemy.isDead())){
                return false;
            }
        }
        return true;
    }
}
