# Job scheduling step 3

## Goals

* Worker have holidays.
* Some tasks are locked and can't be moved.

## Solving the problem

### Model

* Worker class now has 'daysOff' list.
* The list of locked allocations is added as a 'ProblemFactCollectionProperty' of the Schedule class.


### Drools scheduleCalculatorRules.drl

* The 'duration' attribute of class Task should not be used to compute the end of a scheduled task, because it does not take the days off into account, so:
  * The method Task.getDuration() is removed.
  * The rule 'simultaneousTasks' is updated, now it calls Allocation.getEnd() for collision detection.
  * The rule 'delayPenality' is updated too.