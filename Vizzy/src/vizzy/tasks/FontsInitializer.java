/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergeil
 */
public class FontsInitializer {
    private Font[] fonts;
    private String[] fontNames;

    public void loadSystemFonts() {
        try {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            fonts = env.getAllFonts();
            fontNames = new String[fonts.length];
            for (int i = 0; i < fonts.length; i++) {
                Font font = fonts[i];
                fontNames[i] = font.getName();
            }
        } catch (Exception ex) {
            Logger.getLogger(FontsInitializer.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public Font[] getFonts() {
        return fonts;
    }

    public String[] getFontNames() {
        return fontNames;
    }

}
