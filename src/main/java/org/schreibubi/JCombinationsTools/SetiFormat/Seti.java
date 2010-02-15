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
package org.schreibubi.JCombinationsTools.SetiFormat;

import org.schreibubi.JCombinationsTools.util.Helper;
import org.schreibubi.visitor.VArrayList;

/**
 * Seti root node
 * 
 * @author Jörg Werner
 * 
 */
public class Seti {

	private String product;

	private String design_step;

	private String revision;

	private String date;

	/** from version 2.00 onwards only */
	private String format;

	/** from version 2.00 onwards only */
	private VArrayList<Client> clients = new VArrayList<Client>();

	private VArrayList<Chain> chains = new VArrayList<Chain>();

	private VArrayList<Variable> variables = new VArrayList<Variable>();

	private VArrayList<Command> commands = new VArrayList<Command>();

	/** not yet used, need extension of the xml-format for that! */
	private VArrayList<Content> contents = new VArrayList<Content>();

	/** from version 2.00 onwards only */
	private VArrayList<PinAssignment> pinassignments = new VArrayList<PinAssignment>();

	/**
	 * @return Returns the chains.
	 */
	public VArrayList<Chain> getChains() {
		return this.chains;
	}

	public VArrayList<Client> getClients() {
		return clients;
	}

	/**
	 * @return Returns the commands.
	 */
	public VArrayList<Command> getCommands() {
		return this.commands;
	}

	/**
	 * @return Returns the contents.
	 */
	public VArrayList<Content> getContents() {
		return this.contents;
	}

	/**
	 * @return Returns the date.
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * @return Returns the design_step.
	 */
	public String getDesign_step() {
		return this.design_step;
	}

	public String getFormat() {
		return format;
	}

	public PinAssignment getPinassignment(String name) {
		for (PinAssignment p : getPinassignments()) {
			if (p.getMode().equals(name)) {
				return p;
			}
		}
		return null;
	}

	public VArrayList<PinAssignment> getPinassignments() {
		return pinassignments;
	}

	/**
	 * @return Returns the product.
	 */
	public String getProduct() {
		return this.product;
	}

	/**
	 * @return Returns the revision.
	 */
	public String getRevision() {
		return this.revision;
	}

	/**
	 * @return Returns the variables.
	 */
	public VArrayList<Variable> getVariables() {
		return this.variables;
	}

	/**
	 * @param chains
	 *            The chains to set.
	 */
	public void setChains(VArrayList<Chain> chains) {
		this.chains = chains;
	}

	public void setClients(VArrayList<Client> clients) {
		this.clients = clients;
	}

	/**
	 * @param commands
	 *            The commands to set.
	 */
	public void setCommands(VArrayList<Command> commands) {
		this.commands = commands;
	}

	/**
	 * @param contents
	 *            The contents to set.
	 */
	public void setContents(VArrayList<Content> contents) {
		this.contents = contents;
	}

	/**
	 * @param date
	 *            The date to set.
	 */
	public void setDate(String date) {
		this.date = Helper.sanitizeString(date);
	}

	/**
	 * @param design_step
	 *            The design_step to set.
	 */
	public void setDesign_step(String design_step) {
		this.design_step = Helper.sanitizeString(design_step);
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setPinassignments(VArrayList<PinAssignment> pinassignments) {
		this.pinassignments = pinassignments;
	}

	/**
	 * @param product
	 *            The product to set.
	 */
	public void setProduct(String product) {
		this.product = Helper.sanitizeString(product);
	}

	/**
	 * @param revision
	 *            The revision to set.
	 */
	public void setRevision(String revision) {
		this.revision = Helper.sanitizeString(revision);
	}

	/**
	 * @param variables
	 *            The variables to set.
	 */
	public void setVariables(VArrayList<Variable> variables) {
		this.variables = variables;
	}

}
