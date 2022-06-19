package jason;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ReadFile {

    public static String Read(File file) {
        String conteudo = new String();
        String linha = new String();
        try {
            if (file.exists()) {
                FileReader reader = new FileReader(file);
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
}
