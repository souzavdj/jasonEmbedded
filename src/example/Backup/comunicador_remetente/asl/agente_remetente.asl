// Agent agenteRemetente in project smaRemetente
souRemetente.

/* Initial beliefs and rules */
!start.

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f879 */

/* "file:///D:/OneDrive/Workspaces/Notebook Dell/Intellij/mast/src/example/comunicador_remetente/comunicador_remetente.mas2j" */

/* Initial goals */

/* Plans */

+!start : true <-
	.print("Sou o remetente e vou enviar uma mensagem ao destinatário.");
	.sendOut("788b2b22-baa6-4c61-b1bb-01cff1f5f878", tell, oi);
	.moveOut("788b2b22-baa6-4c61-b1bb-01cff1f5f878", predator);.

/*+!ola : true <-
    .print("Sou o remetente e recebi uma resposta do destinatário").

*/