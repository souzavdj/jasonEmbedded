// Agent agenteRemetente in project smaRemetente
souRemetente.

/* Initial beliefs and rules */
!start.

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f879 */

/* "file:///D:/OneDrive/Workspaces/Notebook Dell/Turing/ultron-protocol/src/example/autolocalizacao/mutualism/comunicador_remetente/comunicador_remetente.mas2j" */

/* Initial goals */

/* Plans */

+!start : true <-
	.connectCN("192.168.0.100", 5500, "788b2b22-baa6-4c61-b1bb-01cff1f5f879");
	.print("Sou o remetente e vou enviar uma mensagem ao destinatário.");
	.moveOut("788b2b22-baa6-4c61-b1bb-01cff1f5f878", mutualism).
