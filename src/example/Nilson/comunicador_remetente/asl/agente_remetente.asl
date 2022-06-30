// Agent agenteRemetente in project smaRemetente
souRemetente.

/* Initial beliefs and rules */
!start.

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f879 */

/* "src/example/Nilson/comunicador_remetente/comunicador_remetente.mas2j" */

/* Initial goals */

/* Plans */

+!start : true <-
	.print("Sou o remetente e vou enviar uma mensagem ao destinatário.");
	.sendOut("788b2b22-baa6-4c61-b1bb-01cff1f5f878", tell, oi);
	!feedback.

+!feedback : ola <-
    .print("Sou o remetente e recebi uma resposta do destinatário").

-!feedback <-
	!feedback.
