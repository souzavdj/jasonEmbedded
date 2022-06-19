// Agente A
souA1.
souA12.
souA13.
souA14.
souA15.
souA16.
souA17.
souA18.
souA19.
souA111.
souA112.
souA113.
souA114.
souA115.
souA116.
souA117.
souA118.
souA119.
souA120.
souA121.

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <-
	.print("Sou o agente A");
	.send(agente_b, tell, crenca_de_a_para_b);
	+crencaA.