package ch.epfl.cs107.play.game.icrogue.handler;

import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.MoveableTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;

public interface ICRogueInteractionHandler extends AreaInteractionVisitor {

    /**
     * Method that interacts with the game cell.
     * @param cell
     * @param isCellInteraction
     */

    default void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction){}

    /**
     * Method that interacts with the main character.
     * @param player
     * @param isCellInteraction
     */
    default void interactWith(ICRoguePlayer player, boolean isCellInteraction){}

    /**
     * Method that interacts with a cherry.
     * @param c
     * @param isCellInteraction
     */
    default void interactWith(Cherry c, boolean isCellInteraction){}

    /**
     * Method that interacts with a stick.
     * @param s
     * @param isCellInteraction
     */
    default void interactWith(Staff s, boolean isCellInteraction){}

    /**
     * Method that interacts with a fireball.
     * @param fire
     * @param isCellInteraction
     */
    default void interactWith(Fire fire, boolean isCellInteraction){}

    /**
     * Method that interacts with a key.
     * @param key
     * @param isCellInteraction
     */
    default void interactWith(Key key, boolean isCellInteraction){}

    /**
     * Method that interacts with the connector.
     * @param connector
     * @param isCellInteraction
     */
    default void interactWith(Connector connector, boolean isCellInteraction){}

    /**
     * Method that interacts with a turret.
     * @param turret
     * @param isCellInteraction
     */
    default void interactWith(Turret turret, boolean isCellInteraction){}

    /**
     * Method that interacts with an arrow.
     * @param arrow
     * @param isCellInteraction
     */
    default void interactWith(Arrow arrow, boolean isCellInteraction){}

    /**
     * Method that interacts with a moveable turret.
     * @param moveableTurret
     * @param isCellInteraction
     */
    default void interactWith(MoveableTurret moveableTurret, boolean isCellInteraction){}




}
