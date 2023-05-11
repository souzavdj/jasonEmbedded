// Agent agenteDestinatario in project smaDestinatario

/* Initial beliefs and rules */
souDestinatario.

/* Initial goals */

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f878 */

/* "file:///D:/OneDrive/Workspaces/Notebook Dell/Turing/ultron-protocol/src/example/autolocalizacao/mutualism/comunicador_destinatario/comunicador_destinatario.mas2j" */

!start.

/* Plans */

+!start : true <-
    .connectCN("192.168.1.105", 5500, "788b2b22-baa6-4c61-b1bb-01cff1f5f878");
	.print("Sou o destinatário e vou receber uma mensagem do remetente").
