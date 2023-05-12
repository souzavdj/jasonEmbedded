// Agente B

/* Initial beliefs and rules */
crencaB.
/* Initial goals */

!start.

/* Plans */

+!start : true <-
	.print("Sou o agente B");
    .wait(500);
    !start.