/* Initial beliefs and rules */
souDestinatario.

/* Initial goals */
!start.

/* Plans */

+!start : true <-
    .connectCN("skynet.turing.pro.br", 5500, "788b2b22-baa6-4c61-b1bb-01cff1f5f878");
	.print("Computer, Commander Montgomery Scott, Chief Engineering Office").

+beam_us_up_scotty <-
    .print("Transporter ready!");
    .sendOut("788b2b22-baa6-4c61-b1bb-01cff1f5f879",tell,energizing);
    -beam_us_up_scotty[source(self)].