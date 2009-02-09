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
package org.schreibubi.JCombinationsTools.generateBinaryDiffValues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.SymbolVisitors.SymbolVisitorDiffVal;
import org.schreibubi.JCombinationsTools.assignWalker.AssignLexer;
import org.schreibubi.JCombinationsTools.assignWalker.AssignParser;
import org.schreibubi.JCombinationsTools.assignWalker.AssignTreeWalker;
import org.schreibubi.JCombinationsTools.chunk.Chunk;
import org.schreibubi.JCombinationsTools.chunk.ChunkVisitorPrint;
import org.schreibubi.JCombinationsTools.mangle.Mangle;
import org.schreibubi.JCombinationsTools.mangle.MangleMRS;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolVisitorPrint;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * GenerateBinaryDiffValues program
 * 
 * @author Jörg Werner
 */
public class GenerateBinaryDiffValues {

	/**
	 * @param variablesInName
	 *            list of all the variables used in the binary diff
	 * @param outputDir
	 *            output dir
	 * @param templateVariable
	 *            variable which contains the template name
	 * @param seti
	 *            if unequal "" enables seti patching with passed value as filename for the necessary xml file
	 * @return list off templates for which testvalues were generated
	 */
	public static VArrayList<String> exec(String variablesInName, String outputDir, String setiXmlFile,
			String templateVariable) {
		VArrayList<String> usedTemplates = new VArrayList<String>();
		try {
			File variablesInFile = new File(variablesInName);
			if (!variablesInFile.exists())
				throw new Exception(variablesInName + " does not exist.");

			AssignLexer assignLexer = new AssignLexer(new BufferedReader(new FileReader(variablesInFile)));
			AssignParser assignParser = new AssignParser(assignLexer);
			assignParser.lines();

			/*
			 * ASTFactory factory = new ASTFactory(); AST r = factory.create( 0, "AST ROOT" ); r.setFirstChild(
			 * assignParser.getAST() ); ASTFrame frame = new ASTFrame( "Preserve Whitespace Example AST", r );
			 * frame.setVisible( true );
			 */

			AssignTreeWalker assignTreeWalker = new AssignTreeWalker();
			VHashMap<VHashMap<Symbol>> typeSizeMap = assignTreeWalker.maxlines(assignParser.getAST(), templateVariable);

			for (String template : typeSizeMap.keySet()) {
				usedTemplates.add(template);
				Mangle mangle = null;
				if (setiXmlFile.length() > 0)
					throw new Exception("Seti Patching not supported any more");
				else {
					mangle = new MangleMRS(0x040F0000);
				}
				// Visit typeSizeMap with visitorDiffVal to generate List of
				// expected Chunks
				VArrayList<Chunk> allPossibleChunks = new VArrayList<Chunk>();
				VArrayList<Symbol> removeSymbolList = new VArrayList<Symbol>();
				VArrayList<Symbol> replaceSymbolList = new VArrayList<Symbol>();
				SymbolVisitorDiffVal visitorDiffVal = new SymbolVisitorDiffVal(allPossibleChunks, removeSymbolList,
						replaceSymbolList, mangle);
				VHashMap<Symbol> typeSizeMapTemplate = typeSizeMap.get(template);
				typeSizeMapTemplate.accept(visitorDiffVal);

				// Print out lines for template engine
				PrintWriter pw = new PrintWriter(new FileWriter(new File(outputDir, template + ".testvalues")));

				// remove
				SymbolVisitorPrint visitorPrinter = new SymbolVisitorPrint(pw);
				removeSymbolList.accept(visitorPrinter);

				// replace
				visitorPrinter = new SymbolVisitorPrint(pw);
				replaceSymbolList.accept(visitorPrinter);
				pw.close();

				// Print out list of possible chunks
				pw = new PrintWriter(new FileWriter(new File(outputDir, template + ".possiblechunks")));
				pw.println("--- dummy");
				pw.println("+++ dummy");
				ChunkVisitorPrint chunkVisitorPrinter = new ChunkVisitorPrint(pw);
				allPossibleChunks.accept(chunkVisitorPrinter);
				pw.close();
			}
		} catch (TokenStreamException e) {
			System.out.println("GenerateBinaryDiffValues TokenStreamException: " + e);
		} catch (RecognitionException e) {
			System.out.println("GenerateBinaryDiffValues RecognitionException: " + e);
		} catch (JiBXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("GenerateBinaryDiffValues error: " + e);
			e.printStackTrace();
		}
		return usedTemplates;
	}

	/**
	 * This program takes the file with all possible combinations of variables and creates two set of variables for
	 * relating the variable names to positions in the binary diff. Additionally a patch is created containing the
	 * expected chunks from a binary diff.
	 * 
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		String variablesInName = "";
		String outputDir = "";
		String seti = "";
		Options options = new Options();
		try {
			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.isRequired().withLongOpt("variablesIn").withDescription(
					"[in] file containing the variables").hasArg().withArgName("file").create('i'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("dir").withDescription("[out] output directory")
					.hasArg().withArgName("file").create('d'));
			options.addOption(OptionBuilder.withLongOpt("seti").withDescription("[in] enable seti-chain patching")
					.hasArg().withArgName("file").create('s'));
			options.addOption(OptionBuilder.withLongOpt("version").withDescription("version").create('v'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("v")) {
				Info.printVersion("GenerateBinaryDiffValues");
				Runtime.getRuntime().exit(0);
			}
			if (line.hasOption("i")) {
				variablesInName = line.getOptionValue("i");
			}
			if (line.hasOption("s")) {
				seti = line.getOptionValue("s");
			}
			if (line.hasOption("d")) {
				outputDir = line.getOptionValue("d");
			}

			exec(variablesInName, outputDir, seti, "TEMPLATE");
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("GenerateBinaryDiffValues"), options);
		} catch (Exception e) {
			System.out.println("GenerateBinaryDiffValues error: " + e);
			e.printStackTrace();
		}

	}
}
