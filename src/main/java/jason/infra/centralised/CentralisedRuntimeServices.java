package jason.infra.centralised;

import jason.JasonException;
import jason.RevisionFailedException;
import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;
import jason.mas2j.AgentParameters;
import jason.mas2j.ClassParameters;
import jason.runtime.RuntimeServicesInfraTier;
import jason.runtime.Settings;
import jason.util.BeliefUtils;
import jason.util.BioInspiredProtocolLogUtils;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** This class implements the centralised version of the runtime services. */
public class CentralisedRuntimeServices implements RuntimeServicesInfraTier {

    private static Logger logger = Logger.getLogger(CentralisedRuntimeServices.class.getName());

    protected RunCentralisedMAS masRunner;

    public CentralisedRuntimeServices(RunCentralisedMAS masRunner) {
        this.masRunner = masRunner;
    }

    protected CentralisedAgArch newAgInstance() {
        return new CentralisedAgArch();
    }

    public String createAgent(String agName, String agSource, String agClass, List<String> archClasses,
                              ClassParameters bbPars, Settings stts) throws Exception {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Creating centralised agent.asl " + agName + " from source " + agSource + " (agClass=" + agClass
                    + ", archClass=" + archClasses + ", settings=" + stts);
        }

        AgentParameters ap = new AgentParameters();
        ap.setAgClass(agClass);
        ap.addArchClass(archClasses);
        ap.setBB(bbPars);

        if (stts == null) {
            stts = new Settings();
        }

        String nb = "";
        int n = 1;
        while (masRunner.getAg(agName + nb) != null) {
            nb = "_" + (n++);
        }
        agName = agName + nb;

        CentralisedAgArch agArch = newAgInstance();
        agArch.setAgName(agName);
        agArch.createArchs(ap.getAgArchClasses(), ap.agClass.getClassName(), ap.getBBClass(), agSource, stts,
                masRunner);
        agArch.setEnvInfraTier(masRunner.getEnvironmentInfraTier());
        agArch.setControlInfraTier(masRunner.getControllerInfraTier());
        masRunner.addAg(agArch);

        logger.fine("Agent " + agName + " created!");
        return agName;
    }

    public void startAgent(String agName) {
        // create the agent thread
        CentralisedAgArch agArch = masRunner.getAg(agName);
        Thread agThread = new Thread(agArch);
        agArch.setThread(agThread);
        agThread.start();

        String masName = RunCentralisedMAS.getRunner().getProject().getSocName();
        Literal myMASBelief = Literal.parseLiteral(BeliefUtils.MY_MAS_BELIEF_VALUE.replace(
                BeliefUtils.VALUE_REPLACEMENT,
                masName));
        try {
            RunCentralisedMAS.getRunner().getAg(agName).getTS().getAg().addBel(myMASBelief);
        } catch (RevisionFailedException e) {
            BioInspiredProtocolLogUtils.LOGGER.log(Level.SEVERE,
                    "Error: Tt was not possible to add the belief related to the MAS name: " + e);
        }
    }

    public AgArch clone(Agent source, List<String> archClasses, String agName) throws JasonException {
        // create a new infra arch
        CentralisedAgArch agArch = newAgInstance();
        agArch.setAgName(agName);
        agArch.setEnvInfraTier(masRunner.getEnvironmentInfraTier());
        agArch.setControlInfraTier(masRunner.getControllerInfraTier());
        masRunner.addAg(agArch);

        agArch.createArchs(archClasses, source, masRunner);

        startAgent(agName);
        return agArch.getUserAgArch();
    }

    public Set<String> getAgentsNames() {
        return masRunner.getAgs().keySet();
    }

    public int getAgentsQty() {
        return masRunner.getAgs().keySet().size();
    }

    public boolean killAgent(String agName, String byAg) {
        logger.fine("Killing centralised agent.asl " + agName);
        CentralisedAgArch ag = masRunner.getAg(agName);
        if (ag != null && ag.getTS().getAg().killAcc(byAg)) {
            ag.stopAg();
            return true;
        }
        return false;
    }

    public void stopMAS() throws Exception {
        masRunner.finish();
    }
}
