/* Initial beliefs and rules */
bob[source(self)].
bateriafraca[source(kate)].
luminosidade(100)[source(self)].
dia[source(self)].

/* Initial goals */

/* Plans */
+!start <- .print("Sou o bob."); .send(kate,tell,noite).

