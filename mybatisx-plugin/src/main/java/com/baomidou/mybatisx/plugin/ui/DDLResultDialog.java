package com.baomidou.mybatisx.plugin.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DDLResultDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCopy;
    private JButton buttonCancel;
    private JTextArea textAreaResult;

    public DDLResultDialog(String result) {
        setTitle("TONY CREATE TABLE DDL GENERATOR");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCopy);

        textAreaResult.setText(result);


        buttonCopy.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(this.textAreaResult.getText());
        clipboard.setContents(selection, null);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
