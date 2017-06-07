/**
 * MIT License
 *
 * Copyright (c) 2017 Demitrius Belai
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package br.unesp.fc.countdown.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Timer extends JLabel {

    private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 72);

    public Timer() {
        try {
            InputStream is = this.getClass().getResourceAsStream("/NotoMono-Regular.ttf");
            Font f = Font.createFont(Font.TRUETYPE_FONT, is);
            font = f.deriveFont(72f);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setFont(font);
        setText("00:00");
        FontMetrics fontMetrics = getFontMetrics(font);
        float height = fontMetrics.getHeight() * 1.1f;
        float width = fontMetrics.stringWidth("000:000") * 1.1f;
        setMinimumSize(new Dimension((int)width, (int)height));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calcularFontSize();
            }
        });
    }

    private void calcularFontSize() {
        FontMetrics fontMetrics = getFontMetrics(font);
        float height = fontMetrics.getHeight() * 1.1f;
        float width = fontMetrics.stringWidth("000:000") * 1.1f;
        Font newFont = getFont().deriveFont(font.getSize() * Math.min(getWidth() / width, getHeight() / height));
        setFont(newFont);
    }

    private volatile int time = 0;

    public void setTime(int time) {
        if (this.time == time)
            return;
        this.time = time;
        //this.getContentPane().repaint();
        int minutos = Math.abs(this.time) / 60;
        int segundos = Math.abs(this.time) % 60;
        if (time < 0)
            setText(String.format("-%02d:%02d", minutos, segundos));
        else
            setText(String.format("%02d:%02d", minutos, segundos));
    }

}
