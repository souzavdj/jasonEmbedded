// Internal action code for project javinoArchitectureWithInternalActions

//If you want only to use '.move' command type 'package jason.stdlib'. By Pantoja.
package jason.stdlib;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class move extends DefaultInternalAction {

    private static final long serialVersionUID = -4841692752581197132L;

    private boolean isIlloc;
    //private Javino javinoBridge = new Javino("/dev/com8","c:\\cygwin64\\bin\\", 150);

    @Override
    protected void checkArguments(Term[] args) throws JasonException {

        super.checkArguments(args); // check number of arguments
        if (!args[0].isAtom()) {
            throw JasonException.createWrongArgument(this, "The javino illocutionary force parameter ('" + args[0]
                    + "') must be an atom (front, back, left, right or stop)!");
        }
        if (!args[0].toString().equals("front") && !args[0].toString().equals("left") && !args[0].toString().equals(
                "back") && !args[0].toString().equals("right") && !args[0].toString().equals("stop")) {
            throw JasonException.createWrongArgument(this,
                    "There is no '" + args[0].toString() + "' javino illocutionary force!");
        }
    }

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        this.checkArguments(args);

        this.isIlloc = false;
        Term illoc = args[0];

        if (illoc.toString().equalsIgnoreCase("front")) {
            ts.getAg().getLogger().info("Javino: moving " + illoc.toString());
            //ts.getAg().jBridge.sendCommand("COM8", "front");
            System.out.println(ts.getUserAgArch().getPort());
            ts.getUserAgArch().getArgo().sendCommand(ts.getUserAgArch().getPort(), "front");
            //this.javinoBridge.sendmsg("front");
            this.isIlloc = true;
        }

        if (illoc.toString().equalsIgnoreCase("back")) {
            ts.getAg().getLogger().info("Javino: moving " + illoc.toString());
            //ts.getAg().jBridge.sendCommand("COM8", "back");
            ts.getUserAgArch().getArgo().sendCommand(ts.getUserAgArch().getPort(), "back");
            //this.javinoBridge.sendmsg("back");
            this.isIlloc = true;
        }

        if (illoc.toString().equalsIgnoreCase("left")) {
            ts.getAg().getLogger().info("Javino: moving " + illoc.toString());
            //ts.getAg().jBridge.sendCommand("COM8", "left");
            ts.getUserAgArch().getArgo().sendCommand(ts.getUserAgArch().getPort(), "left");
            //this.javinoBridge.sendmsg("left");
            this.isIlloc = true;
        }

        if (illoc.toString().equalsIgnoreCase("right")) {
            ts.getAg().getLogger().info("Javino: moving " + illoc.toString());
            //ts.getAg().jBridge.sendCommand("COM8", "right");
            ts.getUserAgArch().getArgo().sendCommand(ts.getUserAgArch().getPort(), "right");
            //this.javinoBridge.sendmsg("right");
            this.isIlloc = true;
        }

        if (illoc.toString().equalsIgnoreCase("stop")) {
            ts.getAg().getLogger().info("Javino: moving " + illoc.toString());
            //ts.getAg().jBridge.sendCommand("COM8", "stop");
            ts.getUserAgArch().getArgo().sendCommand(ts.getUserAgArch().getPort(), "stop");
            //this.javinoBridge.sendmsg("stop");
			/*this.javinoBridge.sendmsg("request");
			System.out.println(javinoBridge.availablemsg());
			if (javinoBridge.availablemsg()) {
				Literal bel = Literal.parseLiteral("distanceB("
						+ javinoBridge.getmsg() + ")"); // This value has to
														// come from Javino
				bel.addSource(Literal.parseLiteral("percept"));
				ts.getAg().addBel(bel);
			}*/
            this.isIlloc = true;
        }

        return this.isIlloc;
    }
}
