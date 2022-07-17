package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class connectCN extends DefaultInternalAction {

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
        return true;
    }

}
