/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.model;

import java.awt.Color;
import vizzy.util.TextAreaHighlightPainter;

/**
 *
 * @author sergei
 */
public class Conf {

    public static final TextAreaHighlightPainter mouseOverObjectPainter = new TextAreaHighlightPainter(new Color(190, 230, 100));

    public static final TextAreaHighlightPainter highlightedSearchResultPainter = new TextAreaHighlightPainter(new Color(0, 150, 0));
    public static final TextAreaHighlightPainter searchResultPainter = new TextAreaHighlightPainter(new Color(150, 235, 150));

    public static final TextAreaHighlightPainter errorPainter = new TextAreaHighlightPainter(new Color(200, 200, 200));
    public static final TextAreaHighlightPainter warningPainter = new TextAreaHighlightPainter(new Color(230, 230, 230));
}
