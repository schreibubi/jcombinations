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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.cli.ParseException;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallable;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.Xdata;
import org.schreibubi.JCombinations.FileFormat.Ydata;
import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
import org.schreibubi.JCombinationsTools.LotresultsFormat.DCResults;
import org.schreibubi.JCombinationsTools.evalGenCombinations.AlternativesASTSet;
import org.schreibubi.JCombinationsTools.evalGenCombinations.Combination;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinationsLexer;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinationsParser;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinationsTreeWalker;
import org.schreibubi.JCombinationsTools.settings.SettingsSingleton;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.symbol.SymbolVisitorPrint;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;
import org.schreibubi.visitor.VLinkedHashMap;

import antlr.RecognitionException;
import antlr.collections.AST;

import com.jmatio.io.MatFileWriter;

/**
 * Merge results from dclog-file with the data of the combinations files.
 * 
 * @author Jörg Werner
 * 
 */
public class MergeResultsExperimental {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MergeResultsOptions[] settings = MergeResultsOptions.values();
			SettingsSingleton.initialize("MergeResults", settings);
			// command line arguments have the highest priority, so they go into level 2
			SettingsSingleton.getInstance().parseArguments(args, 2);

			if (SettingsSingleton.getInstance().getProperty("help").equals("true")) {
				SettingsSingleton.getInstance().displayHelp();
				Runtime.getRuntime().exit(0);
			}

			if (SettingsSingleton.getInstance().getProperty("version").equals("true")) {
				Info.printVersion("MergeResults");
				Runtime.getRuntime().exit(0);
			}

			// pre-tags
			VArrayList<String> preTags = new VArrayList<String>();
			String preTagsString = SettingsSingleton.getInstance().getProperty("pretags");
			preTags = new VArrayList<String>(Arrays.asList(preTagsString.split(",")));
			// post-tags
			VArrayList<String> postTags = new VArrayList<String>();
			String postTagsString = SettingsSingleton.getInstance().getProperty("posttags");
			postTags = new VArrayList<String>(Arrays.asList(postTagsString.split(",")));

			FileNameLookupSingleton.initialize(preTags, postTags);

			merge();
		} catch (ParseException e) {
		} catch (Exception e) {
			System.out.println("MergeResults error: " + e);
			e.printStackTrace();
		}
	}

	private static boolean altContainsSymbol(VLinkedHashMap<Symbol> symbolList, String symbol) throws Exception {
		if (symbolList.get(symbol) != null)
			return true;
		else
			return false;
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

	private static String generateAlternativeName(VLinkedHashMap<Symbol> symbolList) throws Exception {
		Iterator<Symbol> i = symbolList.values().iterator();
		Symbol s = i.next();
		if (s != null)
			return s.toString();
		else
			return "";
	}

	/**
	 * @param evalGenCombinationsWalker
	 * @param block
	 * @param combination
	 * @param altSymbols
	 * @throws Exception
	 * @throws RecognitionException
	 */
	private static VArrayList<VLinkedHashMap<Symbol>> generateAlternativeSymbols(
			EvalGenCombinationsTreeWalker evalGenCombinationsWalker, VArrayList<AlternativesASTSet> block,
			VArrayList<Integer> combination) throws Exception, RecognitionException {
		VArrayList<VLinkedHashMap<Symbol>> altSymbols = new VArrayList<VLinkedHashMap<Symbol>>();
		// for this combination calculate the value of the symbol table
		VHashMap<Symbol> symbolTable = new VHashMap<Symbol>();
		evalGenCombinationsWalker.setSymbolTable(symbolTable);
		for (int j = 0; j < block.size(); j++) {
			VArrayList<String> setKeys = block.get(j).getKeys();
			VArrayList<AST> setValues = block.get(j).getAlternative(combination.get(j));
			if (setKeys.size() != setValues.size())
				throw new Exception("Number of Keys is unequal to number of values");
			VLinkedHashMap<Symbol> altSymbol = new VLinkedHashMap<Symbol>();
			for (int k = 0; k < setKeys.size(); k++) {
				Symbol res = evalGenCombinationsWalker.expr(setValues.get(k));
				res.setName(setKeys.get(k));
				symbolTable.put(setKeys.get(k), res);
				altSymbol.put(setKeys.get(k), res);
			}
			altSymbols.add(altSymbol);
		}
		return altSymbols;
	}

	private static String generateName(VLinkedHashMap<Symbol> symbolList) throws Exception {
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

	private static String getSymbolValueString(VArrayList<VLinkedHashMap<Symbol>> symbolList, String symbol) {
		for (VLinkedHashMap<Symbol> map : symbolList) {
			Symbol sym = map.get(symbol);
			if (sym != null)
				return sym.getValueString();
		}
		return null;
	}

	/**
	 * @param testNameVariable
	 * @param data
	 * @param altSymbols
	 * @return
	 * @throws Exception
	 */
	private static VLinkedHashMap<VLinkedHashMap<Symbol>> getTestData(String testNameVariable,
			VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> data, VArrayList<VLinkedHashMap<Symbol>> altSymbols)
			throws Exception {
		String testName = getSymbolValueString(altSymbols, testNameVariable);
		if (testName == null)
			throw new Exception("Variable " + testNameVariable + " is undefinded!");
		if (!data.containsKey(testName)) {
			System.err.println("No results found for test " + testName);
			return null;
		}
		VLinkedHashMap<VLinkedHashMap<Symbol>> dclogData = data.get(testName);
		return dclogData;
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static void merge() throws Exception {
		// *****************************************************************************
		// * Load the combinations file and parse it
		// *****************************************************************************
		String combinationsFileName = SettingsSingleton.getInstance().getProperty("combinations");
		File dir = new File(combinationsFileName).getAbsoluteFile().getParentFile();
		File combinationFile = FileNameLookupSingleton.getInstance().lookup(dir, combinationsFileName);
		System.out.print("Loading " + combinationFile.getName() + "...");

		EvalGenCombinationsLexer evalGenCombinationsLexer = new EvalGenCombinationsLexer(new BufferedReader(
				new FileReader(combinationFile)));
		EvalGenCombinationsParser evalGenCombinationsParser = new EvalGenCombinationsParser(evalGenCombinationsLexer);
		evalGenCombinationsParser.setIncludeDir(dir);
		evalGenCombinationsParser.blocks();

		VHashMap<String> optionsFromEvalGenWalker = new VHashMap<String>();
		EvalGenCombinationsTreeWalker evalGenCombinationsWalker = new EvalGenCombinationsTreeWalker();
		evalGenCombinationsWalker.setOptions(optionsFromEvalGenWalker);
		VArrayList<VArrayList<AlternativesASTSet>> blocks = evalGenCombinationsWalker.blocks(evalGenCombinationsParser
				.getAST());
		System.out.println("done");

		// *****************************************************************************
		// now set the options according to the combinations file
		// *****************************************************************************
		String argumentsString = optionsFromEvalGenWalker.get("MergeResults");
		if (argumentsString != null) {
			String[] args = argumentsString.split(" ");
			SettingsSingleton.getInstance().parseArguments(args, 1);
		}

		if (SettingsSingleton.getInstance().areRequiredOptionsSet() == false) {
			SettingsSingleton.getInstance().displayHelp();
			Runtime.getRuntime().exit(0);
		}

		String dutSelectionString = SettingsSingleton.getInstance().getProperty("dut");
		VArrayList<Integer> duts = new VArrayList<Integer>();
		String[] cS = dutSelectionString.split(",");
		for (String s : cS) {
			if (s.contains("-")) {
				String[] dS = s.split("-");
				if (dS.length < 2)
					throw new Exception("dut specification is wrong, should be e.g. 1-16");
				for (int i = Integer.parseInt(dS[0]); i <= Integer.parseInt(dS[1]); i++) {
					duts.add(i);
				}
			} else {
				duts.add(Integer.parseInt(s));
			}
		}

		int dutFileOffset = Integer.parseInt(SettingsSingleton.getInstance().getProperty("dutFileOffset"));
		int dutTouchdownOffset = Integer.parseInt(SettingsSingleton.getInstance().getProperty("dutTouchdownOffset"));

		// *****************************************************************************
		// * Load the dclog file and parse it
		// *****************************************************************************
		String resultFilePrefix = SettingsSingleton.getInstance().getProperty("prefix");
		String resultFilePostfix = SettingsSingleton.getInstance().getProperty("postfix");
		String resultFileString = SettingsSingleton.getInstance().getProperty("dclog");
		ArrayList<String> resultFiles = new ArrayList<String>();
		if (!resultFileString.equals("")) {
			String[] dF = resultFileString.split(",");
			for (String s : dF) {
				resultFiles.add(s);
			}
		} else {
			int dotPos = combinationsFileName.lastIndexOf(".");
			if (dotPos > -1) {
				resultFiles.add(resultFilePrefix + combinationsFileName.substring(0, dotPos) + resultFilePostfix);
			} else {
				resultFiles.add(resultFilePrefix + combinationsFileName + resultFilePostfix);
			}
		}

		VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> data = new VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>>();

		IBindingFactory lotreportBindingFactory = BindingDirectory.getFactory(DCResults.class);
		IUnmarshallingContext lotreportUnmarshallingContext = lotreportBindingFactory.createUnmarshallingContext();

		DCResults obj = new DCResults();
		obj.setActiveDuts(duts);
		obj.setTouchdownDutOffset(dutTouchdownOffset);
		int countOffset = 0;
		for (String file : resultFiles) {

			BufferedReader dc = null;
			File dclogFile = new File(file);
			if (dclogFile.exists()) {
				dc = new BufferedReader(new FileReader(dclogFile));
			} else {
				File gzLogFile = new File(file + ".gz");
				if (gzLogFile.exists()) {
					dc = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(gzLogFile))));
				} else {
					File bzLogFile = new File(file + ".bz2");
					if (bzLogFile.exists()) {
						FileInputStream fIS = new FileInputStream(bzLogFile);
						fIS.read(); // read dummy B
						fIS.read(); // read dummy Z
						dc = new BufferedReader(new InputStreamReader(new CBZip2InputStream(fIS)));
					} else
						throw new Exception(file + " does not exist.");
				}
			}
			System.out.print("Loading " + file + "...");

			obj.setFileDutOffset(countOffset);
			lotreportUnmarshallingContext.setDocument(dc);
			((IUnmarshallable) obj).unmarshal(lotreportUnmarshallingContext);
			System.out.println("done");
			countOffset += dutFileOffset;
		}
		data = obj.getResults();

		// *****************************************************************************
		// now generate the plots
		// *****************************************************************************
		String triggerTrimColumn = SettingsSingleton.getInstance().getProperty("trim");
		String triggerMeasureColumn = SettingsSingleton.getInstance().getProperty("measure");
		String trimColumnsString = SettingsSingleton.getInstance().getProperty("xvals");
		String trimRelativeColumn = SettingsSingleton.getInstance().getProperty("relative");
		String trimAbsColumn = SettingsSingleton.getInstance().getProperty("absolute");
		String subtitleColumn = SettingsSingleton.getInstance().getProperty("subtitle");
		String testNameVariable = SettingsSingleton.getInstance().getProperty("testname");

		String[] tC = trimColumnsString.split(",");
		VArrayList<String> trimColumns = new VArrayList<String>();
		for (String s : tC) {
			trimColumns.add(s);
		}

		OurTreeNode globalParent = new Asdap(1);

		evalGenCombinationsWalker.setGlobalSymbolTable(new VHashMap<Symbol>());

		for (VArrayList<AlternativesASTSet> block : blocks) {
			VArrayList<Integer> alts = new VArrayList<Integer>();
			for (int j = 0; j < block.size(); j++) {
				alts.add(block.get(j).getNumberOfAlternatives());
			}
			Combination c = new Combination(alts);

			c.reset();

			VArrayList<Integer> combination = c.getNextCombination();
			VArrayList<VLinkedHashMap<Symbol>> altSymbols = generateAlternativeSymbols(evalGenCombinationsWalker,
					block, combination);

			boolean allCombinationsDone = false;
			do {
				OurTreeNode parent = globalParent;
				int blockIndex;
				for (blockIndex = 0; blockIndex < block.size(); blockIndex++) {
					// check if we have a nontrivial alternative
					String name = generateName(altSymbols.get(blockIndex));
					OurTreeNode child = findChildWithName(parent, name);
					if (child == null) {
						// child does not exist yet, so we need to create a new one
						if (altContainsSymbol(altSymbols.get(blockIndex), triggerMeasureColumn)) {
							break;
						} else {
							// add general alternative
							boolean found = false;
							for (String symName : altSymbols.get(blockIndex).keySet())
								if (trimColumns.contains(symName)) {
									found = true;
									break;
								}
							if (!found) {
								Alternative alternative = new Alternative();
								alternative.setName(name);
								alternative.setDescription(generateAlternativeName(altSymbols.get(blockIndex)));
								parent.addChild(alternative);
								parent = alternative;
							}
						}
					} else {
						// child is already existing, so we need to do nothing...
						parent = child;
					}
				}
				if (blockIndex < block.size()) {
					String shmooTrim = getSymbolValueString(altSymbols, triggerTrimColumn);
					String shmooMeasuresString = getSymbolValueString(altSymbols, triggerMeasureColumn);
					List<String> shmooMeasures = Arrays.asList(shmooMeasuresString.split("\\\\"));
					String shmooSubtitle = getSymbolValueString(altSymbols, subtitleColumn);

					VHashMap<Symbol> constants = new VHashMap<Symbol>();
					for (int k = 0; k < blockIndex; k++) {
						constants.putAll(altSymbols.get(k));
					}

					VArrayList<Shmoo> shmoos = new VArrayList<Shmoo>();
					for (String shmooMeasure : shmooMeasures) {
						Shmoo shmoo = new Shmoo();
						shmoo.setName(shmooMeasure + "/" + shmooTrim + "/" + shmooSubtitle);
						shmoo.setDescription(shmooMeasure + "/" + shmooTrim);
						shmoo.setSubtitle(shmooSubtitle);
						shmoo.setTrim(shmooTrim);
						shmoo.setMeasure(shmooMeasure);
						shmoo.setDefaultXdata(trimAbsColumn);
						shmoo.setRelativeXdata(trimRelativeColumn);
						shmoo.setConstants(constants);
						shmoos.add(shmoo);
					}

					VLinkedHashMap<VArrayList<Symbol>> xLabelsAcc = new VLinkedHashMap<VArrayList<Symbol>>();
					VLinkedHashMap<VLinkedHashMap<VArrayList<Symbol>>> yDataAcc = new VLinkedHashMap<VLinkedHashMap<VArrayList<Symbol>>>();

					// repeat until next shmoo....
					boolean shmooFinished = false;
					do {
						// accumulate data
						// add required values from the alternatives
						VLinkedHashMap<Symbol> oST = new VLinkedHashMap<Symbol>();
						for (VLinkedHashMap<Symbol> sT : altSymbols) {
							for (String symName : sT.keySet())
								if (trimColumns.contains(symName)) {
									Symbol value = sT.get(symName);
									oST.put(symName, value);
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

						VLinkedHashMap<VLinkedHashMap<Symbol>> dclogData = getTestData(testNameVariable, data,
								altSymbols);
						if (dclogData != null) {
							// add corresponding results from dclog_af.dat
							for (VLinkedHashMap<Symbol> dD : dclogData.values()) {
								for (int i = 0; i < shmooMeasures.size(); i++) {
									VLinkedHashMap<VArrayList<Symbol>> yDA = yDataAcc.get("K" + i);
									if (yDA == null) {
										yDA = new VLinkedHashMap<VArrayList<Symbol>>();
									}
									VArrayList<Symbol> y = null;
									if (yDA.containsKey(dD.get("S" + i).getName())) {
										y = yDA.get(dD.get("S" + i).getName());
									} else {
										y = new VArrayList<Symbol>();
									}
									y.add(dD.get("S" + i));
									yDA.put(dD.get("S" + i).getName(), y);
									yDataAcc.put("K" + i, yDA);
								}
							}
						}

						if (c.hasNext()) {
							VArrayList<Boolean> changedAlternatives = c.getAlternativesWhichWillChange();
							shmooFinished = changedAlternatives.subList(0, blockIndex + 1).contains(true);
							altSymbols = generateAlternativeSymbols(evalGenCombinationsWalker, block, c
									.getNextCombination());
							String shmooTrimComp = getSymbolValueString(altSymbols, triggerTrimColumn);
							String shmooMeasuresStringComp = getSymbolValueString(altSymbols, triggerMeasureColumn);
							String shmooSubtitleComp = getSymbolValueString(altSymbols, subtitleColumn);
							if (!shmooTrim.equals(shmooTrimComp) | !shmooSubtitle.equals(shmooSubtitleComp)
									| !shmooMeasuresString.equals(shmooMeasuresStringComp)) {
								shmooFinished = true;
							}
						} else {
							shmooFinished = true;
							allCombinationsDone = true;
						}
					} while (!shmooFinished);

					// add collected data to shmoo
					VArrayList<Xdata> xlabels = new VArrayList<Xdata>();
					VArrayList<Double> xpos = new VArrayList<Double>();
					for (Symbol sym : xLabelsAcc.get(trimAbsColumn)) {
						xpos.add(sym.convertToDouble().getValue());
					}

					for (String s : xLabelsAcc.keySet())
						if (xLabelsAcc.get(s).get(0) instanceof SymbolString) {
							xlabels.add(new Xdata(s, s, "", xLabelsAcc.get(s), xpos));
						} else if (xLabelsAcc.get(s).get(0).getUnit() != null) {
							xlabels.add(new Xdata(s, s, xLabelsAcc.get(s).get(0).getUnit().toString(), xLabelsAcc
									.get(s), xpos));
						} else {
							xlabels.add(new Xdata(s, s, "", xLabelsAcc.get(s), xpos));
						}

					for (int i = 0; i < shmooMeasures.size(); i++) {
						if (yDataAcc.get("K" + i) != null) {
							Shmoo shmoo = shmoos.get(i);
							shmoo.setXdata(xlabels);
							VLinkedHashMap<VArrayList<Symbol>> yDA = yDataAcc.get("K" + i);
							for (String s : yDA.keySet())
								if (yDA.get(s).get(0).getUnit() == null) {
									shmoo.addChild(new Ydata(shmoo, s, s, "", yDA.get(s)));
								} else {
									shmoo.addChild(new Ydata(shmoo, s, s, yDA.get(s).get(0).getUnit().toString(), yDA
											.get(s)));
								}

							parent.addChild(shmoo);
							System.out.println("New Shmoo: " + shmoo.getMeasure() + "/" + shmoo.getTrim() + ", "
									+ shmoo.getSubtitle());
						}
					}
				} else
					throw new Exception("No trimming alternative found!");
			} while (!allCombinationsDone);
		}
		// *****************************************************************************
		// * Optimize Tree
		// *****************************************************************************

		OptimizeTreeVisitor v = new OptimizeTreeVisitor();
		globalParent.accept(v);

		// *****************************************************************************
		// * Save the data in JCombinations format
		// *****************************************************************************
		String outputFileName = SettingsSingleton.getInstance().getProperty("output");
		if (outputFileName.equals("")) {
			int dotPos = combinationsFileName.lastIndexOf(".");
			if (dotPos > -1) {
				outputFileName = combinationsFileName.substring(0, dotPos);
			} else {
				outputFileName = combinationsFileName;
			}
		}

		OutputStream outStream = null;
		if (SettingsSingleton.getInstance().getProperty("zip").equals("true")) {
			System.out.print("Saving " + outputFileName + ".xml.zip...");
			ZipOutputStream zipOutStream = new ZipOutputStream(new FileOutputStream(outputFileName + ".xml.zip"));
			zipOutStream.putNextEntry(new ZipEntry("default.xml"));
			outStream = zipOutStream;
		} else {
			System.out.print("Saving " + outputFileName + ".xml...");
			outStream = new FileOutputStream(outputFileName + ".xml");
		}

		IBindingFactory AsdapBindingFactory;
		try {
			AsdapBindingFactory = BindingDirectory.getFactory(Asdap.class);

			IMarshallingContext mctx = AsdapBindingFactory.createMarshallingContext();
			mctx.setIndent(4);
			mctx.marshalDocument(globalParent, "UTF-8", null, outStream);

		} catch (JiBXException e) {
			e.printStackTrace();
		}
		System.out.println("done");

		// *****************************************************************************
		// * Save the data in Matlab format
		// *****************************************************************************
		if (SettingsSingleton.getInstance().getProperty("matlab").equals("true")) {
			System.out.print("Saving " + outputFileName + ".mat...");
			MatlabWriteTreeVisitor mw = new MatlabWriteTreeVisitor();
			globalParent.accept(mw);
			ArrayList mllist = new ArrayList();
			mllist.add(mw.getMLArray());
			new MatFileWriter(outputFileName + ".mat", mllist);
			System.out.println("done");
		}
	}
}
