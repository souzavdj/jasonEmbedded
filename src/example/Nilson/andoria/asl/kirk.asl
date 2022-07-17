// Agent agenteRemetente in project smaRemetente
souRemetente.

/* Initial beliefs and rules */
!start.

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f879 */


/* Initial goals */

/* Plans */
+!start : souRemetente & energizing <-
    .print("Transporter ready!");
    -energizing[source(scott)];
    +ready;
    !start.

+!start : souRemetente & ready <-
    -souRemetente;
    -ready;
	.moveOut("788b2b22-baa6-4c61-b1bb-01cff1f5f878", inquilinism).
    
+!start : souRemetente <-
    .connectCN("skynet.turing.pro.br", 5500, "788b2b22-baa6-4c61-b1bb-01cff1f5f879");
	.print("Kirk to Scotty...");
	.sendOut("788b2b22-baa6-4c61-b1bb-01cff1f5f878", tell, beam_us_up_scotty);
	.wait(2000);
	!start.
	
+!start : true <-
    .print("Computer, Commander James T. Kirk, Enterprise's Captain");
    .send(spock,tell,chegamos).


