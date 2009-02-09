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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeAdaptor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolVisitorPrint;
import org.schreibubi.symbol.SymbolVisitorPrintAndEscape;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;

import antlr.RecognitionException;

/**
 * @author Jörg Werner
 */
public class EvalGenCombinations {

	static final TreeAdaptor	adaptor	= new CommonTreeAdaptor() {
		@Override
		public Object create(Token payload) {
			return new DebugCommonTree(payload);
		}
	};

	/**
	 * @param inreader
	 * @return symbolTableLines
	 */
	public static VArrayList<VHashMap<Symbol>> exec(Reader inreader,
			File includeDir, VHashMap<String> options) {
		try {

			CharStream input = new ANTLRReaderStream(inreader);
			EvalGenCombinationsLexer lex = new EvalGenCombinationsLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lex);
			EvalGenCombinationsParser parser = new EvalGenCombinationsParser(
					tokens);
			parser.setTreeAdaptor(adaptor);
			EvalGenCombinationsParser.blocks_return r = parser.blocks();
			System.out.println("tree: " + ((Tree) r.getTree()).toStringTree());

			DebugCommonTree t = (DebugCommonTree) r.getTree();

			/*
			 * EvalGenCombinationsTransformer evalGenCombinationsTransformer =
			 * new EvalGenCombinationsTransformer();
			 * evalGenCombinationsTransformer
			 * .blocks(evalGenCombinationsParser.getAST());
			 */
			// ASTFrame af = new ASTFrame("Tree", t);
			// af.setVisible(true);
			/*
			 * DumpASTVisitor visitor = new DumpASTVisitor();
			 * visitor.visit(parser.getAST());
			 */
			CommonTreeNodeStream nodes = new CommonTreeNodeStream(adaptor, t);
			CheckTreeWalker checkWalker = new CheckTreeWalker(nodes);
			CheckTreeWalker.treeadaptor = adaptor;
			checkWalker.blocks();

			nodes.rewind();
			AnnotateTreeWalker annotateWalker = new AnnotateTreeWalker(nodes);
			int possibleCombinations = annotateWalker.blocks();
			System.out.println("Possible Combinations " + possibleCombinations);
			System.out.println("Tree: " + t.toStringTree());
			PrintWriter pw = new PrintWriter(System.out);
			GetCombinationTreeWalker generateWalker = new GetCombinationTreeWalker(
					nodes);

			for (int c = 0; c < possibleCombinations; c++) {
				nodes.rewind();
				VHashMap<Symbol> symbolTable = new VHashMap<Symbol>();
				VHashMap<Symbol> globalSymbolTable = new VHashMap<Symbol>();
				generateWalker.setSymbolTable(symbolTable);
				generateWalker.setGlobalSymbolTable(globalSymbolTable);
				generateWalker.blocks(c);

				SymbolVisitorPrint symbolVisitorPrinter = new SymbolVisitorPrint(
						pw);
				symbolTable.accept(symbolVisitorPrinter);
			}
			pw.close();
			// DOTTreeGenerator gen = new DOTTreeGenerator();
			// StringTemplate st = gen.toDOT(t);
			// PrintWriter pw = new PrintWriter(new BufferedWriter(new
			// FileWriter("C:\\UserData\\tree.dot")));
			// pw.println(st);
			// pw.close();

			/*
			 * walker.setGlobalSymbolTable(new VHashMap<Symbol>());
			 * 
			 * PrintWriter pw = new PrintWriter(new FileWriter(outfile));
			 * SymbolVisitorPrint symbolVisitorPrint = new
			 * SymbolVisitorPrint(pw);
			 * 
			 * for (VArrayList<AlternativesCommonTreeSet> block : blocks) {
			 * VArrayList<Integer> alts = new VArrayList<Integer>(); for
			 * (AlternativesCommonTreeSet set : block) {
			 * alts.add(set.getNumberOfAlternatives()); } Combination c = new
			 * Combination(alts); c.reset();
			 * 
			 * for (; c.hasNext();) { VHashMap<Symbol> symbolTable = new
			 * VHashMap<Symbol>(); walker.setSymbolTable(symbolTable);
			 * 
			 * VArrayList<Integer> combination = c.getNextCombination();
			 * 
			 * for (int j = 0; j < block.size(); j++) { VArrayList<String>
			 * setKeys = block.get(j).getKeys(); VArrayList<AST> setValues =
			 * block.get(j).getAlternative(combination.get(j)); if
			 * (setKeys.size() != setValues.size()) throw new
			 * Exception("Number of Keys is unequal to number of values");
			 * 
			 * for (int k = 0; k < setKeys.size(); k++) { Symbol res =
			 * walker.expr(setValues.get(k)); res.setName(setKeys.get(k));
			 * symbolTable.put(setKeys.get(k), res); // res.accept(
			 * symbolVisitorPrint ); // pw.print( " " ); } }
			 * symbolTable.accept(symbolVisitorPrint); } } pw.close();
			 */
		} catch (RecognitionException e) {
			System.out
					.println("EvalGenCombinations RecognitionException: " + e);
		} catch (Exception e) {
			System.out.println("EvalGenCombinations error: " + e);
		}

		
		
		
		// Old implementation
//		try {
//
//			EvalGenCombinationsLexer evalGenCombinationsLexer = new EvalGenCombinationsLexer(
//					new BufferedReader(inreader));
//			EvalGenCombinationsParser evalGenCombinationsParser = new EvalGenCombinationsParser(
//					evalGenCombinationsLexer);
//			evalGenCombinationsParser.setIncludeDir(includeDir);
//			evalGenCombinationsParser.blocks();
//
//			// ASTFactory factory = new ASTFactory();
//			// AST r = factory.create(0, "AST ROOT");
//			// r.setFirstChild(evalGenCombinationsParser.getAST());
//			// ASTFrame frame = new ASTFrame("before", r);
//			// frame.setVisible(true);
//
//			/*
//			 * DumpASTVisitor visitor = new DumpASTVisitor();
//			 * visitor.visit(parser.getAST());
//			 */
//
//			EvalGenCombinationsTreeWalker evalGenCombinationsWalker = new EvalGenCombinationsTreeWalker();
//			evalGenCombinationsWalker.setOptions(options);
//			VArrayList<VArrayList<AlternativesASTSet>> blocks = evalGenCombinationsWalker
//					.blocks(evalGenCombinationsParser.getAST());
//			evalGenCombinationsWalker
//					.setGlobalSymbolTable(new VHashMap<Symbol>());
//
//			VArrayList<VHashMap<Symbol>> symbolTableLines = new VArrayList<VHashMap<Symbol>>();
//
//			for (VArrayList<AlternativesASTSet> block : blocks) {
//				VArrayList<Integer> alts = new VArrayList<Integer>();
//				for (AlternativesASTSet set : block) {
//					alts.add(set.getNumberOfAlternatives());
//				}
//				Combination c = new Combination(alts);
//				c.reset();
//
//				for (; c.hasNext();) {
//					VHashMap<Symbol> symbolTable = new VHashMap<Symbol>();
//					evalGenCombinationsWalker.setSymbolTable(symbolTable);
//
//					VArrayList<Integer> combination = c.getNextCombination();
//
//					for (int j = 0; j < block.size(); j++) {
//						VArrayList<String> setKeys = block.get(j).getKeys();
//						VArrayList<AST> setValues = block.get(j)
//								.getAlternative(combination.get(j));
//						if (setKeys.size() != setValues.size())
//							throw new Exception("Number of Keys "
//									+ setKeys.toString()
//									+ " is unequal to number of values "
//									+ setValues.toString());
//
//						for (int k = 0; k < setKeys.size(); k++) {
//							Symbol res = evalGenCombinationsWalker
//									.expr(setValues.get(k));
//							res.setName(setKeys.get(k));
//							symbolTable.put(setKeys.get(k), res.clone());
//							// res.accept( symbolVisitorPrint );
//							// pw.print( " " );
//						}
//
//					}
//					symbolTableLines.add(symbolTable);
//				}
//			}
//			return symbolTableLines;
//		} catch (TokenStreamException e) {
//			System.out
//					.println("EvalGenCombinations TokenStreamException: " + e);
//		} catch (RecognitionException e) {
//			System.out
//					.println("EvalGenCombinations RecognitionException: " + e);
//		} catch (Exception e) {
//			System.out.println("EvalGenCombinations error: " + e);
//		}
		return null;
	}

	/**
	 * @param infile
	 * @param outfile
	 * @throws Exception
	 */
	public static void execAndWriteToFile(File infile, File outfile,
			File includeDir, VHashMap<String> options) throws Exception {
		PrintWriter pw = new PrintWriter(new FileWriter(outfile));
		SymbolVisitorPrintAndEscape symbolVisitorPrintAndEscape = new SymbolVisitorPrintAndEscape(
				pw);

		VArrayList<VHashMap<Symbol>> symbolTableLines = exec(new FileReader(
				infile), includeDir, options);
		for (VHashMap<Symbol> symbolTable : symbolTableLines) {
			symbolTable.accept(symbolVisitorPrintAndEscape);
		}
		pw.close();
	}

	/**
	 * GenerateCombinations method
	 * 
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		String infile = "";
		String outfile = "";
		Options options = new Options();
		try {
			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.withLongOpt("infile").isRequired()
					.withDescription("input file").hasArg().withArgName("file")
					.withValueSeparator('=').create('i'));
			options.addOption(OptionBuilder.withLongOpt("outfile").isRequired()
					.withDescription("output file").hasArg()
					.withArgName("file").withValueSeparator('=').create('o'));
			options.addOption(OptionBuilder.withLongOpt("version")
					.withDescription("version").create('v'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("v")) {
				Info.printVersion("EvalGenCombinations");
				Runtime.getRuntime().exit(0);
			}
			if (line.hasOption("i")) {
				infile = line.getOptionValue("i");
			}
			if (line.hasOption("o")) {
				outfile = line.getOptionValue("o");
			}
			VHashMap<String> optionsFromEvalGenWalker = null;
			exec(new FileReader(infile), new File("."),
					optionsFromEvalGenWalker);
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("EvalGenCombinations"),
					options);
		} catch (Exception e) {
			System.out.println("EvalGenCombinations error: " + e);
		}
	}
}
