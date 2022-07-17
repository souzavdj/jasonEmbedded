// Agente A
/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

    
+!start: chegamos <-
    .print("Computer, Lieutenant Commander Spock, First Officer");
    .port(ttyACM0);
    .percepts(open);
    .print("ligando led");
    .act(turnOnLight);
    .wait(3000);
    .print("desligando led");
    .act(turnOffLight);
    .wait(5000);
    !start.
    
+!start <-
    .print("Aguardando transporte");
    .wait(1000);
    !start.

