package org.ginafro.notenoughfakepixel.features.skyblock.diana;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

public class GaiaConstruct {
    private Entity entity;
    private EntityLivingBase entityLivingBase;
    private float hpGaiaConstruct260 = 1500000;
    private float hpGaiaConstruct140 = 300000;
    private float maxHp;
    private int state; // 0 -> 66-100% hp, 1 -> 33-66% hp, 2 -> 0-33% hp
    private boolean canBeHit;
    private int hits;
    final private int[] hitsNeeded;

    public GaiaConstruct(Entity entity) {
        this.entity = entity;
        this.entityLivingBase = (EntityLivingBase) entity;
        this.state = 0;
        this.canBeHit = false;
        this.hits = 0;
        this.hitsNeeded = new int[] {6,7,8};
        float hp = entityLivingBase.getHealth();
        float diffTo140 = Math.abs(hp - hpGaiaConstruct140);
        float diffTo260 = Math.abs(hp - hpGaiaConstruct260);
        maxHp = hpGaiaConstruct260; // Set maxHp from lvl 260
        if (diffTo140 < diffTo260) {
            maxHp = hpGaiaConstruct140; // Set maxHp from lvl 140
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public int getState() {
        return state;
    }

    public void setStateFromHp() {
        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
        if (entityLivingBase.getHealth() / maxHp >= 0.66) {
            this.state = 0;
        } else if (0.33 <= entityLivingBase.getHealth()/maxHp && entityLivingBase.getHealth()/maxHp < 0.66) {
            this.state = 1;
        } else if (entityLivingBase.getHealth()/maxHp < 0.33) {
            this.state = 2;
        }
    }

    public int getHits() {
        return hits;
    }

    public void addHit() {
        hits++;
        if (hits == hitsNeeded[state]-1) {
            canBeHit = true;
        } else if (hits == hitsNeeded[state]) {
            canBeHit = false;
            hits = 0;
        }
        setStateFromHp();
    }

    public void setHits(int hits) {
        this.hits = hits;
        setStateFromHp();
    }

    public boolean canBeHit() {
        return canBeHit;
    }
}