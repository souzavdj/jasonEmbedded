// Agent agenteDestinatario in project smaDestinatario

/* Initial beliefs and rules */
souDestinatario.

/* Initial goals */

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f878 */

/* "src/example/Nilson/comunicador_destinatario/comunicador_destinatario.mas2j" */

!start.

/* Plans */

+!start : true <-
	.print("Sou o destinatário e vou receber uma mensagem do remetente");
	!helloagent.

+!helloagent: oi <-
    .print("Recebi uma mensagem e vou responder");
    .sendOut("788b2b22-baa6-4c61-b1bb-01cff1f5f879",tell,ola).

-!helloagent <-
	!helloagent.