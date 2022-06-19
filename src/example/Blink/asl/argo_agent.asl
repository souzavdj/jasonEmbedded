// Agent sample_agent in project blink_test

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- 
	.port(COM3);
	.print("Ligando luz...");
	.act(ligar);
	.wait(1000);
	.print("Desligando luz...");
	.act(desligar);
	.wait(1000);
	!start.
