package jason.architecture;

/**
 * Tipos de protocolo de transferência de agentes entre SMA distintos.
 */
public enum TransportAgentMessageType {
    PREDATION(1, "PREDATION"),
    MUTUALISM(2, "MUTUALISM"),
    INQUILINISM(3, "INQUILINISM"),
    
    //Clonagem begin
    CLONAGEM_SMA(6, "CLONAGEM SMA"),
    CLONAGEM_AGENT(7, "CLONAGEN AGENTE"),
    //Clonagem end
    
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
