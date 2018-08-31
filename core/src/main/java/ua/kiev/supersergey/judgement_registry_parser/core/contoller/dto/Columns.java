package ua.kiev.supersergey.judgement_registry_parser.core.contoller.dto;

public enum Columns {
    KEYWORD("keyword"),
    JUDGEMENT_FORM("judgementForm"),
    JUDGE("judge"),
    CREATED_TS("createdTs"),
    COURT("court"),
    DECISION_TYPE("decisionType");

    private String name;

    Columns(String name) {
        this.name = name;
    }

    public static Columns findByName(String name) {
        for (Columns c : Columns.values()) {
            if (c.name.equalsIgnoreCase(name)) {
                return c;
            }
        }
        throw new IllegalArgumentException("The specified column is not supported: " + name);
    }

    public String getName() {
        return name;
    }
}
