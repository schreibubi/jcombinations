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
package org.schreibubi.JCombinationsTools.binaryPatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ListIterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.chunk.ChunkVisitorPatch;
import org.schreibubi.JCombinationsTools.chunk.Patch;
import org.schreibubi.JCombinationsTools.patchWalker.PatchLexer;
import org.schreibubi.JCombinationsTools.patchWalker.PatchParser;
import org.schreibubi.JCombinationsTools.patchWalker.PatchTreeWalker;
import org.schreibubi.visitor.VArrayList;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * BinaryPatch program
 * 
 * @author Jörg Werner
 */
public class BinaryPatch {

	/**
	 * Applies a patch.
	 * 
	 * @param patch
	 *            Name of the patch file. For the format of the patch file
	 *            {@link org.schreibubi.JCombinationsTools.binaryDiff}
	 */
	public static void exec(String patch) {
		try {
			File f = new File(patch);
			if (!f.exists()) {
				throw new Exception(patch + " does not exist");
			}
			PatchLexer patchLexer = new PatchLexer(new BufferedReader(
					new FileReader(f)));
			PatchParser patchParser = new PatchParser(patchLexer);
			patchParser.patches();

			/*
			 * ASTFactory factory = new ASTFactory(); AST r =
			 * factory.create(0,"AST ROOT"); r.setFirstChild(parser.getAST());
			 * ASTFrame frame = new ASTFrame("Preserve Whitespace Example AST",
			 * r); frame.setVisible(true);
			 */

			PatchTreeWalker patchWalker = new PatchTreeWalker();
			VArrayList<Patch> patches = patchWalker.patches(patchParser
					.getAST());
			String backupName = "";
			byte backup[] = null;
			byte binary[] = null;
			long flength = 0;
			for (ListIterator<Patch> it = patches.listIterator(); it.hasNext();) {
				Patch p = it.next();
				// System.out.print("Patching "+p.getNewName()+"...");
				// read file in memory
				if (backupName.equals(p.getReferenceName())) {
					binary = backup.clone();
				} else {
					File filein = new File(p.getReferenceName());
					FileInputStream instr = new FileInputStream(filein);
					flength = filein.length();
					binary = new byte[(int) flength];
					instr.read(binary);
					instr.close();
					backup = binary.clone();
				}

				// apply patches
				ChunkVisitorPatch chunkVisitorPatch = new ChunkVisitorPatch(
						binary);
				p.getChunks().accept(chunkVisitorPatch);

				// Write patched file
				File fileout = new File(p.getNewName());
				if (fileout.exists()) {
					System.out
							.println("Warning: overwriting already existing file: "
									+ p.getNewName());
				}
				FileOutputStream outstr = new FileOutputStream(fileout);
				outstr.write(binary);
				outstr.close();
				// System.out.println("done");
			}

		} catch (TokenStreamException e) {
			System.out.println("TokenStreamException: " + e);
		} catch (RecognitionException e) {
			System.out.println("RecognitionException: " + e);
		} catch (Exception e) {
			System.out.println("BinaryPatch error: " + e);
		}
	}

	/**
	 * Coordinator routine which is used for calls from the command line.
	 * 
	 * @param args
	 *            patch file
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		try {

			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.withLongOpt("version")
					.withDescription("version").create('v'));

			CommandLine line;
			line = CLparser.parse(options, args);

			if (line.hasOption("v")) {
				Info.printVersion("BinaryPatch");
				Runtime.getRuntime().exit(0);
			}
			String[] leftargs = line.getArgs();

			if (leftargs.length == 1) {
				exec(leftargs[0]);
			} else {
				System.out.println("BinaryPatch <patchfile>");
			}
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("BinaryPatch"), options);
		}
	}
}
