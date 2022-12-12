import jason.AslTransferenceModel;
import jason.RevisionFailedException;
import jason.architecture.AgArch;
import jason.architecture.CommMiddleware;
import jason.architecture.TransportAgentMessageType;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;
import jason.bb.BeliefBase;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;
import jason.mas2j.ClassParameters;
import jason.runtime.RuntimeServicesInfraTier;
import jason.util.BeliefUtils;
import jason.util.BioInspiredProtocolLogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Communicator extends AgArch {

    private CommMiddleware commBridge = null;

    private boolean connected = false;

    private static final String AGENT_FILE_EXTENSION = ".asl";

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void connectCN(String gatewayIP, int gatewayPort, String myUUID) {
        this.commBridge = new CommMiddleware(gatewayIP, gatewayPort, myUUID);
        this.commBridge.setAgName(this.getAgName());
        this.connected = true;
    }

    @Override
    public void disconnectCN() {
        if (this.commBridge != null) {
            this.commBridge.disconnect();
            this.connected = false;
        }
    }

    @Override
    public CommMiddleware getCommBridge() {
        return this.commBridge;
    }

    @Override
    public void addMessageToC() {
        this.getTS().getC().addMsg(this.commBridge.checkMailCN());
        //this.commBridge.cleanMailBox();
    }

    @Override
    public void instantiateAgents() {
        if (this.commBridge.getProtocol().equals(TransportAgentMessageType.PREDATION.getName())) {
            this.executePredatorProtocol();
        } else if (this.commBridge.getProtocol().equals(TransportAgentMessageType.MUTUALISM.getName())) {
            this.executeMutualismProtocol();
        } else if (this.commBridge.getProtocol().equals(TransportAgentMessageType.INQUILINISM.getName())) {
            this.executeInquilinismProtocol();
        }
    }

    private int startAgent(String name, String path, String agArchClasse, int qtdAgentsInstantiated) {
        try {
            String agClass = null;
            List<String> agArchClasses = new ArrayList<String>();
            if(agArchClasse != null && !agArchClasse.isEmpty()) {
                agArchClasses.add(agArchClasse);
            }
            ClassParameters bbPars = null;

            RuntimeServicesInfraTier rs = this.getTS().getUserAgArch().getRuntimeServices();
            name = rs.createAgent(name, path, agClass, agArchClasses, bbPars, this.getTS().getSettings());
            rs.startAgent(name);
            qtdAgentsInstantiated++;
            return qtdAgentsInstantiated;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qtdAgentsInstantiated;
    }

    private void executePredatorProtocol () {
        int qtdAgentsInstantiated = 0;
        for (AslTransferenceModel aslTransferenceModel : this.commBridge.getAgentsReceived()) {
            String name = aslTransferenceModel.getName();
            String path = getPath(name);
            String agArchClass = aslTransferenceModel.getAgentArchClass();

            if (agArchClass.equals(Communicator.class.getName())) {
                this.commBridge.setHasCommunicatorAgentTransferred(true);
            }

            qtdAgentsInstantiated = this.startAgent(name, path, agArchClass, qtdAgentsInstantiated);
        }

        if (qtdAgentsInstantiated == this.commBridge.getAgentsReceived().size()) {
            // Todos os agentes instanciados, enviando mensagem para deletar da origem
            this.commBridge.sendMsgToDeleteAllAgents();
            this.killAllAgents();
        }
    }

    private void executeMutualismProtocol() {
        int qtdAgentsInstantiated = 0;
        for (AslTransferenceModel aslTransferenceModel : this.commBridge.getAgentsReceived()) {
            String name = aslTransferenceModel.getName();
            String path = getPath(name);
            String agArchClass = aslTransferenceModel.getAgentArchClass();

            if (agArchClass.equals(Communicator.class.getName())) {
                this.commBridge.setHasCommunicatorAgentTransferred(true);
            }

            qtdAgentsInstantiated = this.startAgent(name, path, agArchClass, qtdAgentsInstantiated);
        }
        if (qtdAgentsInstantiated == this.commBridge.getAgentsReceived().size()) {
            // Todos os agentes instanciados, enviando mensagem para deletar da origem
            this.commBridge.sendMsgToDeleteAllAgents();
        }
    }

    private void executeInquilinismProtocol () {
        int qtdAgentsInstantiated = 0;
        for (AslTransferenceModel aslTransferenceModel : this.commBridge.getAgentsReceived()) {
            String name = aslTransferenceModel.getName();
            String path = getPath(name);
            String agArchClass = aslTransferenceModel.getAgentArchClass();

            if (agArchClass.equals(Communicator.class.getName())) {
                this.commBridge.setHasCommunicatorAgentTransferred(true);
            }

            qtdAgentsInstantiated = this.startAgent(name, path, agArchClass, qtdAgentsInstantiated);
        }
        if (qtdAgentsInstantiated == this.commBridge.getAgentsReceived().size()) {
            // Todos os agentes instanciados, enviando mensagem para deletar da origem
            this.commBridge.sendMsgToDeleteAllAgents();
        }
    }

    private String getPath(String agentName) {
        String path = "";
        for (CentralisedAgArch centralisedAgArch : RunCentralisedMAS.getRunner().getAgs().values()) {
            path = centralisedAgArch.getTS().getAg().getASLSrc();
            path = path.substring(0, path.length() - (centralisedAgArch.getAgName() + AGENT_FILE_EXTENSION).length());
            break;
        }
        path += agentName + AGENT_FILE_EXTENSION;
        return path;
    }

    @Override
    public void killAllAgents() {
        Map<String, CentralisedAgArch> agentsOfTheSMA = RunCentralisedMAS.getRunner().getAgs();
        if (this.commBridge.getAgentsReceived() != null && !this.commBridge.getAgentsReceived().isEmpty()
                && this.commBridge.getAgentsReceived().size() > 0) {
            for (CentralisedAgArch centralisedAgArch : agentsOfTheSMA.values()) {
                if (!this.commBridge.getNameAgents().contains(centralisedAgArch.getAgName())) {
                    this.getTS().getUserAgArch().getRuntimeServices().killAgent(centralisedAgArch.getAgName(),
                            this.getTS().getUserAgArch().getAgName());
                    File file = new File(centralisedAgArch.getTS().getAg().getASLSrc());
                    this.commBridge.deleteFileAsl(file);
                }
            }
        } else {
            for (CentralisedAgArch centralisedAgArch : agentsOfTheSMA.values()) {
                if (this.commBridge.getNameAgents().contains(centralisedAgArch.getAgName())) {
                    this.getTS().getUserAgArch().getRuntimeServices().killAgent(centralisedAgArch.getAgName(),
                            this.getTS().getUserAgArch().getAgName());
                }
            }
        }
    }

    @Override
    public void connectAutomatically() {
        if (this.getCommBridge() == null || !this.isConnected()) {
            Agent communicatorAgent = this.getTS().getAg();
            List<String> serverAddressBeliefList = BeliefUtils.getBeliefByStartWith(communicatorAgent.getBB(),
                    BeliefUtils.SERVER_ADDRESS_BELIEF_PREFIX);
            List<String> serverPortBeliefList = BeliefUtils.getBeliefByStartWith(communicatorAgent.getBB(),
                    BeliefUtils.SERVER_PORT_BELIEF_PREFIX);
            List<String> myUUIDBeliefList = BeliefUtils.getBeliefByStartWith(communicatorAgent.getBB(),
                    BeliefUtils.MY_UUID_BELIEF_PREFIX);
            String sourceSelf = BeliefBase.TSelf.toString();
            String serverAddress = BeliefUtils.getBeliefValue(serverAddressBeliefList,
                    sourceSelf);
            serverAddress = serverAddress.replaceAll("\"", "");
            String serverPort = BeliefUtils.getBeliefValue(serverPortBeliefList,
                    sourceSelf);
            serverPort = serverPort.replaceAll("\"", "");
            String myUUID = BeliefUtils.getBeliefValue(myUUIDBeliefList,
                    sourceSelf);
            myUUID = myUUID.replaceAll("\"", "");
            this.connectCN(serverAddress, Integer.parseInt(serverPort), myUUID);
            String masName = RunCentralisedMAS.getRunner().getProject().getSocName();
            Literal myMASBelief = Literal.parseLiteral(BeliefUtils.MY_MAS_BELIEF_VALUE.replace(
                    BeliefUtils.VALUE_REPLACEMENT,
                    masName));
            try {
                this.getTS().getAg().addBel(myMASBelief);
            } catch (RevisionFailedException e) {
                BioInspiredProtocolLogUtils.LOGGER.log(Level.SEVERE,
                        "Error: Tt was not possible to add the belief related to the MAS name: " + e);
            }
        }
    }
}
