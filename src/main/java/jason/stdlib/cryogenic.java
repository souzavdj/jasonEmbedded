package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class cryogenic extends DefaultInternalAction {

    private static final long serialVersionUID = -4841692752581197132L;

    private boolean isIlloc;
    //private Javino javinoBridge = new Javino("/dev/com8","c:\\cygwin64\\bin\\", 150);

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
       ts.getUserAgArch().getCommBridge().cryogenic();
        return true;

    }
}
