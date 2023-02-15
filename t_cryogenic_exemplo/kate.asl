/* Initial beliefs and rules */
kate.
bateria(10).

/* Initial goals */
!start.

/* Plans */
    +!start: true <-
    .print("Sou a kate.");
    .wait(1000);
    //.connectCN("skynet.chon.group",3273,UUID);
    //.connectCN("192.168.43.3",5500,UUID);
    .send(bob,tell,bateriafraca);
    .print("Precisamos criogenar");
    .send(agenteComunicador,achieve,cryogenic).


