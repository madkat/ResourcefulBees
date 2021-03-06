package com.resourcefulbees.resourcefulbees.api;

import com.resourcefulbees.resourcefulbees.api.beedata.CustomBeeData;
import net.minecraft.entity.AgeableEntity;

/**
 * Implemented in CustomBeeEntity and ResourcefulBee
 *
 * It is recommended to use ResourcefulBee (or a class that is child of it) when referred to ICustomBee
 */
public interface ICustomBee {

    /**
     * Gets "this" bee's type.
     *
     *  @return "This" bee's type.
     */
    String getBeeType();

    /**
     * Gets "this" bee's information card from the BEE_INFO hashmap.
     *
     *  @return "This" bee's info card.
     */
    CustomBeeData getBeeData();

    /**
     * Gets "this" bee's current number of times fed.
     *
     * @return "This" bee's feed count.
     */
    int getFeedCount();

    /**
     * Resets "this" bee's current number of times fed.
     */
    void resetFeedCount();

    /**
     * Increments "this" bee's current number of times fed by 1.
     */
    void addFeedCount();

    AgeableEntity createSelectedChild(CustomBeeData beeType);
}
