// Internal action code for project javinoArchitectureWithInternalActions

//If you want only to use '.move' command type 'package jason.stdlib'. By Pantoja.
package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.asSyntax.Literal;

public class act extends DefaultInternalAction {

    private static final long serialVersionUID = -4841692752581197132L;

    private boolean isIlloc;
    //private Javino javinoBridge = new Javino("/dev/com8","c:\\cygwin64\\bin\\", 150);

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        Term action = args[0];
        if (ts.getUserAgArch().getArgo().sendCommand(ts.getUserAgArch().getPort(), action.toString())) {
            return true;
        } else {
            String PORT = ts.getUserAgArch().getPort();
            String PORTshortNAME=PORT.substring(PORT.lastIndexOf("/")+1);
            if(PORTshortNAME==""){
                PORTshortNAME="unknown";
            }
            ts.getAg().getBB().remove(Literal.parseLiteral("port("+PORTshortNAME+",on);"));
            ts.getAg().getBB().add(Literal.parseLiteral("port("+PORTshortNAME+",off);"));
            return false;
        }
    }
}
