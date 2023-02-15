/* Initial beliefs and rules */
bob.
luminosidade(100).
dia.

/* Initial goals */
!start.

/* Plans */
    +!start: true <-
    .print("Sou o bob.");
    .send(kate,tell,noite).


