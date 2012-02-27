/**
 * 
 */
package org.schreibubi.JCombinationsTools.mergeResults;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.Unit;
import org.schreibubi.visitor.VLinkedHashMap;

import com.tragicphantom.stdf.Record;
import com.tragicphantom.stdf.RecordVisitor;

/**
 * @author jwerner6
 * 
 */
public class VerigyRecordVisitor implements RecordVisitor {

	int touchdown=-1;
	
	VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> results = new VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>>();

	public VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> getResults() {
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tragicphantom.stdf.RecordVisitor#handleRecord(com.tragicphantom.stdf
	 * .Record)
	 */
	@Override
	public void handleRecord(Record record) {
		//System.out.println(record.getType());
		if (record.getType() == "Pir") {
			touchdown++;
		}
		if (record.getType() == "Ptr" && record.hasField("HEAD_NUM") && record.hasField("SITE_NUM")
				&& record.hasField("TEST_NUM") && record.hasField("RESULT")) {
			Long headNum = (Long) record.getField("HEAD_NUM");
			Long siteNum = (Long) record.getField("SITE_NUM");
			Long testNum = (Long) record.getField("TEST_NUM");
			Float testValue = (Float) record.getField("RESULT");
			if (headNum == 1) {
				NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
				DecimalFormat myFormatter = (DecimalFormat) nf;
				myFormatter.applyPattern("00");

				VLinkedHashMap<VLinkedHashMap<Symbol>> t = results.get("" + testNum);
				if (t == null) {
					t = new VLinkedHashMap<VLinkedHashMap<Symbol>>();
				}
				VLinkedHashMap<Symbol> d = t.get("DUT" + myFormatter.format(siteNum+touchdown));
				if (d == null) {
					d = new VLinkedHashMap<Symbol>();
				}
				d.put("S0", new SymbolDouble("DUT" + myFormatter.format(siteNum+touchdown), 20*Math.log10(testValue)-82.4, new Unit("dB")));
				t.put("DUT" + myFormatter.format(siteNum+touchdown), d);
				results.put("" + testNum, t);
			}
		}
	}
}
