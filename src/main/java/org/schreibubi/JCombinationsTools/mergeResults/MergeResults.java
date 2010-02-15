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
package org.schreibubi.JCombinationsTools.mergeResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.Xdata;
import org.schreibubi.JCombinations.FileFormat.Ydata;
import org.schreibubi.JCombinationsTools.evalGenCombinations.AlternativesASTSet;
import org.schreibubi.JCombinationsTools.evalGenCombinations.Combination;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinationsLexer;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinationsParser;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinationsTreeWalker;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.symbol.SymbolVisitorPrint;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;
import org.schreibubi.visitor.VLinkedHashMap;

import antlr.collections.AST;

/**
 * Merge results from dclog-file with the data of the combinations files.
 * 
 * @author Jörg Werner
 * 
 */
public class MergeResults {

	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		try {

			Properties defaultProperties = new Properties();
			Properties userProperties = new Properties(defaultProperties);

			defaultProperties.setProperty("triggerTrimColumn", "TRIM");
			defaultProperties.setProperty("triggerMeasureColumn", "MEASURE");
			defaultProperties
					.setProperty("trimColumns",
							"TRIM_TESTMODE,TRIM_TRIMMING,TRIM_RELATIVE,TRIM_ABS,TRIM_PATNAME,FORCE_VALUE");
			defaultProperties.setProperty("trimAbsColumn", "TRIM_ABS");
			defaultProperties
					.setProperty("trimRelativeColumn", "TRIM_RELATIVE");
			defaultProperties.setProperty("subtitleColumn", "COMMENT");
			defaultProperties.setProperty("dutSelection", "1-1024");
			defaultProperties.setProperty("dutOffset", "32");
			defaultProperties.setProperty("zip", "false");

			try {
				userProperties.loadFromXML(new FileInputStream("config.xml"));
			} catch (FileNotFoundException e) {
			}

			String dclogFile = "";
			ArrayList<String> dclogFiles = new ArrayList<String>();
			String outputFile = "";
			String combinationsFile = "";
			String triggerTrimColumn = userProperties
					.getProperty("triggerTrimColumn");
			String triggerMeasureColumn = userProperties
					.getProperty("triggerMeasureColumn");
			String trimColumnsString = userProperties
					.getProperty("trimColumns");
			String trimAbsColumn = userProperties.getProperty("trimAbsColumn");
			String trimRelativeColumn = userProperties
					.getProperty("trimRelativeColumn");
			String subtitleColumn = userProperties
					.getProperty("subtitleColumn");
			String dutSelectionString = userProperties
					.getProperty("dutSelection");
			String dutOffsetString = userProperties.getProperty("dutOffset");
			boolean zip = Boolean.parseBoolean(userProperties
					.getProperty("zip"));

			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.withLongOpt("dclog")
					.withDescription("[in] dclog-files").hasArg().withArgName(
							"file").create('d'));
			options.addOption(OptionBuilder.isRequired().withLongOpt(
					"combinations").withDescription("[in] combinations file")
					.hasArg().withArgName("file").create('c'));
			options.addOption(OptionBuilder.withLongOpt("output")
					.withDescription("[out] output file").hasArg().withArgName(
							"file").create('o'));
			options.addOption(OptionBuilder.withLongOpt("trim")
					.withDescription(
							"Column name of trim column, defaults to "
									+ triggerTrimColumn).hasArg().withArgName(
							"column").create('t'));
			options.addOption(OptionBuilder.withLongOpt("measure")
					.withDescription(
							"Column name of measure column, defaults to "
									+ triggerMeasureColumn).hasArg()
					.withArgName("column").create('m'));
			options.addOption(OptionBuilder.withLongOpt("xvals")
					.withDescription(
							"Column name containing the x-values, defaults to "
									+ trimColumnsString).hasArg().withArgName(
							"column").create('x'));
			options.addOption(OptionBuilder.withLongOpt("relative")
					.withDescription(
							"Column name containing the relative changes, defaults to "
									+ trimRelativeColumn).hasArg().withArgName(
							"column").create('r'));
			options.addOption(OptionBuilder.withLongOpt("subtitle")
					.withDescription(
							"Column name containing the subtitles for the shmoos, defaults to "
									+ subtitleColumn).hasArg().withArgName(
							"column").create('s'));
			options.addOption(OptionBuilder.withLongOpt("zip").withDescription(
					"zip output file").create('z'));
			options.addOption(OptionBuilder.withLongOpt("dut").withDescription(
					"use only DUTs " + dutSelectionString).hasArg()
					.withArgName("column").create('u'));
			options.addOption(OptionBuilder.withLongOpt("dutOffset")
					.withDescription(
							"dut offset when merging multiple measurments "
									+ dutOffsetString).hasArg().withArgName(
							"offset").create('f'));
			options.addOption(OptionBuilder.withLongOpt("version")
					.withDescription("version").create('v'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("v")) {
				Info.printVersion("MergeResults");
				Runtime.getRuntime().exit(0);
			}
			if (line.hasOption("c")) {
				combinationsFile = line.getOptionValue("c");
			}

			if (line.hasOption("d")) {
				dclogFile = line.getOptionValue("d");
				String[] dF = dclogFile.split(",");
				for (String s : dF) {
					dclogFiles.add(s);
				}
			} else {
				int dotPos = combinationsFile.lastIndexOf(".");
				if (dotPos > -1) {
					dclogFiles.add("dclog_af_b_source.csv_"
							+ combinationsFile.substring(0, dotPos));
				} else {
					dclogFiles.add("dclog_af_b_source.csv_" + combinationsFile);
				}
			}

			if (line.hasOption("o")) {
				outputFile = line.getOptionValue("o");
			} else {
				int dotPos = combinationsFile.lastIndexOf(".");
				if (dotPos > -1) {
					outputFile = combinationsFile.substring(0, dotPos) + ".xml";
				} else {
					outputFile = combinationsFile + ".xml";
				}
			}

			if (line.hasOption("t")) {
				triggerTrimColumn = line.getOptionValue("t");
			}
			if (line.hasOption("m")) {
				triggerMeasureColumn = line.getOptionValue("m");
			}
			if (line.hasOption("x")) {
				trimColumnsString = line.getOptionValue("x");
			}
			if (line.hasOption("r")) {
				trimRelativeColumn = line.getOptionValue("r");
			}
			if (line.hasOption("s")) {
				subtitleColumn = line.getOptionValue("s");
			}
			if (line.hasOption("z")) {
				zip = true;
			}
			if (line.hasOption("u")) {
				dutSelectionString = line.getOptionValue("u");
			}
			if (line.hasOption("f")) {
				dutOffsetString = line.getOptionValue("f");
			}

			String[] tC = trimColumnsString.split(",");
			VArrayList<String> trimColumns = new VArrayList<String>();
			for (String s : tC) {
				trimColumns.add(s);
			}

			String[] dS = dutSelectionString.split("-");
			if (dS.length < 2) {
				throw new Exception(
						"dut specification is wrong, should be e.g. 1-16");
			}
			int dutStart = Integer.parseInt(dS[0]);
			int dutStop = Integer.parseInt(dS[1]);

			int dutOffset = Integer.parseInt(dutOffsetString);
			merge(dclogFiles, combinationsFile, outputFile, zip,
					triggerTrimColumn, trimRelativeColumn, trimAbsColumn,
					trimColumns, triggerMeasureColumn, subtitleColumn,
					dutStart, dutStop, dutOffset);

		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("MergeResults"), options);
		} catch (Exception e) {
			System.out.println("MergeResults error: " + e);
			e.printStackTrace();
		}

	}

	private static boolean altContainsSymbol(VLinkedHashMap<Symbol> symbolList,
			String symbol) throws Exception {
		if (symbolList.get(symbol) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * check if parent already has a child whith our name
	 * 
	 * @param parent
	 *            Parent to use
	 * @param name
	 *            name of child
	 * @return child, null if not found
	 */
	private static OurTreeNode findChildWithName(OurTreeNode parent, String name) {
		OurTreeNode child = null;
		for (int k = 0; k < parent.getChildCount(); k++) {
			child = (OurTreeNode) parent.getChildAt(k);
			if (child.getName().equals(name)) {
				break;
			}
			child = null;
		}
		return child;
	}

	private static String generateAlternativeName(
			VLinkedHashMap<Symbol> symbolList) throws Exception {
		Iterator<Symbol> i = symbolList.values().iterator();
		Symbol s = i.next();
		if (s != null) {
			return s.toString();
		} else {
			return "";
		}
	}

	private static String generateName(VLinkedHashMap<Symbol> symbolList)
			throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		SymbolVisitorPrint symbolVisitorPrint = new SymbolVisitorPrint(pw);
		for (Iterator<Symbol> i = symbolList.values().iterator(); i.hasNext();) {
			Symbol s = i.next();
			s.accept(symbolVisitorPrint);
			if (i.hasNext()) {
				pw.print(" ");
			}
		}
		return sw.toString();
	}

	private static String getSymbolValueString(
			VArrayList<VLinkedHashMap<Symbol>> symbolList, String symbol) {
		for (VLinkedHashMap<Symbol> map : symbolList) {
			Symbol sym = map.get(symbol);
			if (sym != null) {
				return sym.getValueString();
			}
		}
		return "";
	}

	/**
	 * @param dclogFiles
	 *            dclog Filename
	 * @param combinations
	 *            Combinations Filename
	 * @param output
	 *            Output Filename
	 * @param zip
	 *            Zip output file?
	 * @param triggerTrimColumn
	 *            Column name of trim column
	 * @param trimRelativeColumn
	 *            Column name of relative trimming column
	 * @param trimAbsColumn
	 *            Column name of absolute trimming column
	 * @param trimColumns
	 *            Column name of xvals column
	 * @param triggerMeasureColumn
	 *            Column name of measure column
	 * @param subtitleColumn
	 *            Column name of the subtitle column
	 * @param dutStart
	 *            start dut
	 * @param dutStop
	 *            stop dut
	 * @throws Exception
	 */
	private static void merge(ArrayList<String> dclogFiles,
			String combinations, String output, boolean zip,
			String triggerTrimColumn, String trimRelativeColumn,
			String trimAbsColumn, VArrayList<String> trimColumns,
			String triggerMeasureColumn, String subtitleColumn, int dutStart,
			int dutStop, int dutOffset) throws Exception {

		File combinationsFile = new File(combinations);
		if (!combinationsFile.exists()) {
			throw new Exception(combinations + " does not exist.");
		}

		VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> data = new VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>>();

		int countOffset = 0;
		for (String file : dclogFiles) {

			BufferedReader dc = null;
			File dclogFile = new File(file);
			if (dclogFile.exists()) {
				dc = new BufferedReader(new FileReader(dclogFile));
			} else {
				File gzLogFile = new File(file + ".gz");
				if (gzLogFile.exists()) {
					dc = new BufferedReader(
							new InputStreamReader(new GZIPInputStream(
									new FileInputStream(gzLogFile))));
				} else {
					File bzLogFile = new File(file + ".bz2");
					if (bzLogFile.exists()) {
						FileInputStream fIS = new FileInputStream(bzLogFile);
						fIS.read(); // read dummy B
						fIS.read(); // read dummy Z
						dc = new BufferedReader(new InputStreamReader(
								new CBZip2InputStream(fIS)));
					} else {
						throw new Exception(file + " does not exist.");
					}
				}
			}
			// *****************************************************************************
			// * Load the dclog file and parse it
			// *****************************************************************************
			System.out.print("Loading " + file + "...");
			for (int i = 0; i < 36; i++) {
				dc.readLine(); // skip header
			}
			DclogLexer dclexer = new DclogLexer(dc);
			DclogParser dcparser = new DclogParser(dclexer);
			dcparser.file(data, dutStart, dutStop, countOffset);
			System.out.println("done");
			countOffset += dutOffset;
		}

		// *****************************************************************************
		// * Load the combinations file and parse it
		// *****************************************************************************
		EvalGenCombinationsLexer evalGenCombinationsLexer = new EvalGenCombinationsLexer(
				new BufferedReader(new FileReader(new File(combinations))));
		EvalGenCombinationsParser evalGenCombinationsParser = new EvalGenCombinationsParser(
				evalGenCombinationsLexer);
		evalGenCombinationsParser.setIncludeDir(new File("."));
		evalGenCombinationsParser.blocks();

		EvalGenCombinationsTreeWalker evalGenCombinationsWalker = new EvalGenCombinationsTreeWalker();
		VArrayList<VArrayList<AlternativesASTSet>> blocks = evalGenCombinationsWalker
				.blocks(evalGenCombinationsParser.getAST());

		OurTreeNode globalParent = new Asdap(1);

		Iterator<VLinkedHashMap<VLinkedHashMap<Symbol>>> dataIterator = data
				.values().iterator();

		evalGenCombinationsWalker.setGlobalSymbolTable(new VHashMap<Symbol>());

		for (VArrayList<AlternativesASTSet> block : blocks) {
			VArrayList<Integer> alts = new VArrayList<Integer>();
			VArrayList<Integer> nonTrivialAlt = new VArrayList<Integer>();
			for (int j = 0; j < block.size(); j++) {
				alts.add(block.get(j).getNumberOfAlternatives());
				if (block.get(j).getNumberOfAlternatives() > 1) {
					nonTrivialAlt.add(j);
				}
			}
			Combination c = new Combination(alts);

			Shmoo lastshmoo = null;
			VLinkedHashMap<VArrayList<Symbol>> xLabelsAcc = new VLinkedHashMap<VArrayList<Symbol>>();
			VLinkedHashMap<VArrayList<Symbol>> yDataAcc = new VLinkedHashMap<VArrayList<Symbol>>();

			c.reset();
			for (; c.hasNext();) {

				VArrayList<Integer> combination = c.getNextCombination();
				if (!dataIterator.hasNext()) {
					System.out.println("Warning: dclog is to short!!!!");
					break;
				}
				VLinkedHashMap<VLinkedHashMap<Symbol>> entry = dataIterator
						.next();
				OurTreeNode parent = globalParent;

				// for this combination calculate the value of the symbol table
				VHashMap<Symbol> symbolTable = new VHashMap<Symbol>();
				evalGenCombinationsWalker.setSymbolTable(symbolTable);

				VArrayList<VLinkedHashMap<Symbol>> altSymbols = new VArrayList<VLinkedHashMap<Symbol>>();
				for (int j = 0; j < block.size(); j++) {
					VArrayList<String> setKeys = block.get(j).getKeys();
					VArrayList<AST> setValues = block.get(j).getAlternative(
							combination.get(j));
					if (setKeys.size() != setValues.size()) {
						throw new Exception(
								"Number of Keys is unequal to number of values");
					}
					VLinkedHashMap<Symbol> altSymbol = new VLinkedHashMap<Symbol>();
					for (int k = 0; k < setKeys.size(); k++) {
						Symbol res = evalGenCombinationsWalker.expr(setValues
								.get(k));
						res.setName(setKeys.get(k));
						symbolTable.put(setKeys.get(k), res);

						altSymbol.put(setKeys.get(k), res);
					}
					altSymbols.add(altSymbol);
				}

				for (int j = 0; j < block.size(); j++) {
					// check if we have a nontrivial alternative
					String name = generateName(altSymbols.get(j));

					if (altContainsSymbol(altSymbols.get(j),
							triggerMeasureColumn)) {
						// shmoo
						String shmooTrim = getSymbolValueString(altSymbols,
								triggerTrimColumn);
						String shmooMeasure = getSymbolValueString(altSymbols,
								triggerMeasureColumn);
						String shmooSubtitle = getSymbolValueString(altSymbols,
								subtitleColumn);

						name = shmooMeasure + "/" + shmooTrim + "/"
								+ shmooSubtitle;
					} else if (altContainsSymbol(altSymbols.get(j),
							triggerTrimColumn)) {
						//
					} else if (nonTrivialAlt.contains(j)) {
						// general alternative
					}

					OurTreeNode child = findChildWithName(parent, name);
					if (child == null) {
						// child does not exist yet, so we need to create a new
						// one
						if (altContainsSymbol(altSymbols.get(j),
								triggerMeasureColumn)) {
							// add shmoo
							if (lastshmoo != null) {
								// add remaining stuff to last shmoo

								VArrayList<Xdata> xlabels = new VArrayList<Xdata>();

								VArrayList<Double> xpos = new VArrayList<Double>();
								for (Symbol sym : xLabelsAcc.get(trimAbsColumn)) {
									xpos.add(sym.convertToDouble().getValue());
								}
								for (String s : xLabelsAcc.keySet()) {
									if (xLabelsAcc.get(s).get(0) instanceof SymbolString) {
										xlabels.add(new Xdata(s, s, "",
												xLabelsAcc.get(s), xpos));
									} else if (xLabelsAcc.get(s).get(0)
											.getUnit() != null) {
										xlabels.add(new Xdata(s, s, xLabelsAcc
												.get(s).get(0).getUnit()
												.toString(), xLabelsAcc.get(s),
												xpos));
									} else {
										xlabels.add(new Xdata(s, s, "",
												xLabelsAcc.get(s), xpos));
									}
								}
								lastshmoo.setXdata(xlabels);

								for (String s : yDataAcc.keySet()) {
									if (yDataAcc.get(s).get(0).getUnit() == null) {
										lastshmoo.addChild(new Ydata(lastshmoo,
												s, s, "", yDataAcc.get(s)));
									} else {
										lastshmoo.addChild(new Ydata(lastshmoo,
												s, s, yDataAcc.get(s).get(0)
														.getUnit().toString(),
												yDataAcc.get(s)));
									}
								}

								// reset accumulation arrays
								xLabelsAcc = new VLinkedHashMap<VArrayList<Symbol>>();
								yDataAcc = new VLinkedHashMap<VArrayList<Symbol>>();
							}
							// add new shmoo
							Shmoo shmoo = new Shmoo();
							// shmoo.setName( generateName( altSymbols.get( j )
							// ) );
							String shmooTrim = getSymbolValueString(altSymbols,
									triggerTrimColumn);
							String shmooMeasure = getSymbolValueString(
									altSymbols, triggerMeasureColumn);
							String shmooSubtitle = getSymbolValueString(
									altSymbols, subtitleColumn);
							/*
							 * for ( int m = 0; m < altSymbols.size(); m++ ) {
							 * if ( nonTrivialAlt.contains( m ) ) {
							 * System.out.print( generateName( altSymbols.get( m
							 * ) ) + " " ); } }
							 */
							shmoo.setName(shmooMeasure + "/" + shmooTrim + "/"
									+ shmooSubtitle);
							shmoo
									.setDescription(shmooMeasure + "/"
											+ shmooTrim);
							shmoo.setSubtitle(shmooSubtitle);
							shmoo.setTrim(shmooTrim);
							shmoo.setMeasure(shmooMeasure);
							shmoo.setDefaultXdata(trimAbsColumn);
							shmoo.setRelativeXdata(trimRelativeColumn);

							VHashMap<Symbol> constants = new VHashMap<Symbol>();
							for (int k = 0; k < j; k++) {
								constants.putAll(altSymbols.get(k));
							}
							shmoo.setConstants(constants);

							parent.addChild(shmoo);
							System.out.println("New Shmoo: " + shmooMeasure
									+ "/" + shmooTrim + ", " + shmooSubtitle);
							System.out.println("at: " + shmoo.getTreePath());
							lastshmoo = shmoo;
							parent = shmoo;
						} else if (altContainsSymbol(altSymbols.get(j),
								triggerTrimColumn)) {
							// accumulate data
							// add required values from the alternatives
							VLinkedHashMap<Symbol> oST = new VLinkedHashMap<Symbol>();
							for (VLinkedHashMap<Symbol> sT : altSymbols) {
								for (String symName : sT.keySet()) {
									if (trimColumns.contains(symName)) {
										Symbol value = sT.get(symName);
										oST.put(symName, value);
									}
								}
							}

							// now oST only contains the last value of all
							// alternatives
							// variables appearing twice are overwritten!
							for (String symName : oST.keySet()) {
								Symbol value = oST.get(symName);
								// add to xlabels
								VArrayList<Symbol> xlabel = null;
								if (xLabelsAcc.containsKey(symName)) {
									xlabel = xLabelsAcc.get(symName);
								} else {
									xlabel = new VArrayList<Symbol>();
								}
								xlabel.add(value);
								// recognize type
								xLabelsAcc.put(symName, xlabel);

							}

							// add corresponding results from dclog_af.dat
							for (VLinkedHashMap<Symbol> e : entry.values()) {
								VArrayList<Symbol> y = null;
								if (yDataAcc.containsKey(e.get("S0").getName())) {
									y = yDataAcc.get(e.get("S0").getName());
								} else {
									y = new VArrayList<Symbol>();
								}
								y.add(e.get("S0"));
								yDataAcc.put(e.get("S0").getName(), y);
							}
						} else if (nonTrivialAlt.contains(j)) {
							// add general alternative

							boolean found = false;
							for (String symName : altSymbols.get(j).keySet()) {
								if (trimColumns.contains(symName)) {
									found = true;
									break;
								}
							}

							if (!found) {
								Alternative alternative = new Alternative();
								alternative.setName(name);
								alternative
										.setDescription(generateAlternativeName(altSymbols
												.get(j)));
								if (parent instanceof Shmoo) {
									System.out.println("adding alt " + name
											+ " to Shmoo " + parent.toString());
								}
								parent.addChild(alternative);
								parent = alternative;
							}
						}
					} else {
						// child is already existing, so we need to do
						// nothing...
						parent = child;
					}
				}
			}
			if (lastshmoo != null) {
				// add remaining stuff to last shmoo

				VArrayList<Xdata> xlabels = new VArrayList<Xdata>();

				VArrayList<Double> xpos = new VArrayList<Double>();
				for (Symbol sym : xLabelsAcc.get(trimAbsColumn)) {
					xpos.add(sym.convertToDouble().getValue());
				}
				for (String s : xLabelsAcc.keySet()) {
					if (xLabelsAcc.get(s).get(0) instanceof SymbolString) {
						xlabels
								.add(new Xdata(s, s, "", xLabelsAcc.get(s),
										xpos));
					} else if (xLabelsAcc.get(s).get(0).getUnit() != null) {
						xlabels
								.add(new Xdata(s, s, xLabelsAcc.get(s).get(0)
										.getUnit().toString(), xLabelsAcc
										.get(s), xpos));
					} else {
						xlabels
								.add(new Xdata(s, s, "", xLabelsAcc.get(s),
										xpos));
					}
				}
				lastshmoo.setXdata(xlabels);

				for (String s : yDataAcc.keySet()) {
					if (yDataAcc.get(s).get(0).getUnit() == null) {
						lastshmoo.addChild(new Ydata(lastshmoo, s, s, "",
								yDataAcc.get(s)));
					} else {
						lastshmoo.addChild(new Ydata(lastshmoo, s, s, yDataAcc
								.get(s).get(0).getUnit().toString(), yDataAcc
								.get(s)));
					}
				}
			}
		}

		// *****************************************************************************
		// * Save the data in JCombinations format
		// *****************************************************************************

		OutputStream outStream = null;
		if (zip) {
			System.out.print("Saving " + output + ".zip...");
			ZipOutputStream zipOutStream = new ZipOutputStream(
					new FileOutputStream(output + ".zip"));
			zipOutStream.putNextEntry(new ZipEntry("default.xml"));
			outStream = zipOutStream;
		} else {
			System.out.print("Saving " + output + "...");
			outStream = new FileOutputStream(output);
		}

		IBindingFactory bfact;
		try {
			bfact = BindingDirectory.getFactory(Asdap.class);

			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(4);
			mctx.marshalDocument(globalParent, "UTF-8", null, outStream);

		} catch (JiBXException e) {
			e.printStackTrace();
		}

		System.out.println("done");
	}
}
