package jason.stdlib;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.infra.centralised.RunCentralisedMAS;
import jason.util.BeliefUtils;
import jason.util.BioInspiredProtocolLogUtils;

import java.util.logging.Level;

public class connect extends DefaultInternalAction {

    @Override
    public int getMinArgs() {
        return 3;
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    protected void checkArguments(Term[] args) throws JasonException {
        // Verifica a quantidade de argumentos.
        if (args.length < getMinArgs() || args.length > getMaxArgs()) {
            BioInspiredProtocolLogUtils.LOGGER.log(Level.SEVERE, "Error: The number of arguments passed was ('"
                    + args.length + "') and must be between "+ getMinArgs() + " and " + getMaxArgs() + "!");
            throw JasonException.createWrongArgumentNb(this);
        }


    }

    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        String gatewayIP = args[0].toString();
        if (gatewayIP.startsWith("\"")) {
            gatewayIP = gatewayIP.substring(1, gatewayIP.length() - 1);
        }

        int gatewayPort = Integer.parseInt(args[1].toString());

        String uuid = args[2].toString();
        if (uuid.startsWith("\"")) {
            uuid = uuid.substring(1, uuid.length() - 1);
        }
        ts.getUserAgArch().connectCN(gatewayIP, gatewayPort, uuid);
        ts.getAg().addBel(Literal.parseLiteral(BeliefUtils.SERVER_ADDRESS_BELIEF_VALUE.replace(
                BeliefUtils.VALUE_REPLACEMENT, gatewayIP)));
        ts.getAg().addBel(Literal.parseLiteral(BeliefUtils.SERVER_PORT_BELIEF_VALUE.replace(
                BeliefUtils.VALUE_REPLACEMENT, String.valueOf(gatewayPort))));
        ts.getAg().addBel(Literal.parseLiteral(BeliefUtils.MY_UUID_BELIEF_VALUE.replace(
                BeliefUtils.VALUE_REPLACEMENT, uuid)));
        String masName = RunCentralisedMAS.getRunner().getProject().getSocName();
        Literal myMASBelief = Literal.parseLiteral(BeliefUtils.MY_MAS_BELIEF_VALUE.replace(
                BeliefUtils.VALUE_REPLACEMENT,
                masName));
        ts.getAg().addBel(myMASBelief);
        return true;
    }

}
