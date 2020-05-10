/*Copyright (c) 2020-2020, Mathieu Bordas
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
3- Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package mbordas.optaplanner_stepbystep.jobscheduling2;

import mbordas.optaplanner_stepbystep.jobscheduling2.domain.Schedule;
import mbordas.optaplanner_stepbystep.jobscheduling2.domain.Task;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JobScheduling {

	static Task addNewTask(Map<Integer, Task> map, int charge, int dueDelay, int latePenalty) {
		Task newTask = new Task(charge, dueDelay, latePenalty);
		map.put(newTask.getId(), newTask);
		return newTask;
	}

	public static void main(String[] args) {
		// Build the Solver
		SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(
				"mbordas/optaplanner_stepbystep/jobscheduling2/score/scheduleCalculatorConfig.xml");
		Solver<Schedule> solver = solverFactory.buildSolver();

		Map<Integer, Task> tasks = new TreeMap<>();

		// Building preliminary tasks with no late penalty
		Task t1 = addNewTask(tasks, 2, 5, 0);
		Task t2 = addNewTask(tasks, 1, 4, 0);

		addNewTask(tasks, 3, 6, 2); // Higher priority
		addNewTask(tasks, 3, 10, 1); // Lower priority

		// Building tasks that depend on preliminary tasks
		Task t3 = addNewTask(tasks, 3,12,2).addDependency(t1);
		addNewTask(tasks, 2,15,1).addDependency(t3,t2);

		Schedule unsolvedSchedule = new Schedule(new ArrayList<>(tasks.values()));
		unsolvedSchedule.init();

		// Solve the problem
		Schedule solvedSchedule = solver.solve(unsolvedSchedule);

		// Display the result
		System.out.println("\nSolution:\n" + solvedSchedule.toString());
	}
}