/**
 *  Copyright (c) 2005-2007 Qimonda AG                                                  Company Confidential
 *  PD PT TPE C
 *  --------------------------------------------------------------------------------------------------------
 *  $Rev:108 $
 *  $Date:2007-07-11 11:26:24 +0200 (Mi, 11 Jul 2007) $
 *  $Author:wernerj $
 *  $URL:svn+ssh://scmis001.muc.infineon.com/home/ppe/ppebe/SVNRepository/JAsdap/trunk/src/com/qimonda/pd/pt/tpec/JasdapTools/evalGenCombinations/AlternativesCommonTreeSet.java $
 *  --------------------------------------------------------------------------------------------------------
 *  Signature for 'what': @(#) $$Id:AlternativesCommonTreeSet.java 108 2007-07-11 09:26:24Z wernerj $$
 *  --------------------------------------------------------------------------------------------------------
 *  Engineers in charge: Jörg Werner +49 89 60088 2231
 */
package org.schreibubi.JCombinationsTools.evalGenCombinations;

import org.schreibubi.visitor.VArrayList;


/**
 * Container class, which stores for one Key set the different possible alternatives
 *
 * @author Jörg Werner
 */
public class AlternativesCommonTreeSet<T> {

	private VArrayList<String>				keys;

	private final VArrayList<VArrayList<T>>	valuesets;

	/**
	 * Constructor
	 */
	public AlternativesCommonTreeSet() {
		this.valuesets = new VArrayList<VArrayList<T>>();
	}

	/**
	 * Adds an alternative set of values
	 *
	 * @param valueset
	 */
	public void addAlternative(VArrayList<T> valueset) {
		this.valuesets.add(valueset);
	}

	/**
	 * Gets an alternative set
	 *
	 * @param n
	 *            number of alternative set to get
	 * @return one alternative set
	 */
	public VArrayList<T> getAlternative(int n) {
		return this.valuesets.get(n);
	}

	/**
	 * gets the Keys
	 *
	 * @return keys
	 */
	public VArrayList<String> getKeys() {
		return this.keys;
	}

	/**
	 * Returns the number of alternatives available
	 *
	 * @return number of alternatives available
	 */
	public int getNumberOfAlternatives() {
		return this.valuesets.size();
	}

	/**
	 * sets the Keys
	 *
	 * @param keys
	 */
	public void setKeys(VArrayList<String> keys) {
		this.keys = keys;
	}
}
