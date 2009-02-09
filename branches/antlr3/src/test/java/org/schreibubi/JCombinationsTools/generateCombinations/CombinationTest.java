/**
 * Copyright (C) 2009 Jörg Werner schreibubi@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.schreibubi.JCombinationsTools.generateCombinations;

import java.util.ArrayList;
import java.util.Collections;

import org.schreibubi.JCombinationsTools.evalGenCombinations.Combination;
import org.schreibubi.visitor.VArrayList;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * Unit test for combinations class
 * 
 * @author Jörg Werner
 * 
 */
public class CombinationTest {

	private final VArrayList<Integer>	possibilities	= new VArrayList<Integer>();

	/**
	 * test getChangedAlternatives
	 */
	@Test()
	public void testGetChangedAlternatives() {
		Combination c = new Combination(this.possibilities);
		ArrayList<Integer> previousCombination = c.getNextCombination();
		for (int i = 0; i < 89; i++) {
			ArrayList<Integer> currentCombination = c.getNextCombination();
			ArrayList<Boolean> flipped = c.getChangedAlternatives();
			for (int j = 0; j < flipped.size(); j++) {
				assert flipped.get(j) == (currentCombination.get(j) != previousCombination.get(j));
			}
			previousCombination = currentCombination;
		}
	}

	/**
	 * test method for 'org.schreibubi.visitor.generateCombinations.Combination.getNextCombination()'
	 */
	@Test()
	public void testGetNextCombination() {
		Combination c = new Combination(this.possibilities);
		ArrayList<Integer> comb = null;
		for (int i = 0; i < 15; i++) {
			comb = c.getNextCombination();
		}
		ArrayList<Integer> compare = new ArrayList<Integer>();
		Collections.addAll(compare, 0, 2, 0, 2);
		for (int i = 0; i < compare.size(); i++) {
			assert compare.get(i) == comb.get(i);
		}
	}

	/**
	 * test method for 'org.schreibubi.visitor.generateCombinations.Combination.getTotalPossibilities()'
	 */
	@Test()
	public void testGetTotalPossibilities() {
		Combination c = new Combination(this.possibilities);
		assert c.getTotalPossibilities() == 90;
	}

	/**
	 * test reset method
	 */
	@Test()
	public void testReset() {
		Combination c = new Combination(this.possibilities);
		ArrayList<Integer> comb = null;
		for (int i = 0; i < 55; i++) {
			comb = c.getNextCombination();
		}
		c.reset();
		for (int i = 0; i < 15; i++) {
			comb = c.getNextCombination();
		}
		ArrayList<Integer> compare = new ArrayList<Integer>();
		Collections.addAll(compare, 0, 2, 0, 2);
		for (int i = 0; i < compare.size(); i++) {
			assert compare.get(i) == comb.get(i);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@BeforeTest
	protected void setUp() {
		Collections.addAll(this.possibilities, 5, 3, 1, 6);
	}

}
