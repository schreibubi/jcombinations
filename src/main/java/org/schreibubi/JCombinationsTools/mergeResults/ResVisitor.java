package org.schreibubi.JCombinationsTools.mergeResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.visitor.VLinkedHashMap;

public class ResVisitor {
	static Pattern p = Pattern.compile(";");

	VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> results = new VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>>();

	public VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> getResults() {
		return results;
	}

	public void parse(String file) {
		try {
			File dclogFile = new File(file);
			BufferedReader br = new BufferedReader(new FileReader(dclogFile));
			String line;
			while ((line = br.readLine()) != null) {
				String[] tC = line.split(",");
				NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
				DecimalFormat myFormatter = (DecimalFormat) nf;
				myFormatter.applyPattern("00");

				VLinkedHashMap<VLinkedHashMap<Symbol>> t = results.get("" + tC[0]);
				if (t == null) {
					t = new VLinkedHashMap<VLinkedHashMap<Symbol>>();
				}
				VLinkedHashMap<Symbol> d = t.get("DUT" + myFormatter.format(1));
				if (d == null) {
					d = new VLinkedHashMap<Symbol>();
				}
				String[] values = p.split(tC[1], 0);
				for (int i = 0; i < values.length; i++) {
					if (values[i].contains("NaN"))
						values[i]=values[i].replace("NaN", "0.0"); // replace NaN with 0.0 but keep Unit!
					d.put("S" + i, new SymbolDouble("DUT" + myFormatter.format(1), values[i]));
				}
				t.put("DUT" + myFormatter.format(1), d);
				results.put(tC[0], t);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
