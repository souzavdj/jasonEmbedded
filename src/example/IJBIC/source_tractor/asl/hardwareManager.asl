// Agent HardwareManager in project sourceTractor

/* Initial beliefs and rules */
!start.

/* 788b2b22-baa6-4c61-b1bb-01cff1f5f878 */

/* "file:///D:/OneDrive/Workspaces/Notebook Dell/Intellij/mast/src/example/IJBIC/source_tractor/source_tractor.mas2j" */

/* Initial goals */

/* Plans */

+!start : true <-
	.print("Sou o gerenciador de hardware.");
	.port(ttyACM0);
	.percepts(open);
	!moveOn.

+!moveOn : true <-
	.act(goAHead);
	.wait(2000);
    .act(goLeft);
    .wait(200);
    !moveOn.
