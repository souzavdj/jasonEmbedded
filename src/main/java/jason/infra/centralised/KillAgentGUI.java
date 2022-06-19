package jason.infra.centralised;

import jason.runtime.RuntimeServicesInfraTier;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;

public class KillAgentGUI extends BaseDialogGUI {

    private static final long serialVersionUID = 1L;

    private JList lAgs;

    private RuntimeServicesInfraTier services;

    public KillAgentGUI(Frame f, String title) {
        super(f, title);
    }

    protected void initComponents() {
        services = RunCentralisedMAS.getRunner().getEnvironmentInfraTier().getRuntimeServices();
        getContentPane().setLayout(new BorderLayout());

        // Fields
        Vector<String> agNames = new Vector<String>(services.getAgentsNames());
        Collections.sort(agNames);
        lAgs = new JList(agNames);
        lAgs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory
                .createTitledBorder(BorderFactory.createEtchedBorder(), "Current agents", TitledBorder.LEFT,
                        TitledBorder.TOP));
        p.add(lAgs, BorderLayout.CENTER);

        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(createButtonsPanel(), BorderLayout.SOUTH);
        ok.setText("Kill");
    }

    protected boolean ok() {
        new Thread() {
            public void run() {
                Object[] sls = lAgs.getSelectedValues();
                for (int i = 0; i < sls.length; i++) {
                    String agName = sls[i].toString();
                    services.killAgent(agName, "KillAgGUI");
                }
            }
        }.start();
        return true;
    }
}
