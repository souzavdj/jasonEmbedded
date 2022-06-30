/* Initial beliefs and rules */
initialBelief(true).

/* Initial goals */
!start.

/* Plans */
+!start : initialBelief(true) <-
	.print("Hello agent").