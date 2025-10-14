package com.example.operatedarocket.utils;

public enum Chapters {
    THE_BEGINNING("1: The Beginning",
            """
                    Your friend, Nolan has told you about a new space mission.
                    He is one of the scientists working on the project.
                    After a long discussion, you have decided to join the mission.
                    But you have to register yourself first.
                    And a lot of more things.
                    \s"""),
    THE_LAUNCH("2: The Launch",
            """
                    CHAPTER NOT INITIALIZED YET, TYPE 'exit' to QUIT
                    \s"""),
    THE_MISSION("3: The Mission",
            """
                    CHAPTER NOT INITIALIZED YET, TYPE 'exit' to QUIT
                    \s""");

    private String label, desc;

    private Chapters(String label, String desc) {
        this.label = label;
        this.desc = desc;
    }

    public String getLabel() {
        return label;
    }

    public String getDesc() {
        return desc;
    }
}
