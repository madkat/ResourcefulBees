package com.resourcefulbees.resourcefulbees.effects;

import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import org.jetbrains.annotations.NotNull;

public class Calming extends Effect {
    protected Calming(EffectType beneficial, int color) {
        super(beneficial, color);
    }

    @Override
    public void performEffect(@NotNull LivingEntity entity, int level) {
        if (entity instanceof IAngerable) {
            IAngerable angerable = (IAngerable) entity;
            angerable.stopAnger();
        }
        super.performEffect(entity, level);
    }

    @Override
    public boolean isReady(int duration, int level) {
        return duration % 5 == 0;
    }
}
