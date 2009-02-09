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
package org.schreibubi.JCombinations.pdfGen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipInputStream;

import javax.swing.tree.TreePath;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jdesktop.swingx.event.ProgressEvent;
import org.jdesktop.swingx.event.ProgressListener;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinations.logic.DataModel;
import org.schreibubi.JCombinations.logic.io.GeneralDataFormat;
import org.schreibubi.JCombinations.logic.io.ImportDataInterface;
import org.schreibubi.JCombinations.ui.GridChartPanel;


/**
 * Class which generates pdf files from the xml-data files given on the command-line. A limit file can be given and also
 * which DUTs should be masked.
 * 
 * @author Jörg Werner
 * 
 */
public class PdfGen {

	/**
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options options = new Options();
		try {
			CommandLineParser CLparser = new PosixParser();

			// create the Options
			options.addOption(OptionBuilder.withLongOpt("limits").withDescription("[in] limit-file").hasArg()
					.withArgName("file").create('l'));
			options.addOption(OptionBuilder.withLongOpt("mask").withDescription("[in] mask DUTs").hasArg().withArgName(
					"").create('m'));

			CommandLine line = CLparser.parse(options, args);
			String[] leftargs = line.getArgs();

			for (String inFile : leftargs) {
				DataModel dm = new DataModel();
				String outFile = null;

				File file = new File(inFile);
				ImportDataInterface in = new GeneralDataFormat();

				FileInputStream fileInputStream;
				System.out.print("Reading " + inFile + "...");
				fileInputStream = new FileInputStream(file);
				if (file.getName().endsWith(".zip")) {
					ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
					zipInputStream.getNextEntry();
					in.readData(dm, zipInputStream);
					zipInputStream.close();
					outFile = inFile.substring(0, inFile.lastIndexOf(".xml.zip")) + ".pdf";
				} else {
					in.readData(dm, fileInputStream);
					outFile = inFile.substring(0, inFile.lastIndexOf(".xml")) + ".pdf";
				}
				fileInputStream.close();
				System.out.println("done.");

				if (line.hasOption("m")) {
					String maskString = line.getOptionValue("m");
					String[] maskArray = maskString.split(",");
					ArrayList<String> dutMask = new ArrayList<String>();
					for (String s : maskArray)
						dutMask.add(s);
					dm.setSeriesMask(dutMask);
				}

				if (line.hasOption("l")) {
					System.out.print("Applying limits...");
					String limitsText = "";
					String limitsFile = line.getOptionValue("l");
					BufferedReader inReader = new BufferedReader(new FileReader(limitsFile));
					String s;
					while ((s = inReader.readLine()) != null)
						limitsText = limitsText + s + "\n";
					inReader.close();
					dm.setLimitsText(limitsText);
					dm.applyLimits();
					System.out.println("done");
				}

				System.out.print("Writing " + outFile + "...");
				ProgressListener pl = new ProgressListener() {

					public void progressEnded(ProgressEvent evt) {
						System.out.println("done.");
					}

					public void progressIncremented(ProgressEvent evt) {
						System.out.print("[ " + evt.getProgress() + " ]");
					}

					public void progressStarted(ProgressEvent evt) {
					}
				};
				ArrayList<TreePath> selection = new ArrayList<TreePath>();
				selection.add(dm.getRoot().getTreePath());

				GridChartPanel.generatePDF(new File(outFile), dm, selection, pl);
				System.out.println("done.");

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Info.getVersionString("pdfGen"), options);
		}

	}

}
