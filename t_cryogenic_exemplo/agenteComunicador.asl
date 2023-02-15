/* Initial beliefs and rules */
tielle("d0ab642e-76bc-44fe-b911-468e8f3655da")[source(self)].

/* Initial goals */
!start[source(self)].

/* Plans */
   +!start <- ?tielle(UUID);
   //.connectCN("skynet.chon.group",3273,UUID);
   .connectCN("192.168.0.100",5500,UUID);
   .print("Sou o agente comunicador!").

   +!cryogenic<-
   .print("Acionando o comando de criogenia.");
   .cryogenic;
   .print("Agentes criogenados.");
   .print("Parando SMA");
   .stopMAS.


