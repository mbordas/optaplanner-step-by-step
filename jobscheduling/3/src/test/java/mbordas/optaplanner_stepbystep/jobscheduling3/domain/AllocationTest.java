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

package mbordas.optaplanner_stepbystep.jobscheduling3.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AllocationTest {

	@Test
	public void collisions() {
		Task t1 = new Task( 10, 50, 1);
		Task t2 = new Task( 20, 50, 1);
		Task t3 = new Task( 30, 50, 1);

		Allocation a1 = new Allocation(t1, 10, null);

		assertEquals(0, a1.getCollision(0, 10));
		assertEquals(0, a1.getCollision(20, 30));
		assertEquals(10, a1.getCollision(10, 20));
	}

	@Test
	public void getEnd() {
		Task t = new Task(10, 50, 1);
		Worker w = new Worker("W");
		w.addDaysOff(3, 5, 6, 10, 11, 17);
		Allocation a = new Allocation(t, 3, w);

		// Days on that task should be:
		// 4, 7, 8, 9, 12, 13, 14, 15, 16, 18

		assertEquals(19, a.getEnd());

		w.clearDaysOff();
	}
}