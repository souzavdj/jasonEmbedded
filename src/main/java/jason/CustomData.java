package jason;

import java.io.*;

public class CustomData implements Serializable {

    private static final long serialVersionUID = 6093226637618022646L;

    private File agent;

    private String pathDir;

    private String[] namesAgents;

    public CustomData() {
    }

    public CustomData(File agent) {
        this.agent = agent;
    }

    public CustomData(String pathDir) {
        this.pathDir = pathDir;
    }

    public String[] getNamesAgents() {
        return namesAgents;
    }

    public void setNamesAgents(String[] namesAgents) {
        this.namesAgents = namesAgents;
    }

    public File getAgent() {
        return agent;
    }

    public void setAgent(File agent) {
        this.agent = agent;
    }

    public String getPathDir() {
        return pathDir;
    }

    public void setPathDir(String pathDir) {
        this.pathDir = pathDir;
    }

    public File[] getAgentsPath() {
        return new File(pathDir).listFiles();
    }

    public String readSourceAgent(File agent) {
        String conteudo = new String();
        String linha = new String();
        try {
            if (agent.exists()) {
                FileReader reader = new FileReader(agent);
                BufferedReader leitor = new BufferedReader(reader);
                while (leitor.ready()) {
                    linha = leitor.readLine();
                    conteudo += linha + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conteudo;
    }

    public String concatNamesSourcesAgents(File[] agents) {
        String concat = "";
        this.namesAgents = new String[agents.length];
        for (int i = 0; i < agents.length; i++) {
            String name = agents[i].getName() + "N#";
            this.namesAgents[i] = agents[i].getName();
            String source = this.readSourceAgent(agents[i]) + "S#";
            concat = concat + name + source + "A#";
        }
        return concat;
    }

    public void receiveConcatNamesSourcesAgents(String concat) {
        String[] agentsString = concat.split("A#");
        String[] namesAgents = new String[agentsString.length];
        String[] sourcesAgents = new String[agentsString.length];
        for (int i = 0; i < agentsString.length; i++) {
            namesAgents[i] = agentsString[i].split("N#")[0];
            sourcesAgents[i] = agentsString[i].split("S#")[0];
            sourcesAgents[i] = sourcesAgents[i].substring(namesAgents[i].length() + 2);
            createASL(sourcesAgents[i], namesAgents[i]);
        }
        this.namesAgents = namesAgents;
    }

    public void killAgents(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                files[i].delete();
            }
        } else {
            dir.delete();
        }
    }

    public void createASL(String source, String name) {
        try {
            FileWriter geral = new FileWriter("src/asl/" + name);//mudar endere�o
            PrintWriter escreve = new PrintWriter(geral);
            escreve.println(source);
            escreve.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String firstPart = "Path diretorio: " + this.pathDir + "\n";
        String secondPart = "\nfile Name: " + agent.getName();
        String thirdPart = "\nfile Caminho: " + agent.getPath();
        return firstPart + secondPart + thirdPart;
    }
}