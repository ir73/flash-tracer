/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.comp;

import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 *
 * @author sergeil
 */
public class BGComboBoxEditor extends BasicComboBoxEditor {

    @Override
    protected JTextField createEditorComponent() {
        JTextField editor = new BGComboBoxEditorComponent("",9);
        editor.setBorder(null);
        return editor;
    }

}
