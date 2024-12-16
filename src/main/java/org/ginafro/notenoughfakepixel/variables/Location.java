package org.ginafro.notenoughfakepixel.variables;

public enum Location {

    DWARVEN("sbm-"),
    HUB("skyblock-"),
    DUNGEON_HUB("sbdh-"),
    BARN("sbfarms-"),
    PARK("sbpark-"),
    GOLD_MINE("sbmines-"),
    PRIVATE_ISLAND("sbi-"),
    JERRY("sbj-"),
    SPIDERS_DEN("sbspiders-"),
    THE_END("sbend-"),
    CRIMSON_ISLE("sbcris-"),
    DUNGEON("sbdungeon-"),
    NONE("");

    private String s;

    Location(String s){
        this.s = s;
    }

    public String getLocation() {
        return this.s;
    }

    public static Location getLocation(String s){
        for(Location l : Location.values()){
            if(l.getLocation().equals(s)) return l;
        }
        return NONE;
    }

    public boolean isDungeon(){
        return this == DUNGEON;
    }

}
