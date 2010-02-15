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
package org.schreibubi.JCombinationsTools.templateEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ListIterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
import org.schreibubi.JCombinationsTools.assignWalker.AssignLexer;
import org.schreibubi.JCombinationsTools.assignWalker.AssignParser;
import org.schreibubi.JCombinationsTools.assignWalker.AssignTreeWalker;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;

/**
 * @author Jörg Werner
 */
public class TemplateEngine {

	public static VArrayList<String> exec(File variablesFile,
			boolean generateSeparateFiles, String outputFilenameOrVariable,
			boolean multiTemplate, String templateFilenameOrVariable,
			File inputDir, File outputDir, File headerFile, File footerFile)
			throws Exception {
		if (!variablesFile.exists()) {
			throw new Exception(variablesFile.toString() + " does not exist");
		}

		AssignLexer assignLexer = new AssignLexer(new BufferedReader(
				new FileReader(variablesFile)));
		AssignParser assignParser = new AssignParser(assignLexer);
		assignParser.lines();

		AssignTreeWalker assignTreeWalker = new AssignTreeWalker();
		VArrayList<VHashMap<Symbol>> symbolTableLines = null;
		symbolTableLines = assignTreeWalker.lines(assignParser.getAST());
		return exec(symbolTableLines, generateSeparateFiles,
				outputFilenameOrVariable, multiTemplate,
				templateFilenameOrVariable, inputDir, outputDir, headerFile,
				footerFile);
	}

	/**
	 * @param symbolTable
	 *            symbolTable containing the variables
	 * @param generateSeparateFiles
	 *            If true, then each instance of a template is put into a
	 *            separate file. The name of the file is determined by the
	 *            variable name passed in name. If false everything is put into
	 *            one file, which name is passed in name.
	 * @param outputFilenameOrVariable
	 *            Name of the output file if generateSeparateFiles is false or
	 *            the variable name which contains the filename if it is true.
	 * @param multiTemplate
	 *            template name is taken from a variable
	 * @param templateFilenameOrVariable
	 *            Name of the template file if multiTemplate is false or the
	 *            variable name which contains the template name to use.
	 * @param inputDir
	 *            TODO
	 * @param outputDir
	 *            Name of an output directory to use.
	 * @param headerFile
	 *            Filename of the header to prepend
	 * @param footerFile
	 *            Filename of the footer to append
	 * @return List of files generated
	 * @throws Exception
	 */
	public static VArrayList<String> exec(
			VArrayList<VHashMap<Symbol>> symbolTableLines,
			boolean generateSeparateFiles, String outputFilenameOrVariable,
			boolean multiTemplate, String templateFilenameOrVariable,
			File inputDir, File outputDir, File headerFile, File footerFile)
			throws Exception {
		VArrayList<String> fns = new VArrayList<String>();
		char footer[] = null;
		if (footerFile != null) {
			if (!footerFile.exists()) {
				throw new Exception(footerFile.toString() + " does not exist");
			}
			FileReader fr = new FileReader(footerFile);
			footer = new char[(int) footerFile.length()];
			fr.read(footer);
			fr.close();
		}
		char header[] = null;
		if (headerFile != null) {
			if (!headerFile.exists()) {
				throw new Exception(headerFile.toString() + " does not exist");
			}
			FileReader fr = new FileReader(headerFile);
			header = new char[(int) headerFile.length()];
			fr.read(header);
			fr.close();
		}

		TemplateTreeWalker TemplateWalker = null;
		TemplateParser TemplateParser = null;
		if (!multiTemplate) {
			File templateFile = new File(templateFilenameOrVariable);
			if (!templateFile.exists()) {
				throw new Exception(templateFilenameOrVariable
						+ " does not exist");
			}

			TemplateLexer TemplateLexer = new TemplateLexer(new BufferedReader(
					new FileReader(templateFile)));
			TemplateParser = new TemplateParser(TemplateLexer);
			TemplateParser.statements();

			TemplateWalker = new TemplateTreeWalker();
		}

		PrintWriter pw = null;
		if (!generateSeparateFiles) {
			File s = new File(outputDir, outputFilenameOrVariable);
			pw = new PrintWriter(new FileWriter(s));
			if (header != null) {
				pw.write(header);
			}
			fns.add(s.getPath());
		}

		for (ListIterator<VHashMap<Symbol>> i = symbolTableLines.listIterator(); i
				.hasNext();) {
			FileWriter fw = null;
			/* get variables used to fill out the actual template */
			VHashMap<Symbol> sT = i.next();
			if (generateSeparateFiles) {
				String n = evalTemplateExpression(outputFilenameOrVariable, sT);
				File s = new File(outputDir, n);
				fw = new FileWriter(s);
				pw = new PrintWriter(fw);
				if (header != null) {
					pw.write(header);
				}
				fns.add(s.getPath());
			}

			/*
			 * if we have multiple templates in one run reread the template
			 */
			if (multiTemplate) {
				String multiTemplateFilename = evalTemplateExpression(
						templateFilenameOrVariable, sT);
				File templateFile = FileNameLookupSingleton.getInstance()
						.lookup(inputDir, multiTemplateFilename);
				if (!templateFile.exists()) {
					throw new Exception(multiTemplateFilename
							+ " does not exist");
				}

				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(new File(multiTemplateFilename)));
				TemplateLexer TemplateLexer = new TemplateLexer(bufferedReader);
				TemplateParser = new TemplateParser(TemplateLexer);
				TemplateParser.statements();
				bufferedReader.close();
				TemplateWalker = new TemplateTreeWalker();
			}

			TemplateWalker.statements(TemplateParser.getAST(), sT, pw);
			if (generateSeparateFiles) {
				if (footer != null) {
					pw.write(footer);
				}
				pw.close();
				fw.close();
			}
		}

		if (!generateSeparateFiles) {
			if (footer != null) {
				pw.write(footer);
			}
			pw.close();
		}
		return fns;
	}

	/**
	 * TemplateEngine
	 * 
	 * @param args
	 *            assignments template
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		try {
			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.isRequired().withLongOpt(
					"output-filename").withDescription(
					"[out] which variable is used for nameing the files")
					.hasArg().withArgName("variable name").create('o'));
			options
					.addOption(OptionBuilder
							.withLongOpt("multifile")
							.withDescription(
									"output template to multiple files, -o contains variable name to use for filename")
							.create('m'));
			options
					.addOption(OptionBuilder
							.withLongOpt("multitemplate")
							.withDescription(
									"use multiple templates, -t contains variable name to use for template")
							.create('u'));
			options.addOption(OptionBuilder.isRequired()
					.withLongOpt("template").withDescription(
							"[in] the file containing the template to process")
					.hasArg().withArgName("file").create('t'));
			options
					.addOption(OptionBuilder
							.isRequired()
							.withLongOpt("variables")
							.withDescription(
									"[in] a file containing the values of the variables which should be replaced in the template")
							.hasArg().withArgName("file").create('v'));
			options
					.addOption(OptionBuilder
							.withLongOpt("outputDir")
							.withDescription(
									"output directory to write the generated files to.")
							.hasArg().withArgName("file").create('d'));
			options.addOption(OptionBuilder.withLongOpt("header")
					.withDescription("header to insert in each file").hasArg()
					.withArgName("file").create('h'));
			options.addOption(OptionBuilder.withLongOpt("footer")
					.withDescription("footer to insert in each file").hasArg()
					.withArgName("file").create('f'));
			options.addOption(OptionBuilder.withLongOpt("version")
					.withDescription("version").create('v'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("v")) {
				Info.printVersion("TemplateEngine");
				Runtime.getRuntime().exit(0);
			}
			boolean generateSeparateFilesSwitch = false;
			if (line.hasOption("m")) {
				generateSeparateFilesSwitch = true;
			}
			boolean multiTemplateSwitch = false;
			if (line.hasOption("u")) {
				multiTemplateSwitch = true;
			}
			String outputFilenameOrVariable = "";
			if (line.hasOption("o")) {
				outputFilenameOrVariable = line.getOptionValue("o");
			}
			String templateFilenameOrVariable = "";
			if (line.hasOption("t")) {
				templateFilenameOrVariable = line.getOptionValue("t");
			}
			String variablesFilename = "";
			if (line.hasOption("v")) {
				variablesFilename = line.getOptionValue("v");
			}
			String outputDir = null;
			if (line.hasOption("d")) {
				outputDir = line.getOptionValue("d", null);
			}
			String header = null;
			if (line.hasOption("h")) {
				header = line.getOptionValue("h", null);
			}
			String footer = null;
			if (line.hasOption("f")) {
				outputDir = line.getOptionValue("f", null);
			}

			File variablesFile = new File(variablesFilename);
			File headerFile = new File(header);
			File footerFile = new File(footer);
			File inputDirFile = new File(".");
			File outputDirFile = new File(outputDir);

			exec(variablesFile, generateSeparateFilesSwitch,
					outputFilenameOrVariable, multiTemplateSwitch,
					templateFilenameOrVariable, inputDirFile, outputDirFile,
					headerFile, footerFile);
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("TemplateEngine"),
					options);
		} catch (Exception e) {
			System.out.println("TemplateEngine error: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static String evalTemplateExpression(String fn, VHashMap<Symbol> sT)
			throws Exception {

		TemplateLexer TemplateLexer = new TemplateLexer(new StringReader(fn));
		TemplateParser TemplateParser = new TemplateParser(TemplateLexer);
		TemplateParser.statements();

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		/*
		 * ASTFactory factory = new ASTFactory(); AST r =
		 * factory.create(0,"AST ROOT");
		 * r.setFirstChild(TemplateParser.getAST()); ASTFrame frame = new
		 * ASTFrame("Preserve Whitespace Example AST", r);
		 * frame.setVisible(true);
		 */

		TemplateTreeWalker TemplateWalker = new TemplateTreeWalker();
		TemplateWalker.statements(TemplateParser.getAST(), sT, pw);
		return sw.toString();
	}
}