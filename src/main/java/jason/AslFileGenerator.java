package jason;

import jason.architecture.AgArch;
import jason.asSemantics.*;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gerador de arquivo .asl do jason para um agente em tempo de execução.
 */
public class AslFileGenerator {

    /** Extensão de arquivo asl. */
    public static final String ASL_EXTENSION = ".asl";

    /** Pulo de linha. */
    private static final String NEXT_LINE = "\n";

    /** Símbolo de término de comando. */
    private static final String END_SYMBOL = ".";

    /** Símbolo que fica no início das declarações de objetivo. */
    private static final String INITIAL_GOALS_SYMBOL = "!";

    /** Regex para encontrar o início das declarações de plano. */
    private static final String INITIAL_PLANS_SYMBOL_REGEX = "\\+!";

    /** Símbolo de início das declarações de planos. */
    private static final String INITIAL_PLANS_SYMBOL = "+!";

    /** Regex para encontrar o início das declarações de plano contingência. */
    private static final String INITIAL_CONTINGENCY_PLANS_SYMBOL_REGEX = "-!";

    /** Símbolo de início das declarações de planos contingências. */
    private static final String INITIAL_CONTINGENCY_PLANS_SYMBOL = "-!";

    /** Texto inicial para localizar planos padrão de kqml. */
    private static final String KQML_PREFIX = "@kqml";

    /**
     * Gera o conteúdo de um arquivo asl e encapsula no modelo serializável para ser transferido via contextNet.
     *
     * @return Modelo de transferência de agente.
     */
    public AslTransferenceModel generateAslContent(AgArch agArch) {
        Agent agent = agArch.getTS().getAg();

        StringBuilder content = new StringBuilder();
        content.append(generateInitialBeliefs(agent) + NEXT_LINE);
        content.append(generateInitialGoals(agent) + NEXT_LINE);
        content.append(generatePlans(agent) + NEXT_LINE);

        AslTransferenceModel aslTransferenceModel = new AslTransferenceModel(agArch.getAgName(),
                content.toString().getBytes(), agArch.getClass().getName());
        return aslTransferenceModel;
    }

    /**
     * Cria um novo arquivo .asl no endereço passado, de acordo com o nome e o conteúdo passado no modelo de
     * transferência de agente.
     *
     * @param path Caminho da pasta.
     * @param aslTransferenceModel Modelo de transferência de agente.
     */
    public void createAslFile(String path, AslTransferenceModel aslTransferenceModel) {
        path = path.replaceAll("\\\\", "/");
        if (path.charAt(path.length() -1) != '/') {
            path += "/";
        }
        File file =
                new File(path + aslTransferenceModel.getName() + ASL_EXTENSION);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(aslTransferenceModel.getFileContent());
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Captura as crenças do agente em tempo de execução.
     *
     * @param agent Agente.
     * @return Declaração em String das crença do agente em tempo de execução.
     */
    private String generateInitialBeliefs(Agent agent) {
        StringBuilder beliefs = new StringBuilder();
        beliefs.append("/* Initial beliefs and rules */" + NEXT_LINE);

        Iterator<Literal> beliefsIterator = agent.getBB().iterator();

        while (beliefsIterator.hasNext()) {
            Literal literal = beliefsIterator.next();
            beliefs.append(literal.toString() + END_SYMBOL + NEXT_LINE);
        }

        return beliefs.toString();
    }

    private String getIntentionName(Intention intention) {
        String intentionNameAndImplementation = intention.peek().toString();
        String[] intentionNameAndImplementationSplit = intentionNameAndImplementation.split("<-");
        String intentionName = intentionNameAndImplementationSplit[0];
        intentionName = intentionName.replaceAll("!", "");
        intentionName = intentionName.replaceAll("\\+", "");
        intentionName = intentionName.replaceAll("-", "");
        if (intentionName.contains(" ")) {
            intentionName = intentionName.replaceAll(" ", "");
        }
        return intentionName;
    }

    /**
     * Captura os objetivos iniciais do agente em tempo de execução.
     *
     * @param agent Agente.
     * @return Texto com os objetivos iniciais do agente em tempo de execução.
     */
    private String generateInitialGoals(Agent agent) {
        StringBuilder initialGoals = new StringBuilder();
        initialGoals.append("/* Initial goals */" + NEXT_LINE);
        List<String> intentionsNames = new LinkedList<>();

        Circumstance circumstance = agent.getTS().getC();
        Queue<Intention> intentions = circumstance.getIntentions();
        // recupera as intenções atuais.
        for (Intention intention : intentions) {
            String intentionName1 = getIntentionName(intention);
            if (!intentionsNames.contains(intentionName1)) {
                intentionsNames.add(intentionName1);
            }
        }
        // recupera a intenção selecionada para ser executada no proximo ciclo.
        Intention selectedIntention = circumstance.getSelectedIntention();
        if (selectedIntention != null) {
            String selectedIntentionName = getIntentionName(selectedIntention);
            if (!intentionsNames.contains(selectedIntentionName)) {
                intentionsNames.add(selectedIntentionName);
            }
        }
        // recupera as intenções que estão pendentes para serem executadas em algum momento em ordem.
        Map<String, Intention> pendingIntentions = circumstance.getPendingIntentions();
        if (pendingIntentions != null && !pendingIntentions.isEmpty()) {
            List<Integer> intentionsNumberList = new ArrayList<>();
            for (String key : pendingIntentions.keySet()) {
                String[] intentionNumberSplit = key.split("\\/");
                int intentionNumber = Integer.parseInt(intentionNumberSplit[0]);
                intentionsNumberList.add(intentionNumber);
            }
            intentionsNumberList = intentionsNumberList.stream().sorted().collect(Collectors.toList());
            for (Integer intentionNumber : intentionsNumberList) {
                for (String key : pendingIntentions.keySet()) {
                    if (key.startsWith(intentionNumber+"/")) {
                        String intentionName = getIntentionName(pendingIntentions.get(key));
                        if (!intentionsNames.contains(intentionName)) {
                            intentionsNames.add(intentionName);
                        }
                        break;
                    }
                }
            }
        }
        for (String intentionsName : intentionsNames) {
            initialGoals.append(INITIAL_GOALS_SYMBOL + intentionsName + END_SYMBOL + NEXT_LINE);
        }

        return initialGoals.toString();
    }

    /**
     * Captura os planos do agente.
     *
     * @param agent Agente.
     * @return Texto com os planos do agente.
     */
    private String generatePlans(Agent agent) {
        StringBuilder plains = new StringBuilder();
        plains.append("/* Plans */" + NEXT_LINE);

        for (Plan plan : agent.getPL().getPlans()) {
            String p = plan.toASString();
            if (!p.startsWith(KQML_PREFIX)) {
                String[] planName = p.split(INITIAL_PLANS_SYMBOL_REGEX);
                if (planName.length > 1) {
                    plains.append(INITIAL_PLANS_SYMBOL + planName[1] + NEXT_LINE);
                }

                String[] contingencyPlanName = p.split(INITIAL_CONTINGENCY_PLANS_SYMBOL_REGEX);
                if (contingencyPlanName.length > 1) {
                    plains.append(INITIAL_CONTINGENCY_PLANS_SYMBOL + contingencyPlanName[1] + NEXT_LINE);
                }
            }
        }

        return plains.toString();
    }

}
