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
package org.schreibubi.JCombinationsTools.coordinator;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.ParseException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChain;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChainBuilder;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinations;
import org.schreibubi.JCombinationsTools.settings.SettingsSingleton;
import org.schreibubi.JCombinationsTools.templateEngine.TemplateEngine;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * @author Jörg Werner
 */
public class Coordinator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Info.printVersion("Coordinator");
		try {

			CoordinatorOptions[] settings = CoordinatorOptions.values();
			SettingsSingleton.initialize("Coordinator", settings);
			// command line arguments have the highest priority, so they go into
			// level 2
			SettingsSingleton.getInstance().parseArguments(args, 2);

			if (SettingsSingleton.getInstance().getProperty("help").equals(
					"true")) {
				SettingsSingleton.getInstance().displayHelp();
				Runtime.getRuntime().exit(0);
			}

			if (SettingsSingleton.getInstance().getProperty("version").equals(
					"true")) {
				Info.printVersion("SetiPrinter");
				Runtime.getRuntime().exit(0);
			}

			String combinationFileName = SettingsSingleton.getInstance()
					.getProperty("combinations");

			// pre-tags
			VArrayList<String> preTags = new VArrayList<String>();
			String preTagsString = SettingsSingleton.getInstance().getProperty(
					"pretags");
			preTags = new VArrayList<String>(Arrays.asList(preTagsString
					.split(",")));
			// post-tags
			VArrayList<String> postTags = new VArrayList<String>();
			String postTagsString = SettingsSingleton.getInstance()
					.getProperty("posttags");
			postTags = new VArrayList<String>(Arrays.asList(postTagsString
					.split(",")));

			FileNameLookupSingleton.initialize(preTags, postTags);

			File dir = new File(combinationFileName).getAbsoluteFile()
					.getParentFile();
			File combinationFile = FileNameLookupSingleton.getInstance()
					.lookup(dir, combinationFileName);
			System.out.println("Processing: " + combinationFile.getName());

			// Generate all possible combinations
			System.out.print("Generating and evaluation all combinations...");
			VHashMap<String> optionsFromEvalGenWalker = new VHashMap<String>();
			VArrayList<VHashMap<Symbol>> symbolTableLines = EvalGenCombinations
					.exec(new FileReader(combinationFile), dir,
							optionsFromEvalGenWalker);
			System.out.println("done");

			// now additional default options are known...
			// -----------------------------------------------------------------------------
			String argumentsString = optionsFromEvalGenWalker
					.get("Coordinator");
			if (argumentsString != null) {
				String[] arguments = argumentsString.split(" ");
				SettingsSingleton.getInstance().parseArguments(arguments, 1);
			}

			if (SettingsSingleton.getInstance().areRequiredOptionsSet() == false) {
				SettingsSingleton.getInstance().displayHelp();
				Runtime.getRuntime().exit(0);
			}

			String conditionFilePrefix = SettingsSingleton.getInstance()
					.getProperty("conditionprefix");

			boolean ignoreMissingCBMpos = false;
			if (SettingsSingleton.getInstance().getProperty(
					"ignoremissingcbmpos").equals("true")) {
				ignoreMissingCBMpos = true;
			}
			boolean silent = false;
			if (SettingsSingleton.getInstance().getProperty("silent").equals(
					"true")) {
				silent = true;
			}

			String cbmOutputDir = SettingsSingleton.getInstance().getProperty(
					"cbmdir");
			String patternInputDir = SettingsSingleton.getInstance()
					.getProperty("patterndir");
			String pcfOutputDir = SettingsSingleton.getInstance().getProperty(
					"pcfdir");

			int cbmOffset = Integer.parseInt(SettingsSingleton.getInstance()
					.getProperty("cbmOffset"));

			String conditionFileTemplateName = conditionFilePrefix
					+ ".template";
			String conditionFileHeaderName = conditionFilePrefix + ".header";

			if (!SettingsSingleton.getInstance().getProperty(
					"conditiontemplate").equals("")) {
				conditionFileTemplateName = SettingsSingleton.getInstance()
						.getProperty("conditiontemplate");
			}
			if (!SettingsSingleton.getInstance().getProperty("conditionheader")
					.equals("")) {
				conditionFileHeaderName = SettingsSingleton.getInstance()
						.getProperty("conditionheader");
			}

			String conditionFileName = "";
			if (!SettingsSingleton.getInstance().getProperty("condition")
					.equals("")) {
				conditionFileName = SettingsSingleton.getInstance()
						.getProperty("condition");
			} else {
				int dotPos = combinationFileName.indexOf(".");
				if (dotPos > -1) {
					conditionFileName = conditionFilePrefix + "_"
							+ combinationFileName.subSequence(0, dotPos);
				} else {
					conditionFileName = conditionFilePrefix + "_"
							+ combinationFileName;
				}
			}
			String setiFile = SettingsSingleton.getInstance().getProperty(
					"seti");
			// -----------------------------------------------------------------------------
			if (SettingsSingleton.getInstance().getProperty("nocbmgen").equals(
					"false")) {
				boolean filesWereOverwritten = false;
				System.out.print("Creating CBM dat files...");

				SetiChainBuilder constructSetiChain = new SetiChainBuilder(
						setiFile);

				VHashMap<TemplateInfo> templateInfos = new VHashMap<TemplateInfo>();

				HashMap<String, Integer> patCbmSettings = new HashMap<String, Integer>();

				int cbmPatternCount = cbmOffset;

				for (ListIterator<VHashMap<Symbol>> i = symbolTableLines
						.listIterator(); i.hasNext();) {
					/* get variables used to fill out the actual template */
					VHashMap<Symbol> sT = i.next();

					String patName = sT.get("PATNAME").convertToString()
							.getValue();
					TemplateInfo tI = templateInfos.get(patName);
					// if template was not done already before
					if (tI == null) {
						// read in the cbm position file for this template
						tI = readCBMpos(patternInputDir, patName,
								ignoreMissingCBMpos);
						if (tI == null) {
							if (!silent) {
								System.err
										.println("WARNING: "
												+ patName
												+ ".asc does not contain a cbm part, but ignored due to manual override!");
							}
						} else {
							templateInfos.put(patName, tI);
						}
					}

					if (tI != null) {
						// check if we already have a dat file with the same
						// settings...
						VArrayList<Symbol> currentSymbols = selectSymbols(sT,
								tI.getCbmNames());
						currentSymbols.add(sT.get("PATNAME"));

						Integer foundNumber = patCbmSettings.get(currentSymbols
								.toString());
						if (foundNumber != null) {
							// we have already a dat file containing the same
							// settings
							// so set the CBMNAME accordingly
							sT.put("CBMNAME", new SymbolString(Integer
									.toHexString(foundNumber).toUpperCase()));
						} else {
							// first time we have these conditions
							cbmPatternCount++;
							String cbmName = Integer
									.toHexString(cbmPatternCount);

							VArrayList<String> cbmNames = tI.getCbmNames();

							// remember our settings...
							VArrayList<Symbol> selectedSymbols = selectSymbols(
									sT, cbmNames);
							selectedSymbols.add(sT.get("PATNAME"));
							patCbmSettings.put(selectedSymbols.toString(),
									cbmPatternCount);

							sT.put("CBMNAME", new SymbolString(cbmName
									.toUpperCase()));

							// calculate seti-chains and put them in CBM chunks
							for (CBMChunk chunk : tI.getCbms()) {
								if (sT.get(chunk.getName()) != null) {
									VArrayList<Integer> content = constructSetiChain
											.createCBMChain(sT.get(
													chunk.getName())
													.convertToString(), tI
													.getChannels(), chunk
													.getLength(), chunk
													.getSetiType());
									chunk.setCbmContent(content);
								}
							}
							// now optimize CBMChunks (are already in ascending
							// order, so we only need to check the next one)
							VArrayList<CBMChunk> compressedChunks = new VArrayList<CBMChunk>(
									tI.getCbms());

							// write them to file

							File cbmOutput = new File(cbmOutputDir, cbmName
									+ ".dat");
							if (cbmOutput.exists()) {
								filesWereOverwritten = true;
							}
							DataOutputStream out = new DataOutputStream(
									new BufferedOutputStream(
											new FileOutputStream(cbmOutput)));
							out.writeInt(compressedChunks.size());
							for (CBMChunk chunk : compressedChunks) {
								if (sT.get(chunk.getName()) != null) {
									// output hexstring to cbm-file
									out.writeInt(chunk.getStart());
									out.writeInt(chunk.getLength());
									for (Integer integer : chunk
											.getCbmContent()) {
										out.writeInt(integer);
									}
								} else {
									throw new Exception(chunk.getName()
											+ " does not exist");
								}
							}

							out.close();
						}
					} else {
						sT.put("CBMNAME", new SymbolString(""));
					}
				}
				System.out.println("done");
				if ((!silent) && (filesWereOverwritten)) {
					System.err
							.println("WARNING: Some .dat files were overwritten!");
				}
			} else {
				// Even if no CBM.dat generation is requested, we need to set
				// the CBMNAME...
				for (ListIterator<VHashMap<Symbol>> i = symbolTableLines
						.listIterator(); i.hasNext();) {
					/* get variables used to fill out the actual template */
					VHashMap<Symbol> sT = i.next();
					sT.put("CBMNAME", new SymbolString(""));
				}
			}

			if (SettingsSingleton.getInstance().getProperty("nopcf").equals(
					"false")) {
				// Create the condition file
				System.out.print("Creating " + conditionFileName + "...");
				TemplateEngine.exec(symbolTableLines, false, conditionFileName,
						false, conditionFileTemplateName, new File("."),
						new File(pcfOutputDir), new File(
								conditionFileHeaderName), null);
				System.out.println("done");
			}

		} catch (ParseException e) {
		} catch (TokenStreamException e) {
			System.out.println("TokenStreamException: " + e.getMessage());
		} catch (RecognitionException e) {
			System.out.println("RecognitionException: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Coordinator error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static SetiChain.SetiTypeEnum convertToSetiType(String s)
			throws Exception {
		switch (s.toUpperCase().charAt(0)) {
		case 'R':
			return SetiChain.SetiTypeEnum.READ;
		case 'W':
			return SetiChain.SetiTypeEnum.WRITE;
		case 'C':
			return SetiChain.SetiTypeEnum.COMMAND;
		default:
			throw new Exception("unknown Seti tpye");
		}
	}

	/**
	 * @param templateInfos
	 * @param patName
	 * @param ignoreMissingCBMpos
	 * @param tI
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private static TemplateInfo readCBMpos(String patDir, String patName,
			boolean ignoreMissingCBMpos) throws FileNotFoundException,
			IOException, NumberFormatException, Exception {
		File patFile = FileNameLookupSingleton.getInstance().lookup(
				new File(patDir), patName + ".asc");
		if (patFile.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(patFile));

			// scan to the ASDAPSETUP part
			String str = null;
			do {
				str = in.readLine();
				if (str == null) {
					return null;
				}
			} while (!str.contains("~ASDAPSETUP"));

			VArrayList<Integer> channels = new VArrayList<Integer>();

			Pattern p = Pattern.compile(";~\\s*([\\s\\w,_]*)");
			str = in.readLine();
			Matcher m = p.matcher(str);
			if (!m.matches()) {
				throw new Exception("No pin definition found in line " + str);
			}

			String[] chanArray = m.group(1).split(",");
			for (String element : chanArray) {
				channels.add(Integer.parseInt(element.trim()));
			}

			VArrayList<CBMChunk> cbms = new VArrayList<CBMChunk>();
			str = null;
			while (!(str = in.readLine()).contains("~ASDAPSETEND")) {
				m = p.matcher(str);
				if (!m.matches()) {
					throw new Exception("No cbm position defined in line "
							+ str);
				}
				String[] parts = m.group(1).split(",");
				CBMChunk c = new CBMChunk(parts[0].trim(),
						convertToSetiType(parts[1].trim()), Integer
								.parseInt(parts[2].trim()), Integer
								.parseInt(parts[3].trim()));
				cbms.add(c);
			}
			in.close();
			return new TemplateInfo(cbms, channels);
		} else if (ignoreMissingCBMpos) {
			return null;
		} else {
			throw new Exception(patName + ".asc does not exist!");
		}
	}

	/**
	 * @param symbolList
	 * @param symbolNames
	 * @return
	 */
	private static VArrayList<Symbol> selectSymbols(
			VHashMap<Symbol> symbolList, VArrayList<String> symbolNames) {
		VArrayList<Symbol> settings = new VArrayList<Symbol>();
		for (String n : symbolNames) {
			settings.add(symbolList.get(n));
		}
		return settings;
	}
}
