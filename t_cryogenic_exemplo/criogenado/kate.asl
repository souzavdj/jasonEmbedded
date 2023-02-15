/* Initial beliefs and rules */
bateria(10)[source(self)].
kate[source(self)].
noite[source(bob)].

/* Initial goals */

/* Plans */
+!start <- .print("Sou a kate."); .wait(1000); .send(bob,tell,bateriafraca); .print("Precisamos criogenar"); .send(agenteComunicador,achieve,cryogenic).

