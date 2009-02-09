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
package org.schreibubi.JCombinationsTools.coordinatorPatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
import org.schreibubi.JCombinationsTools.binaryDiff.BinaryDiff;
import org.schreibubi.JCombinationsTools.binaryPatch.BinaryPatch;
import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinations;
import org.schreibubi.JCombinationsTools.generateBinaryDiffValues.GenerateBinaryDiffValues;
import org.schreibubi.JCombinationsTools.generatePatchTemplate.GeneratePatchTemplate;
import org.schreibubi.JCombinationsTools.settings.SettingsSingleton;
import org.schreibubi.JCombinationsTools.templateEngine.TemplateEngine;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;

import antlr.RecognitionException;
import antlr.TokenStreamException;


/**
 * @author Jörg Werner
 */
public class CoordinatorPatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Info.printVersion("CoordinatorPatch");
		try {
			CoordinatorPatchOptions[] settings = CoordinatorPatchOptions.values();
			SettingsSingleton.initialize("CoordinatorPatch", settings);
			// command line arguments have the highest priority, so they go into level 2
			SettingsSingleton.getInstance().parseArguments(args, 2);

			if (SettingsSingleton.getInstance().getProperty("help").equals("true")) {
				SettingsSingleton.getInstance().displayHelp();
				Runtime.getRuntime().exit(0);
			}

			if (SettingsSingleton.getInstance().getProperty("version").equals("true")) {
				Info.printVersion("CoordinatorPatch");
				Runtime.getRuntime().exit(0);
			}

			String combinationFileName = SettingsSingleton.getInstance().getProperty("combinations");

			// pre-tags
			VArrayList<String> preTags = new VArrayList<String>();
			String preTagsString = SettingsSingleton.getInstance().getProperty("pretags");
			preTags = new VArrayList<String>(Arrays.asList(preTagsString.split(",")));
			// post-tags
			VArrayList<String> postTags = new VArrayList<String>();
			String postTagsString = SettingsSingleton.getInstance().getProperty("posttags");
			postTags = new VArrayList<String>(Arrays.asList(postTagsString.split(",")));

			FileNameLookupSingleton.initialize(preTags, postTags);

			File dir = new File(combinationFileName).getAbsoluteFile().getParentFile();
			File combinationFile = FileNameLookupSingleton.getInstance().lookup(dir, combinationFileName);
			System.out.println("Processing: " + combinationFile.getName());

			System.out.println("Executing: " + combinationFileName);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date today = new Date();
			String tempdir = "/tmp/master" + formatter.format(today) + "/";
			String symbolTableLines = tempdir + "evaluated.combinations";
			String mangled_combinations = tempdir + "mangled.combinations";

			File tempdirectory = new File(tempdir);
			if (!tempdirectory.exists()) {
				tempdirectory.mkdir();
			}

			// Generate all possible combinations
			System.out.print("Generating and evaluation all combinations...");
			VHashMap<String> optionsFromEvalGenWalker = new VHashMap<String>();
			EvalGenCombinations.exec(new FileReader(combinationFileName), dir, optionsFromEvalGenWalker);
			System.out.println("done");

			// now additional default options are known...
			// -----------------------------------------------------------------------------
			String argumentsString = optionsFromEvalGenWalker.get("Coordinator");
			if (argumentsString != null) {
				String[] arguments = argumentsString.split(" ");
				SettingsSingleton.getInstance().parseArguments(arguments, 1);
			}

			if (SettingsSingleton.getInstance().areRequiredOptionsSet() == false) {
				SettingsSingleton.getInstance().displayHelp();
				Runtime.getRuntime().exit(0);
			}

			String conditionFilePrefix = SettingsSingleton.getInstance().getProperty("conditionprefix");
			String conditionFileTemplateName = conditionFilePrefix + ".template";
			String conditionFileHeaderName = conditionFilePrefix + ".header";
			String pcfOutputDir = SettingsSingleton.getInstance().getProperty("pcfdir");

			if (!SettingsSingleton.getInstance().getProperty("conditiontemplate").equals("")) {
				conditionFileTemplateName = SettingsSingleton.getInstance().getProperty("conditiontemplate");
			}
			if (!SettingsSingleton.getInstance().getProperty("conditionheader").equals("")) {
				conditionFileHeaderName = SettingsSingleton.getInstance().getProperty("conditionheader");
			}

			String conditionFileName = "";
			if (!SettingsSingleton.getInstance().getProperty("condition").equals("")) {
				conditionFileName = SettingsSingleton.getInstance().getProperty("condition");
			} else {
				int dotPos = combinationFileName.indexOf(".");
				if (dotPos > -1) {
					conditionFileName = conditionFilePrefix + "_" + combinationFileName.subSequence(0, dotPos);
				} else {
					conditionFileName = conditionFilePrefix + "_" + combinationFileName;
				}
			}

			// Create the condition file
			System.out.print("Creating " + conditionFileName + "...");
			TemplateEngine.exec(new File(symbolTableLines), false, conditionFileName, false, conditionFileTemplateName,
					new File("."), new File(pcfOutputDir), new File(conditionFileHeaderName), null);
			System.out.println("done");

			if (SettingsSingleton.getInstance().getProperty("nopatgen").equals("false")) {
				String setiFileName = SettingsSingleton.getInstance().getProperty("seti");

				// Generate Binary Diff value
				System.out.print("Generating test values for binary diff...");
				VArrayList<String> templateNames;
				templateNames = GenerateBinaryDiffValues.exec(symbolTableLines, tempdir, setiFileName, "TEMPLATE");
				System.out.println("done");

				for (String template : templateNames) {
					String mpadir = SettingsSingleton.getInstance().getProperty("mpadir");

					System.out.print("Generating test patterns from pattern-templates... " + template);
					VArrayList<String> fns = TemplateEngine.exec(new File(tempdir + template + ".testvalues"), true,
							"$PATNAME$%LOWERCASE%.pat", false, template + ".template", new File("."), new File(mpadir),
							null, null);
					System.out.println(" ...done");

					String patBaseName1 = fns.get(0).substring(0, fns.get(0).length() - 4);
					String mpaName1 = patBaseName1 + ".mpa";
					String patBaseName2 = fns.get(1).substring(0, fns.get(1).length() - 4);
					String mpaName2 = patBaseName2 + ".mpa";

					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("asdappattern.mk")));
					pw.println("asdappattern: " + mpaName1 + " " + mpaName2);
					pw.close();
					// now run the pattern compiler
					System.out.print("Running pattern compiler... " + template);
					if (fns.size() != 2)
						throw new Exception("only one or more than two test patterns have been generated!");

					boolean silent = false;
					if (SettingsSingleton.getInstance().getProperty("silent").equals("true")) {
						silent = true;
					}
					String[] makecommand = { "sh", "-c", SettingsSingleton.getInstance().getProperty("exec") };

					compile(makecommand, silent);
					System.out.println(" ...done");

					File mpaFile1 = new File(mpadir, mpaName1);
					File mapFile2 = new File(mpadir, mpaName2);
					if (!mpaFile1.exists())
						throw new Exception("compiling didn't produce expected files " + mpaName1);
					if (!mapFile2.exists())
						throw new Exception("compiling didn't produce expected files " + mpaName2);

					// generate the binary diffs
					System.out.print("Generating binary diff... " + template);
					if (setiFileName.length() > 0) {
						BinaryDiff.exec(mpaName1, mpaName2, tempdir + template + ".diff", 4, 48, 48);
					} else {
						BinaryDiff.exec(mpaName1, mpaName2, tempdir + template + ".diff", 4, 0, 0);
					}
					System.out.println(" ...done");

					// now find out which chunk corresponds to which variable
					// and
					// generate the diff template
					System.out.print("Trying to relate found differences to variables... " + template);
					GeneratePatchTemplate.exec(tempdir + template + ".diff", tempdir + template + ".possiblechunks",
							tempdir + template + ".patchtemplate", symbolTableLines, template, mangled_combinations,
							setiFileName);
					System.out.println(" ...done");
					// }

					// Take the template and run it through the template engine
					// template engine needs a switch for output dir!
					System.out.print("Generating patch... " + template);
					TemplateEngine.exec(new File(mangled_combinations), false, tempdir + template + ".patch", false,
							tempdir + template + ".patchtemplate", new File("."), null, null, null);
					System.out.println(" ...done");

					// generate all the files by patching
					System.out.print("Patching... " + template);
					BinaryPatch.exec(tempdir + template + ".patch");
					System.out.println(" ...done");

					if (SettingsSingleton.getInstance().getProperty("noremove").equals("false")) {
						new File(patBaseName1 + ".pat").delete();
						new File(patBaseName2 + ".pat").delete();
						if (!setiFileName.equals("")) {
							new File(patBaseName1 + ".tmc").delete();
							new File(patBaseName2 + ".tmc").delete();
						}
						new File(patBaseName1 + ".asc").delete();
						new File(patBaseName2 + ".asc").delete();
						mpaFile1.delete();
						mapFile2.delete();
						PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter("asdappattern.mk")));
						pw2.println("asdappattern: ");
						pw2.close();
					}

				}
			}
		} catch (ParseException e) {
		} catch (TokenStreamException e) {
			System.out.println("TokenStreamException: " + e.getMessage());
		} catch (RecognitionException e) {
			System.out.println("RecognitionException: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Coordinator error: " + e.getMessage());
			System.exit(1);
		}
	}

	private static void compile(String[] makecommand, boolean silent) throws IOException, InterruptedException,
			Exception {
		ProcessBuilder pb = new ProcessBuilder(makecommand);
		// Map< String, String > en = pb.environment();
		/*
		 * for ( String string : en.keySet() ) { System.out.println( string + "=" + en.get( string ) ); }
		 */
		pb.redirectErrorStream(true);
		Process p = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		if (!silent) {
			String c;
			while ((c = br.readLine()) != null) {
				System.out.println(c);
			}
		}
		p.waitFor();
		int retVal = p.exitValue();
		if (retVal != 0)
			throw new Exception("Compilation failed!");
	}
}
