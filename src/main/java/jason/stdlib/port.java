// Internal action code for project javinoArchitectureWithInternalActions

//If you want only to use '.move' command type 'package jason.stdlib'. By Pantoja.
package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class port extends DefaultInternalAction {

    private static final long serialVersionUID = -4841692752581197132L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        Term illoc = args[0];
        String os = System.getProperty("os.name");
        if (os.substring(0, 1).equals("W")) {
            ts.getUserAgArch().setPort(illoc.toString());
        } else {
            ts.getUserAgArch().setPort("/dev/" + illoc.toString());
        }
        return true;
    }
}
