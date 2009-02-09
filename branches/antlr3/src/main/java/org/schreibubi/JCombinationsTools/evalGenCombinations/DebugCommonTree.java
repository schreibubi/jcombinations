/** Copyright (c) 2005-2007 Qimonda AG                                                  Company Confidential
 * PD PT TPE C
 * --------------------------------------------------------------------------------------------------------
 * $Rev: $
 * $Date: $
 * $Author: $
 * $URL: $
 * --------------------------------------------------------------------------------------------------------
 * Signature for 'what': @(#) $Id: $
 * --------------------------------------------------------------------------------------------------------
 * Engineers in charge: Jörg Werner +49 89 60088 2231
 */

package org.schreibubi.JCombinationsTools.evalGenCombinations;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 * @author Jörg Werner
 *
 */
public class DebugCommonTree extends CommonTree {

	int		alts	= -1;
	int		cumAlts	= 0;
	String	lhsName	= null;

	/**
	 *
	 */
	public DebugCommonTree() {
		super();
	}

	/**
	 * @param arg0
	 */
	public DebugCommonTree(CommonTree arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public DebugCommonTree(Token arg0) {
		super(arg0);
	}

	public int getAlts() {
		return alts;
	}

	public int getCumAlts() {
		return cumAlts;
	}

	public String getLhsName() {
		return lhsName;
	}

	public void setAlts(int alts) {
		this.alts = alts;
	}

	public void setCumAlts(int cumAlts) {
		this.cumAlts = cumAlts;
	}

	public void setLhsName(String lhsName) {
		this.lhsName = lhsName;
	}

	@Override
	public String toString() {
		if (isNil())
			return "nil";
		return token.getText(); // + "<a:" + getAlts() + ",c:" + getCumAlts() +
		// ",l:" + getLhsName() + ">"; // + "
	}

}
