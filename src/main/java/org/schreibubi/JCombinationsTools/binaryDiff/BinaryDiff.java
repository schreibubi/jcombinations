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
package org.schreibubi.JCombinationsTools.binaryDiff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.chunk.Chunk;
import org.schreibubi.JCombinationsTools.chunk.ChunkVisitorPrint;
import org.schreibubi.visitor.VArrayList;


/**
 * BinaryDiff program
 * 
 * @author Jörg Werner
 */
public class BinaryDiff {

	/**
	 * @param name1
	 *            first file to diff against
	 * @param name2
	 *            second file to diff against
	 * @param diffout
	 *            output file name to write diff to
	 * @param diffLength
	 *            Length of diffs
	 * @param beforeLength
	 *            TODO
	 * @param afterLength
	 *            TODO
	 */
	public static void exec(String name1, String name2, String diffout, int diffLength, int beforeLength,
			int afterLength) {

		try {

			File file1 = new File(name1);
			File file2 = new File(name2);

			if (!file1.exists())
				throw new Exception(name1 + " does not exist!");
			if (!file2.exists())
				throw new Exception(name2 + " does not exist!");

			long fileLength = file1.length();
			if (fileLength != file2.length())
				throw new Exception("Files have different lengths!");
			int len = (int) fileLength;
			FileInputStream fileStream1 = new FileInputStream(file1);
			FileInputStream fileStream2 = new FileInputStream(file2);

			byte fileOneContent[] = new byte[len];
			fileStream1.read(fileOneContent);
			byte fileTwoContent[] = new byte[len];
			fileStream2.read(fileTwoContent);

			fileStream1.close();
			fileStream2.close();

			VArrayList<Chunk> chunks = new VArrayList<Chunk>();

			int start = -1;
			int i;

			if (fileLength % diffLength != 0)
				throw new Exception("File length is not a multiple of " + diffLength);

			for (i = 0; i < fileLength / diffLength; i++) {
				boolean different = false;
				for (int j = 0; j < diffLength; j++) {
					int byte1 = fileOneContent[i * diffLength + j] & 0xFF;
					int byte2 = fileTwoContent[i * diffLength + j] & 0xFF;
					if (byte1 != byte2)
						different = true;
				}
				if (different) {
					if (start == -1)
						start = i; // begin difference region
				} else if (start > -1) { // end difference region
					createChunk(diffLength, beforeLength, afterLength, fileLength, fileOneContent, fileTwoContent,
							chunks, start, i);
					start = -1;
				}
			}
			if (start > -1) { // file is different until EOF!
				createChunk(diffLength, beforeLength, afterLength, fileLength, fileOneContent, fileTwoContent, chunks,
						start, i);
				start = -1;
			}

			// Write the diff to a file
			PrintWriter pw;
			if (diffout.equals(""))
				pw = new PrintWriter(System.out);
			else
				pw = new PrintWriter(new FileWriter(diffout));
			pw.println("--- " + name1);
			pw.println("+++ " + name2);
			ChunkVisitorPrint chunkVisitorPrinter = new ChunkVisitorPrint(pw);
			chunks.accept(chunkVisitorPrinter);
			pw.close();
		} catch (Exception e) {
			System.out.println("BinaryDiff error: " + e);
		}
	}

	/**
	 * Compares two binary files of equal size for differences. Output is somwhow similar to gnu unified diff: <br>
	 * --- old file <br>
	 * +++ new file <br>
	 * &#064; start pos, end pos <br>
	 * followed by line pairs preceded by - and +. The line preceded by - list the values which are replaced by the
	 * values in the + line.
	 * <p>
	 * One patch file can contain this whole block more than once, to patch different files in one go.
	 * 
	 * @param args
	 *            two binary files to compare, plus output file for the diff
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();

		try {
			int difflength = 1;
			int beforeLength = 0;
			int afterLength = 0;
			String outfile = "";
			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.isRequired().withLongOpt("length").withDescription(
					"how many bytes should be compared at once").hasArg().withArgName("length").create('l'));
			options.addOption(OptionBuilder.isRequired().withLongOpt("outfile").withDescription("output file").hasArg()
					.withArgName("file").create('o'));
			options.addOption(OptionBuilder.withLongOpt("version").withDescription("version").create('v'));
			options.addOption(OptionBuilder.withLongOpt("before").withDescription("prepend context number bytes")
					.hasArg().withArgName("bytes").create('b'));
			options.addOption(OptionBuilder.withLongOpt("after").withDescription("postpend context number bytes")
					.hasArg().withArgName("bytes").create('a'));

			CommandLine line = CLparser.parse(options, args);

			if (line.hasOption("l"))
				difflength = Integer.parseInt(line.getOptionValue("l"));
			if (line.hasOption("o"))
				outfile = line.getOptionValue("o");
			if (line.hasOption("b"))
				beforeLength = Integer.parseInt(line.getOptionValue("b"));
			if (line.hasOption("a"))
				afterLength = Integer.parseInt(line.getOptionValue("a"));
			if (line.hasOption("v")) {
				Info.printVersion("BinaryDiff");
				Runtime.getRuntime().exit(0);
			}
			String[] leftargs = line.getArgs();
			if (leftargs.length == 2)
				exec(leftargs[0], leftargs[1], outfile, difflength, beforeLength, afterLength);
			else {
				Info.printVersion("BinaryDiff");
				Runtime.getRuntime().exit(0);
			}
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("BinaryDiff"), options);
		}
	}

	/**
	 * @param diffLength
	 * @param beforeLength
	 * @param afterLength
	 * @param fileLength
	 * @param fileOneContent
	 * @param fileTwoContent
	 * @param chunks
	 * @param start
	 * @param stop
	 * @throws Exception
	 */
	private static void createChunk(int diffLength, int beforeLength, int afterLength, long fileLength,
			byte[] fileOneContent, byte[] fileTwoContent, VArrayList<Chunk> chunks, int start, int stop)
			throws Exception {
		VArrayList<Integer> diff1 = new VArrayList<Integer>();
		VArrayList<Integer> diff2 = new VArrayList<Integer>();
		int beforeLengthAdjusted = Math.min(start * diffLength, beforeLength);
		int afterLengthAdjusted = Math.min((int) fileLength - stop * diffLength, afterLength);
		for (int j = start * diffLength - beforeLengthAdjusted; j < stop * diffLength + afterLengthAdjusted; j++) {
			int byte1 = fileOneContent[j] & 0xFF;
			int byte2 = fileTwoContent[j] & 0xFF;
			diff1.add(byte1);
			diff2.add(byte2);
		}
		chunks.add(new Chunk(start * diffLength, stop * diffLength - 1, beforeLengthAdjusted, afterLengthAdjusted,
				diff1, diff2));
	}
}
