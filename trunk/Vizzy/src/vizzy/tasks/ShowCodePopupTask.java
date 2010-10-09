/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.tasks;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import org.apache.commons.io.FileUtils;
import vizzy.forms.panels.CodeForm;
import vizzy.model.SourceAndLine;

/**
 *
 * @author sergeil
 */
public class ShowCodePopupTask {

    private JTextArea owner;
    private CodeForm codeForm;
    private Popup popup;
    private Point codeFormlocationOnScreen;
    private SourceAndLine source;
    private Object lock = new Object();

    public ShowCodePopupTask() {
        
    }

    public void hide() {
        synchronized (lock) {
            source = null;
            codeFormlocationOnScreen = null;
            if (codeForm != null) {
                codeForm = null;
            }

            if (popup != null) {
                popup.hide();
                popup = null;
            }
        }
    }

    public boolean isVisible() {
        return popup != null;
    }

    public boolean isMouseAtCodePopup(Point pt) {
        if (codeForm == null) {
            return false;
        }
        if ((pt.getX() > codeFormlocationOnScreen.getX() + codeForm.getWidth())
                || (pt.getX() < codeFormlocationOnScreen.getX())
                || (pt.getY() > codeFormlocationOnScreen.getY() + codeForm.getHeight())
                || (pt.getY() < codeFormlocationOnScreen.getY())) {
            return false;
        }
        return true;
    }

    public void show(Point pt, SourceAndLine source) {
        synchronized (lock) {
            this.source = source;
            File file = new File(source.filePath);
            if (!file.exists()) {
                return;
            }
            try {

                List<String> lines = FileUtils.readLines(file);
                if (lines.size() < source.lineNum) {
                    return;
                }

                int fromLine = 0;
                int toLine = lines.size() - 1;

                lines = lines.subList(fromLine, toLine);

                codeForm = new CodeForm();
                synchronized (codeForm) {
                    codeForm.initStyles(owner.getFont());
                    codeForm.setText(lines, source.lineNum - 1);
                    codeForm.updateSize();

                    Point codeFormLocation = new Point();
                    Dimension codeFormPreferredSize = codeForm.getPreferredSize();

                    Point textAreaScreenLocation = owner.getLocationOnScreen();
                    Dimension textAreaPreferredSize = owner.getPreferredSize();
                    codeFormLocation.x = (int) (textAreaScreenLocation.x + pt.getX());
                    codeFormLocation.y = (int) (textAreaScreenLocation.y + pt.getY());

                    if (codeFormLocation.x + codeFormPreferredSize.width >= textAreaScreenLocation.x + textAreaPreferredSize.width) {
                        codeFormLocation.x -= codeFormPreferredSize.width;
                    }

                    if (codeFormLocation.y + codeFormPreferredSize.height >= textAreaScreenLocation.y + textAreaPreferredSize.height) {
                        codeFormLocation.y -= codeFormPreferredSize.height;
                        codeFormLocation.y -= 4;
                    } else {
                        codeFormLocation.y += 4;
                    }

                    PopupFactory pf = PopupFactory.getSharedInstance();
                    popup = pf.getPopup(owner, codeForm, codeFormLocation.x, codeFormLocation.y);
                    popup.show();

                    codeForm.revalidate();
                    codeForm.repaint();

                    codeFormlocationOnScreen = codeForm.getLocationOnScreen();

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            try {
                                codeForm.scrollText();
                                codeForm.setFocus();
                            } catch (Exception e) {
                            }
                        }
                    });
                }


            } catch (Exception ex) {
                Logger.getLogger(ShowCodePopupTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public JTextArea getOwner() {
        return owner;
    }

    public void setOwner(JTextArea owner) {
        this.owner = owner;
    }

    public SourceAndLine getSource() {
        return source;
    }

    public void setSource(SourceAndLine source) {
        this.source = source;
    }

}
