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
package org.schreibubi.JCombinationsTools.evalGenCombinations;

import java.util.ListIterator;

import org.schreibubi.visitor.VArrayList;

/**
 * Class which helps generating all possible combinations. It needs to know for
 * each possible position the number of possibilities. It iterates through all
 * possible combinations by calling getNextCombination
 * 
 * @author Jörg Werner
 * 
 */
public class Combination {

	VArrayList<Integer> possibilities = new VArrayList<Integer>();

	VArrayList<Integer> alternatives = null;

	int totalPossibilities = 0;

	int alt = 0;

	/**
	 * Constructor
	 * 
	 * @param alternatives
	 *            Array which contains how many possibilities exist for each
	 *            position
	 */
	public Combination(VArrayList<Integer> alternatives) {

		int previous = 1;

		for (ListIterator<Integer> i = alternatives.listIterator(alternatives
				.size()); i.hasPrevious();) {
			int alt = i.previous();
			this.possibilities.add(0, previous);
			previous = previous * alt;

		}
		this.alternatives = alternatives;
		this.totalPossibilities = previous;
	}

	/**
	 * @return
	 */
	public VArrayList<Boolean> getAlternativesWhichWillChange() {
		return findAltsWhichWillFlip(this.alt);
	}

	/**
	 * Returns which positions have changed for the last call to
	 * getNextCombination()
	 * 
	 * @return positions which have changed
	 */
	public VArrayList<Boolean> getChangedAlternatives() {
		return findAltsWhichWillFlip(this.alt - 1);
	}

	/**
	 * Returns next possible combination
	 * 
	 * @return next possible combination
	 */
	public VArrayList<Integer> getNextCombination() {
		VArrayList<Integer> combinations = peekNextCombination();
		this.alt++;
		return combinations;
	}

	/**
	 * Returns total number of possibilities
	 * 
	 * @return total number of possibilities
	 */
	public int getTotalPossibilities() {
		return this.totalPossibilities;
	}

	/**
	 * Has next combination?
	 * 
	 * @return true: there are more combinations
	 */
	public boolean hasNext() {
		return this.alt < this.totalPossibilities;
	}

	/**
	 * Returns next possible combination
	 * 
	 * @return next possible combination
	 */
	public VArrayList<Integer> peekNextCombination() {
		VArrayList<Integer> combinations = new VArrayList<Integer>();
		int altb = this.alt;
		for (int j = 0; j < this.possibilities.size(); j++) {
			combinations.add(altb / this.possibilities.get(j));
			altb = altb % this.possibilities.get(j);
		}
		return combinations;
	}

	/**
	 * Go back to first combination
	 */
	public void reset() {
		this.alt = 0;
	}

	/**
	 * @param flipped
	 * @param altb
	 * @return
	 */
	private VArrayList<Boolean> findAltsWhichWillFlip(int altb) {
		VArrayList<Boolean> flipped = new VArrayList<Boolean>();
		for (int j = 0; j < this.possibilities.size(); j++) {
			altb = altb % this.possibilities.get(j);
			if ((altb == 0) && (this.alternatives.get(j) > 1)) {
				flipped.add(true);
			} else {
				flipped.add(false);
			}
		}
		return flipped;
	}
}
