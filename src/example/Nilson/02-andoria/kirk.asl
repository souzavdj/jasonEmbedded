// Agent agenteRemetente in project smaRemetente
abroad.

/* Initial beliefs and rules */
!start.

/* Initial goals */

/* Plans */
    
+!start : abroad <-
	.print("Kirk to Scotty...");
	.connectCN("skynet.turing.pro.br",5500,"41ff1712-b2f0-416d-8232-fef834651e77");
	-abroad;
	.sendOut("07ba9e4a-d539-4a0e-8c14-4ac336476858", tell, beam_us_up_scotty);
	.wait(2000);
	!start.
	
+!start : true <-
    .print("Computer, Commander James T. Kirk, Enterprise's Captain").


