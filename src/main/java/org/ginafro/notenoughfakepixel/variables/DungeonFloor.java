package org.ginafro.notenoughfakepixel.variables;

public enum DungeonFloor {
    NONE(-1),
    E0(0),
    F1(30),
    F2(40),
    F3(50),
    F4(60),
    F5(70),
    F6(85),
    F7(100),
    M1(30),
    M2(40),
    M3(50),
    M4(60),
    M5(70),
    M6(85),
    M7(100);

    private int secretPercentage;

    DungeonFloor(int secretPercentage) {
        this.secretPercentage = secretPercentage;
    }

    public int getSecretPercentage() {
        return secretPercentage;
    }
    
    public static DungeonFloor getFloor(String fromValue){
        for(DungeonFloor floor : DungeonFloor.values()){
            if(floor.name().equals(fromValue)){
                return floor;
            }
        }
        return DungeonFloor.NONE;
    }

}

