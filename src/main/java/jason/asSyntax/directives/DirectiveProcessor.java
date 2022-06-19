package jason.asSyntax.directives;

import jason.asSemantics.Agent;
import jason.asSyntax.Pred;
import jason.asSyntax.patterns.goal.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class maintains the set of directives and is used by the
 * parser to process them.
 * <p>
 * All available directives must be registered in this class using the
 * addDirective method.
 *
 * @author jomi
 */
public class DirectiveProcessor {

    static Logger logger = Logger.getLogger(DirectiveProcessor.class.getName());

    private static Map<String, Directive> directives = new HashMap<String, Directive>();

    public static void addDirective(String id, Directive d) {
        directives.put(id, d);
    }

    public static Directive getDirective(String id) {
        return directives.get(id);
    }

    public static Directive removeDirective(String id) {
        return directives.remove(id);
    }

    // add known directives
    static {
        addDirective("include", new Include());
        addDirective("register_function", new FunctionRegister());

        addDirective("dg", new DG());
        addDirective("bdg", new BDG());
        addDirective("ebdg", new EBDG());
        addDirective("bc", new BC());
        addDirective("smc", new SMC());
        addDirective("rc", new RC());
        addDirective("omc", new OMC());
        addDirective("mg", new MG());
        addDirective("sga", new SGA());
    }

    public static Agent process(Pred directive, Agent outerAg, Agent innerAg) {
        try {
            logger.fine("Processing directive " + directive);
            Directive d = directives.get(directive.getFunctor());
            if (d != null) {
                return d.process(directive, outerAg, innerAg);
            } else {
                logger.log(Level.SEVERE, "Unknown directive " + directive);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing directive " + directive, e);
        }
        return null;
    }

}
