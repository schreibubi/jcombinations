/**
 *  Copyright (c) 2006 Qimonda AG                                                       Company Confidential
 *  QAG PD PT TPE C
 *  --------------------------------------------------------------------------------------------------------
 *  $Rev: 99 $
 *  $Date: 2007-05-08 14:28:37 +0200 (Di, 08 Mai 2007) $
 *  $Author: wernerj $
 *  $URL: svn+ssh://scmis001.muc.infineon.com/home/ppe/ppebe/SVNRepository/FlexibleBitstreamDecoder/trunk/src/com/qimonda/pd/pt/tpec/FlexibleBitstreamDecoder/ASTFrame.java $
 *  --------------------------------------------------------------------------------------------------------
 *  Signature for 'what': @(#) $$Id: ASTFrame.java 99 2007-05-08 12:28:37Z wernerj $$
 *  --------------------------------------------------------------------------------------------------------
 *  Engineers in charge: Jörg Werner +49 89 600 88 2231
 */
package org.schreibubi.JCombinationsTools.evalGenCombinations;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.antlr.runtime.tree.CommonTree;

/**
 * @author Jörg Werner
 *
 */
public class ASTFrame extends JFrame {

	private static final long	serialVersionUID	= -92059391014955313L;

	public ASTFrame(String lab, CommonTree t) {
		super(lab);

		JTree tree = new JTree(new ASTtoTreeModelAdapter(t));

		JScrollPane scrollPane = new JScrollPane(tree);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane);

		Container content = getContentPane();
		content.add(panel, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Frame f = (Frame) e.getSource();
				f.setVisible(false);
				f.dispose();
				// System.exit(0);
			}
		});
	}

}
