/* Initial beliefs and rules */
crencaA[source(self)].

/* Initial goals */

/* Plans */
+!start <- .print("Sou o agente A"); .send(agente_b,tell,crenca_de_a_para_b); +crencaA.

