package jason.architecture;

import jason.AslFileGenerator;
import jason.AslTransferenceModel;
import jason.asSyntax.Term;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;
import lac.cnclib.net.NodeConnection;
import lac.cnclib.net.NodeConnectionListener;
import lac.cnclib.net.mrudp.MrUdpNodeConnection;
import lac.cnclib.sddl.message.ApplicationMessage;
import lac.cnclib.sddl.message.Message;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommMiddleware implements NodeConnectionListener {

    private MrUdpNodeConnection connection;

    private String agName = "";

    private ArrayList<jason.asSemantics.Message> jMsg = new ArrayList<jason.asSemantics.Message>();

    private String answerToSendAboutTransfer;

    private String replySentAboutTransfer;

    private List<String> nameAgents;

    private List<AslTransferenceModel> agentsReceived;

    private String protocol;

    private UUID senderUUID;

    private String myUUID;

    private static final String EMPTY_VALUE = "";

    private static final String AGENT_FILE_EXTENSION = ".asl";

    private static final String COMMUNICATOR_PREAMBLE = "fffe";

    private static final String COMMUNICATOR_SIZE_MESSAGE_DEFINITION = "ss";

    public CommMiddleware(String gatewayIP, int gatewayPort, String myUUID) {
        this.myUUID = myUUID;
        UUID uuid = UUID.fromString(myUUID);
        InetSocketAddress address = new InetSocketAddress(gatewayIP, gatewayPort);
        try {
            this.connection = new MrUdpNodeConnection(uuid);
            this.connection.addNodeConnectionListener(this);
            this.connection.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAgName() {
        return this.agName;
    }

    public void setAgName(String agName) {
        this.agName = agName;
    }

    public List<AslTransferenceModel> getAgentsReceived() {
        return this.agentsReceived;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public UUID getSenderUUID() {
        return this.senderUUID;
    }

    public List<String> getNameAgents() {
        return this.nameAgents;
    }

    public String getMyUUID() {
        return this.myUUID;
    }

    public void connected(NodeConnection arg0) {
        ApplicationMessage message = new ApplicationMessage();
        message.setContentObject("Registering");
        try {
            this.connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reconnected(NodeConnection arg0, SocketAddress arg1, boolean arg2, boolean arg3) {
    }

    public void disconnected(NodeConnection arg0) {
    }

    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * [Pantoja]:
     * a) Tem que tratar as mensagens recebidas. Colocar em um
     * pool de mensagens que serão limpas após serem processadas no novo
     * (possível) checkMail.
     *
     * b) também é preciso desmontar as mensagens
     * vinda do protocolo e transformá-las em mensagens do tipo Message do
     * Jason.
     */
    public void newMessageReceived(NodeConnection remoteCon, Message message) {
        if (message.getContentObject() instanceof String) {
            this.extractMessageFromContextNet(message.getContentObject().toString().toCharArray());

            // Verifica se há algo relacionado à transferência de agentes para responder a quem enviou a solicitação
            // de transferência de agentes.
            if (this.answerToSendAboutTransfer != null && !this.answerToSendAboutTransfer.equals(EMPTY_VALUE)
                    && this.answerToSendAboutTransfer.equals(TransportAgentMessageType.CAN_TRANSFER.getName())) {
                ApplicationMessage appMessage = new ApplicationMessage();
                appMessage.setContentObject(prepareToSend(this.answerToSendAboutTransfer));
                appMessage.setRecipientID(message.getSenderID());
                try {
                    this.connection.sendMessage(appMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.replySentAboutTransfer != null && !this.replySentAboutTransfer.equals(EMPTY_VALUE)
                    && this.replySentAboutTransfer.equals(TransportAgentMessageType.CAN_TRANSFER.getName())) {
                Map<String, CentralisedAgArch> agentsOfTheSMA = RunCentralisedMAS.getRunner().getAgs();
                ArrayList<AslTransferenceModel> aslTransferenceModelList = new ArrayList<AslTransferenceModel>();
                int qtdAgents = 0;
                for (CentralisedAgArch centralisedAgArch : agentsOfTheSMA.values()) {
                    if (this.nameAgents.contains(centralisedAgArch.getAgName())) {
                        AslFileGenerator aslFileGenerator = new AslFileGenerator();
                        AslTransferenceModel aslTransferenceModel;
                        if (TransportAgentMessageType.INQUILINISM.getName().equalsIgnoreCase(this.getProtocol())) {
                            aslTransferenceModel = aslFileGenerator.generateAslContentWithoutIntentions(
                                    centralisedAgArch.getUserAgArch());
                        } else {
                            aslTransferenceModel = aslFileGenerator.generateAslContent(
                                    centralisedAgArch.getUserAgArch());
                        }

                        aslTransferenceModelList.add(aslTransferenceModel);
                        qtdAgents++;
                    }
                }

                // Verificando se a quantidade de agentes está de acordo com o que se deseja enviar.
                if (qtdAgents == this.nameAgents.size()) {
                    ApplicationMessage appMessage = new ApplicationMessage();
                    appMessage.setContentObject(aslTransferenceModelList);
                    appMessage.setRecipientID(message.getSenderID());
                    try {
                        this.connection.sendMessage(appMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("[ERRO]: Não é possível realizar a transferência porque a quantidade de "
                            + "agentes esperados para envio não foi satisfeita.");
                }
            }
            if (this.replySentAboutTransfer != null && !this.replySentAboutTransfer.equals(EMPTY_VALUE)
                    && this.replySentAboutTransfer.equals(TransportAgentMessageType.CAN_KILL.getName())) {
                //Deletar os arquivos ASL
                if (this.protocol != null && !TransportAgentMessageType.CLONING.getName().equals(this.protocol)) {
                    Map<String, CentralisedAgArch> agentsOfTheSMA = RunCentralisedMAS.getRunner().getAgs();
                    for (CentralisedAgArch centralisedAgArch : agentsOfTheSMA.values()) {
                        if (this.nameAgents.contains(centralisedAgArch.getAgName())) {
                            String path = centralisedAgArch.getTS().getAg().getASLSrc();
                            File file = new File(path);
                            this.deleteFileAsl(file);
                        }
                    }
                }
            }
        } else if (message.getContentObject() instanceof ArrayList) {
            // Recebi os agentes
            AslFileGenerator aslFileGenerator = new AslFileGenerator();
            ArrayList<AslTransferenceModel> aslTransferenceModelArrayList = (ArrayList<AslTransferenceModel>) message
                    .getContentObject();
            int qtdAgentsReceived = 0;
            for (AslTransferenceModel aslTransferenceModel : aslTransferenceModelArrayList) {
                if (this.nameAgents.contains(aslTransferenceModel.getName())) {
                    // Descobrindo o path que os agentes devem ser criados.
                    String path = EMPTY_VALUE;
                    for (CentralisedAgArch centralisedAgArch : RunCentralisedMAS.getRunner().getAgs().values()) {
                        path = centralisedAgArch.getTS().getAg().getASLSrc();
                        path = path.substring(0, path.length() - (centralisedAgArch.getAgName()
                                + AGENT_FILE_EXTENSION).length());
                        break;
                    }
                    aslFileGenerator.createAslFile(path, aslTransferenceModel);
                    qtdAgentsReceived++;
                }
            }

            if (qtdAgentsReceived == this.nameAgents.size()) {
                // Todos agentes recebidos com sucesso.
                this.answerToSendAboutTransfer = TransportAgentMessageType.CAN_KILL.getName();
                this.senderUUID = message.getSenderID();
                this.agentsReceived = aslTransferenceModelArrayList;
            } else {
                System.out.println("Qtd: " + qtdAgentsReceived + "\tQtdNames: " + this.nameAgents.size());
            }
        } else {
            System.err.println("Error: Getting the object content in the ContextNet communication");
        }
        //System.out.println("[ARGO]: A Message has arrived: " + message.getContentObject().toString());
    }

    public void unsentMessages(NodeConnection arg0, List<Message> arg1) {
    }

    public void internalException(NodeConnection arg0, Exception arg1) {
    }

    /*
     * [Pantoja]: Functions provided for working with Javino. These functions
     * already exists in Javino
     */

    public void cleanMailBox() {
        if (this.jMsg.size() > 0) {
            this.jMsg.remove(0);
        }
    }

    public boolean hasMsg() {
        //System.out.println("[ARGO]: Messages = " + this.jMsg.size());
        return this.jMsg != null && this.jMsg.size() > 0;
    }

    public boolean hasAgentsReceived() {
        return this.agentsReceived != null && !this.agentsReceived.isEmpty();
    }

    public boolean hasToKillMyAgents() {
        return this.replySentAboutTransfer != null && this.replySentAboutTransfer.length() > 0
                && this.replySentAboutTransfer.equals(TransportAgentMessageType.CAN_KILL.getName());
    }

    public void deleteFileAsl(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    public void cleanAtributesOfTransference() {
        this.protocol = "";
        this.agentsReceived = new ArrayList<AslTransferenceModel>();
        this.answerToSendAboutTransfer = "";
        this.replySentAboutTransfer = "";
        this.nameAgents = new ArrayList<String>();
        this.protocol = "";
        this.senderUUID = null;
    }

    public jason.asSemantics.Message checkMailCN() {
        return this.jMsg.get(0);
    }

    public void extractMessageFromContextNet(char[] message) {
        String preamble = String.valueOf(message, 0, 4);
        if (preamble.equals(COMMUNICATOR_PREAMBLE)) {
            String firstParam = EMPTY_VALUE;
            for (int cont = 6; cont < (this.hex2int(char2int(message[5]), char2int(message[4])) + 6); cont++) {
                firstParam += message[cont];
            }

            // Após coletar o primeira parâmetro, será possível interpretar a mensagem. Os protocolos de
            // transferência de agentes são tratados da mesma maneira. Já os demais terão uma lógica específica.
            if (firstParam.equals(TransportAgentMessageType.PREDATION.getName()) || firstParam.equals(
                    TransportAgentMessageType.INQUILINISM.getName()) || firstParam.equals(
                    TransportAgentMessageType.MUTUALISM.getName()) || firstParam.equals(
                    TransportAgentMessageType.CLONING.getName())) {
                this.protocol = firstParam;
                treatAgentTransferenceMsg(message);
            } else if (firstParam.equals(TransportAgentMessageType.CAN_TRANSFER.getName()) || firstParam.equals(
                    TransportAgentMessageType.CAN_KILL.getName())) {
                this.replySentAboutTransfer = firstParam;
            } else {
                treatSendOutMessage(firstParam, message);
            }
        } else {
            System.err.println("[COMMUNICATOR]: The preamble does not match!");
        }
    }

    /**
     * Trata as mensagens em texto de transferência de agentes.
     *
     * @param message Mensagem que contem as informações necessárias que serão coletadas pelo comunicador.
     */
    private void treatAgentTransferenceMsg(char[] message) {
        // O índice da próxima mensagem começa com o fim da mensagem anterior. Até então, foi lido
        // "[fffe][ss][mensagem]".
        int currentMsgIndex = COMMUNICATOR_PREAMBLE.length() + COMMUNICATOR_SIZE_MESSAGE_DEFINITION.length()
                + this.protocol.length();
        int currentValueIndex = currentMsgIndex + 2;
        int currentValueSize = this.hex2int(char2int(message[currentMsgIndex + 1]), char2int(message[currentMsgIndex]));
        int finalPosition = 0;
        finalPosition = currentValueIndex + currentValueSize;
        this.nameAgents = new ArrayList<String>();
        while (finalPosition < message.length) {
            String name = EMPTY_VALUE;
            for (int cont = currentValueIndex; cont < (currentValueIndex + currentValueSize); cont++) {
                name += message[cont];
            }
            this.nameAgents.add(name);

            currentMsgIndex = currentValueIndex + currentValueSize;
            currentValueIndex = currentMsgIndex + 2;
            currentValueSize = this.hex2int(char2int(message[currentMsgIndex + 1]), char2int(message[currentMsgIndex]));
            finalPosition = currentValueIndex + currentValueSize;
        }
        String name = EMPTY_VALUE;
        for (int cont = currentValueIndex; cont < (currentValueIndex + currentValueSize); cont++) {
            name += message[cont];
        }
        this.nameAgents.add(name);

        this.answerToSendAboutTransfer = TransportAgentMessageType.CAN_TRANSFER.getName();
    }

    /**
     * Trata as mensagens de sendOut do agente Communicator.
     *
     * @param sender Parâmetro que indica o agente que está enviando a mensagem.
     * @param message Mensagem enviada.
     */
    private void treatSendOutMessage(String sender, char[] message) {
        String force = EMPTY_VALUE;
        String msg = EMPTY_VALUE;
        int nextValue = sender.length() + 6;
        int shift = this.hex2int(char2int(message[nextValue + 1]), char2int(message[nextValue]));
        for (int cont = nextValue + 2; cont <= (shift + nextValue + 1); cont++) {
            force += message[cont];
        }
        nextValue = shift + nextValue + 2;
        shift = this.hex2int(char2int(message[nextValue + 1]), char2int(message[nextValue]));
        for (int cont = nextValue + 2; cont <= (shift + nextValue + 1); cont++) {
            msg += message[cont];
        }

        jason.asSemantics.Message jasonMsgs = new jason.asSemantics.Message();
        jasonMsgs.setIlForce(force);
        jasonMsgs.setSender(sender);
        jasonMsgs.setPropCont(msg);
        jasonMsgs.setReceiver(this.agName);
        //System.out.println("[ARGO]: The message is: " + jasonMsgs.toString());
        this.jMsg.add(jasonMsgs);
    }

    public void sendMsgToContextNet(String sender, String receiver, Term force, Term msg) {
        ApplicationMessage message = new ApplicationMessage();
        message.setContentObject(this.prepareToSend(sender, force.toString(), msg.toString()));
        message.setRecipientID(UUID.fromString(receiver.substring(1, receiver.length() - 1)));
        try {
            this.connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAllAgentsToContextNet(String receiver, Term protocol, List<String> nameAgents) {
        ApplicationMessage message = new ApplicationMessage();
        this.nameAgents = new ArrayList<String>();
        this.nameAgents.addAll(nameAgents);
        message.setContentObject(this.prepareToSend(protocol.toString().toUpperCase().trim(), nameAgents));
        message.setRecipientID(UUID.fromString(receiver.substring(1, receiver.length() - 1)));
        try {
            this.connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendAgentToContextNet(String receiver, Term protocol, Term agent) {
        ApplicationMessage message = new ApplicationMessage();
        this.nameAgents = new ArrayList<String>();
        this.nameAgents.add(agent.toString());
        message.setContentObject(this.prepareToSend(protocol.toString().toUpperCase().trim(), agent.toString()));
        message.setRecipientID(UUID.fromString(receiver.substring(1, receiver.length() - 1)));
        try {
            this.connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsgToDeleteAllAgents() {
        ApplicationMessage appMessage = new ApplicationMessage();
        appMessage.setContentObject(this.prepareToSend(this.answerToSendAboutTransfer));
        appMessage.setRecipientID(this.senderUUID);
        try {
            this.connection.sendMessage(appMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String prepareToSend(String msg) {
        msg = COMMUNICATOR_PREAMBLE + int2hex(msg.length()) + msg;
        return msg;
    }

    private String prepareToSend(String protocol, List<String> nameAgents) {
        String msg = COMMUNICATOR_PREAMBLE + int2hex(protocol.length()) + protocol;
        for (String nameAgent : nameAgents) {
            msg += int2hex(nameAgent.length()) + nameAgent;
        }
        return msg;
    }

    private String prepareToSend(String protocol, String nameAgent) {
        return COMMUNICATOR_PREAMBLE + int2hex(protocol.length()) + protocol + int2hex(nameAgent.length()) + nameAgent;
    }

    private String prepareToSend(String sender, String force, String msg) {
        msg = COMMUNICATOR_PREAMBLE + int2hex(sender.length()) + sender + int2hex(force.length()) + force
                + int2hex(msg.length()) + msg;
        return msg;
    }

    private String int2hex(int v) {
        String stringOne = Integer.toHexString(v);
        if (v < 16) {
            stringOne = "0" + stringOne;
        }
        return stringOne;
    }

    private int char2int(char charValue) {
        int intValue = 0;
        switch (charValue) {
            case '1':
                intValue = 1;
                break;
            case '2':
                intValue = 2;
                break;
            case '3':
                intValue = 3;
                break;
            case '4':
                intValue = 4;
                break;
            case '5':
                intValue = 5;
                break;
            case '6':
                intValue = 6;
                break;
            case '7':
                intValue = 7;
                break;
            case '8':
                intValue = 8;
                break;
            case '9':
                intValue = 9;
                break;
            case 'a':
                intValue = 10;
                break;
            case 'b':
                intValue = 11;
                break;
            case 'c':
                intValue = 12;
                break;
            case 'd':
                intValue = 13;
                break;
            case 'e':
                intValue = 14;
                break;
            case 'f':
                intValue = 15;
                break;
        }
        return intValue;
    }

    private int hex2int(int x, int y) {
        int converted = x + (y * 16);
        return converted;
    }
}
