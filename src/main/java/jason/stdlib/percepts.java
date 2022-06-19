// Internal action code for project javinoArchitectureWithInternalActions

//If you want only to use '.move' command type 'package jason.stdlib'. By Pantoja.
package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class percepts extends DefaultInternalAction {

    private static final long serialVersionUID = -4841692752581197132L;

    // private boolean isIlloc;
    // private Javino javinoBridge = new
    // Javino("/dev/com8","c:\\cygwin64\\bin\\", 150);

    /*
     * @Override protected void checkArguments(Term[] args) throws
     * JasonException {
     *
     * super.checkArguments(args); // check number of arguments if
     * (!args[0].isAtom()) throw JasonException .createWrongArgument( this,
     * "The javino illocutionary force parameter ('" + args[0] +
     * "') must be an atom (front, back, left, right or stop)!"); if
     * (!args[0].toString().equals("front") &&
     * !args[0].toString().equals("left") && !args[0].toString().equals("back")
     * && !args[0].toString().equals("right") &&
     * !args[0].toString().equals("stop")) { throw
     * JasonException.createWrongArgument(this, "There is no '" +
     * args[0].toString() + "' javino illocutionary force!"); } }
     */

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        if (args[0].toString().equals("block")) {
            ts.blocked = true;
            return true;
        } else if (args[0].toString().equals("open")) {
            ts.blocked = false;
            return true;
        } else {
            return false;
        }
    }
}
