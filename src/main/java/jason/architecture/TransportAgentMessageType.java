package jason.architecture;

/**
 * Tipos de protocolo de transferência de agentes entre SMA distintos.
 */
public enum TransportAgentMessageType {
    PREDATOR(1, "PREDATOR"),
    MUTUALISM(2, "MUTUALISM"),
    INQUILINISM(3, "INQUILINISM"),

    CAN_TRANSFER(4, "CAN TRANSFER THE AGENT(S)"),
    CAN_KILL(5, "CAN KILL THE AGENT(S)");

    int id;
    String name;

    TransportAgentMessageType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
