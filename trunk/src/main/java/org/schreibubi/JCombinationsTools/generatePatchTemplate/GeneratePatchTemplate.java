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
package org.schreibubi.JCombinationsTools.generatePatchTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.SymbolVisitors.SymbolVisitorExtractAndMangle;
import org.schreibubi.JCombinationsTools.assignWalker.AssignLexer;
import org.schreibubi.JCombinationsTools.assignWalker.AssignParser;
import org.schreibubi.JCombinationsTools.assignWalker.AssignTreeWalker;
import org.schreibubi.JCombinationsTools.chunk.Chunk;
import org.schreibubi.JCombinationsTools.chunk.ChunkVisitorCheckIfAllHaveNames;
import org.schreibubi.JCombinationsTools.chunk.ChunkVisitorDiffTemplate;
import org.schreibubi.JCombinationsTools.chunk.ChunkVisitorRelate;
import org.schreibubi.JCombinationsTools.chunk.Patch;
import org.schreibubi.JCombinationsTools.mangle.Mangle;
import org.schreibubi.JCombinationsTools.mangle.MangleMRS;
import org.schreibubi.JCombinationsTools.patchWalker.PatchLexer;
import org.schreibubi.JCombinationsTools.patchWalker.PatchParser;
import org.schreibubi.JCombinationsTools.patchWalker.PatchTreeWalker;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolVisitorPrint;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * @author Jörg Werner
 */
public class GeneratePatchTemplate {

	/**
	 * @param diffResultName
	 * @param possibleChunksName
	 * @param diffTemplateName
	 * @param possibleCombinationsIn
	 * @param templateNameFilter
	 * @param possibleCombinationsOut
	 * @param seti
	 */
	public static void exec(String diffResultName, String possibleChunksName, String diffTemplateName,
			String possibleCombinationsIn, String templateNameFilter, String possibleCombinationsOut, String setiXmlFile) {
		try {
			// parse and treewalk BinaryDiff result file
			File diffResultFile = new File(diffResultName);
			if (!diffResultFile.exists())
				throw new Exception(diffResultName + " does not exist.");

			PatchLexer binDiffLexer = new PatchLexer(new BufferedReader(new FileReader(diffResultFile)));
			PatchParser binDiffParser = new PatchParser(binDiffLexer);
			binDiffParser.patch();
			PatchTreeWalker binDiffTreeWalker = new PatchTreeWalker();
			Patch patch = binDiffTreeWalker.patch(binDiffParser.getAST());
			VArrayList<Chunk> chunksList = patch.getChunks();

			// parse and treewalk allPossibleChunks file
			File possibleChunksFile = new File(possibleChunksName);
			if (!possibleChunksFile.exists())
				throw new Exception(possibleChunksName + " does not exist.");

			PatchLexer possibleChunksLexer = new PatchLexer(new BufferedReader(new FileReader(possibleChunksFile)));
			PatchParser possibleChunksParser = new PatchParser(possibleChunksLexer);
			possibleChunksParser.patch();
			PatchTreeWalker possibleChunksTreeWalker = new PatchTreeWalker();
			Patch patch2 = possibleChunksTreeWalker.patch(possibleChunksParser.getAST());
			VArrayList<Chunk> possibleChunksList = patch2.getChunks();

			// relate between found chunks (in chunksList) and all possible
			// chunks (in possibleChunksList)
			ChunkVisitorRelate chunkVisitorRel = new ChunkVisitorRelate(possibleChunksList);
			chunksList.accept(chunkVisitorRel);

			// in the chunksList are now the names of the corresponding
			// variables. Need to check if we really could relate all in
			// chunksList!
			ChunkVisitorCheckIfAllHaveNames check = new ChunkVisitorCheckIfAllHaveNames();
			chunksList.accept(check);
			// output the diff-Template
			PrintWriter pw = new PrintWriter(new FileWriter(diffTemplateName));
			pw.println("--- " + patch.getReferenceName());
			pw.println("+++ $PATNAME_UNMANGLED$%LOWERCASE%.mpa");
			ChunkVisitorDiffTemplate chunkVisitorDiffTemp = new ChunkVisitorDiffTemplate(pw);
			chunksList.accept(chunkVisitorDiffTemp);
			pw.close();

			File variablesFile = new File(possibleCombinationsIn);
			if (!variablesFile.exists())
				throw new Exception(possibleCombinationsIn + " does not exist");

			AssignLexer assignLexer = new AssignLexer(new BufferedReader(new FileReader(variablesFile)));
			AssignParser assignParser = new AssignParser(assignLexer);
			assignParser.lines();

			AssignTreeWalker assignTreeWalker = new AssignTreeWalker();
			VArrayList<VHashMap<Symbol>> symbolTableLines = null;
			symbolTableLines = assignTreeWalker.lines(assignParser.getAST());

			Mangle mangle = null;
			if (setiXmlFile.length() > 0)
				throw new Exception("Seti Patching not supported any more");
			else {
				mangle = new MangleMRS(0x040F0000);
			}
			SymbolVisitorExtractAndMangle extractMangleVisitor = new SymbolVisitorExtractAndMangle(chunksList, mangle);
			for (ListIterator<VHashMap<Symbol>> iter = symbolTableLines.listIterator(); iter.hasNext();) {
				VHashMap<Symbol> symbolTableLine = iter.next();
				Symbol templateSymbol = symbolTableLine.get("TEMPLATE");
				if (templateSymbol != null) {
					String template = templateSymbol.convertToString().getValue();
					if (!template.equalsIgnoreCase(templateNameFilter)) {
						iter.remove();
					} else {
						Symbol patname = symbolTableLine.get("PATNAME").clone();
						patname.setName("PATNAME_UNMANGLED");
						symbolTableLine.accept(extractMangleVisitor);
						symbolTableLine.put("PATNAME_UNMANGLED", patname);
					}
				} else {
					iter.remove();
				}
			}

			// remove duplicate lines, so not to unnecessary patch more than
			// once with the same file as result...
			// sort lines

			class symbolTableLineComparator implements Comparator<VHashMap<Symbol>> {
				public int compare(VHashMap<Symbol> o1, VHashMap<Symbol> o2) {
					StringWriter o1sw = new StringWriter();
					StringWriter o2sw = new StringWriter();
					PrintWriter o1pw = new PrintWriter(o1sw);
					PrintWriter o2pw = new PrintWriter(o2sw);
					SymbolVisitorPrint svp1 = new SymbolVisitorPrint(o1pw);
					SymbolVisitorPrint svp2 = new SymbolVisitorPrint(o2pw);
					try {
						o1.accept(svp1);
						o2.accept(svp2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return o1sw.toString().compareTo(o2sw.toString());
				}
			}

			symbolTableLineComparator stlc = new symbolTableLineComparator();
			Collections.sort(symbolTableLines, stlc);
			// remove duplicates
			VHashMap<Symbol> previous = null;
			for (ListIterator<VHashMap<Symbol>> iter = symbolTableLines.listIterator(); iter.hasNext();) {
				VHashMap<Symbol> symbolTableLine = iter.next();
				if (previous != null) {
					if (stlc.compare(previous, symbolTableLine) == 0) {
						iter.remove();
					} else {
						previous = symbolTableLine;
					}
				} else {
					previous = symbolTableLine;
				}
			}
			pw = new PrintWriter(new FileWriter(possibleCombinationsOut));
			SymbolVisitorPrint symbolVisitorPrint = new SymbolVisitorPrint(pw);
			for (ListIterator<VHashMap<Symbol>> i = symbolTableLines.listIterator(); i.hasNext();) {
				i.next().accept(symbolVisitorPrint);
			}
			pw.close();

		} catch (TokenStreamException e) {
			System.out.println("generatePatchTemplate TokenStreamException: " + e);
		} catch (RecognitionException e) {
			System.out.println("generatePatchTemplate RecognitionException: " + e);
		} catch (JiBXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("generatePatchTemplate error: " + e);
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 *            binaryDiffResult allPossibleChunks difftemplate
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		String diffResultFile = "";
		String diffTemplateFile = "";
		String possibleChunksFile = "";
		String possibleCombinationsIn = "";
		String possibleCombinationsOut = "";
		String seti = "";
		String templateNameFilter = "";
		Options options = new Options();
		try {
			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.isRequired().withLongOpt("diffResult").withDescription(
					"[in] file containing the result of the binary diff").hasArg().withArgName("file").create('d'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("possibleChunks").withDescription(
					"[in] file containing the possible chunks").hasArg().withArgName("file").create('p'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("possibleCombinationsIn").withDescription(
					"[in] list of all possible combinations").hasArg().withArgName("file").create('i'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("filter").withDescription(
					"filter only test which have this value in the TEMPLATE variable").hasArg().withArgName("file")
					.create('f'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("possibleCombinationsOut").withDescription(
					"[out] list of all possible combinations").hasArg().withArgName("file").create('o'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("diffTemplate").withDescription(
					"[out] template for generating the patch chunks").hasArg().withArgName("file").create('t'));
			options.addOption(OptionBuilder.withLongOpt("seti").withDescription("[in] enable seti-chain patching")
					.hasArg().withArgName("file").create('s'));
			options.addOption(OptionBuilder.withLongOpt("version").withDescription("version").create('v'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("v")) {
				Info.printVersion("GeneratepatchTemplate");
				Runtime.getRuntime().exit(0);
			}
			if (line.hasOption("d")) {
				diffResultFile = line.getOptionValue("d");
			}
			if (line.hasOption("t")) {
				diffTemplateFile = line.getOptionValue("t");
			}
			if (line.hasOption("p")) {
				possibleChunksFile = line.getOptionValue("p");
			}
			if (line.hasOption("i")) {
				possibleCombinationsIn = line.getOptionValue("i");
			}
			if (line.hasOption("o")) {
				possibleCombinationsOut = line.getOptionValue("o");
			}
			if (line.hasOption("f")) {
				templateNameFilter = line.getOptionValue("f");
			}
			if (line.hasOption("s")) {
				seti = line.getOptionValue("s");
			}

			exec(diffResultFile, possibleChunksFile, diffTemplateFile, possibleCombinationsIn, templateNameFilter,
					possibleCombinationsOut, seti);

		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("GeneratePatchTemplate"), options);
		} catch (Exception e) {
			System.out.println("GeneratePatchTemplate error: " + e);
			e.printStackTrace();
		}
	}

}
