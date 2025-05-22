package com.gerefi.ui;

import com.gerefi.core.ui.AutoupdateUtil;

import javax.swing.*;
import java.awt.*;

public abstract class InitOnFirstPaintPanel {
    private final JPanel content = new JPanel(new BorderLayout()) {
        boolean isFirstPaint = true;

        @Override
        public void paint(Graphics g) {
            if (isFirstPaint) {
                content.removeAll();
                content.add(createContent(), BorderLayout.CENTER);
                AutoupdateUtil.trueLayout(content);
                isFirstPaint = false;
            }
            super.paint(g);
        }
    };

    protected abstract JPanel createContent();

    public InitOnFirstPaintPanel() {
        content.add(new JLabel("Initializing..."), BorderLayout.CENTER);
    }

    public JComponent getContent() {
        return content;
    }
}
