package jason.util;

import jason.asSyntax.Literal;
import jason.bb.BeliefBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeliefUtils {

    public static final String SERVER_ADDRESS_BELIEF_PREFIX = "serverAddress";

    public static final String SERVER_PORT_BELIEF_PREFIX = "serverPort";

    public static final String MY_UUID_BELIEF_PREFIX = "myUUID";

    public static final String MY_MAS_BELIEF_PREFIX = "myMAS";

    public static final String VALUE_REPLACEMENT = "#";

    private static final String BELIEF_VALUE = "(\""+VALUE_REPLACEMENT+"\")";

    public static String SERVER_ADDRESS_BELIEF_VALUE = SERVER_ADDRESS_BELIEF_PREFIX + BELIEF_VALUE;

    public static String SERVER_PORT_BELIEF_VALUE = SERVER_PORT_BELIEF_PREFIX + BELIEF_VALUE;

    public static String MY_UUID_BELIEF_VALUE = MY_UUID_BELIEF_PREFIX + BELIEF_VALUE;

    public static String MY_MAS_BELIEF_VALUE = MY_MAS_BELIEF_PREFIX + BELIEF_VALUE;

    public static List<String> getBeliefByStartWith(BeliefBase beliefBase, String startAt) {
        List<String> beliefs = new ArrayList<>();
        Iterator<Literal> beliefsIterator = beliefBase.iterator();
        while (beliefsIterator.hasNext()) {
            Literal literal = beliefsIterator.next();
            String belief = literal.toString();
            if (belief.startsWith(startAt)){
                beliefs.add(belief);
            }
        }
        return beliefs;
    }

    public static String getBeliefValue(List<String> beliefList, String source) {
        String correctBelief = "";
        if (beliefList != null && !beliefList.isEmpty()) {
            for (String belief : beliefList) {
                if (belief.contains(source)) {
                    correctBelief = belief;
                    break;
                }
            }
            if (!correctBelief.isEmpty()) {
                int initialIndex = correctBelief.indexOf("(");
                int finalIndex = correctBelief.indexOf(")");
                if (initialIndex != -1 && finalIndex != -1) {
                    correctBelief = correctBelief.substring(initialIndex + 1, finalIndex);
                }
            }
        }
        return correctBelief;
    }

}
