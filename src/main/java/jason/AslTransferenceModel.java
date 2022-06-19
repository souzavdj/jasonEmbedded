package jason;

import java.io.Serializable;

/**
 * Modelo de transferência de agente.
 */
public class AslTransferenceModel implements Serializable {

    /** Nome do agente. */
    private String name;

    /** Conteúdo do arquivo. */
    private byte[] fileContent;

    /**
     * Construtor.
     *
     * @param name {@link #name}
     * @param fileContent {@link #fileContent}
     */
    public AslTransferenceModel(String name, byte[] fileContent) {
        this.name = name;
        this.fileContent = fileContent;
    }

    public String getName() {
        return name;
    }

    public byte[] getFileContent() {
        return fileContent;
    }
}
