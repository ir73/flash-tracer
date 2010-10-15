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

    public static final String OS_LINUX = "linux";
    public static final String OS_MAC_OS_X = "mac os x";
    public static final String OS_WINDOWS = "windows";
    public static final String OS_WINDOWS_VISTA = "vista";

    public static final String OSName = System.getProperty("os.name").toLowerCase();
    public static final String newLine = System.getProperty("line.separator");
    public static final String userHome = System.getProperty("user.home");

    public static final String POLICY_LOG_FILE_NAME = "policyfiles.txt";

    public static final TextAreaHighlightPainter mouseOverObjectPainter = new TextAreaHighlightPainter(new Color(190, 230, 100));
    public static final TextAreaHighlightPainter highlightedSearchResultPainter = new TextAreaHighlightPainter(new Color(0, 150, 0));
    public static final TextAreaHighlightPainter searchResultPainter = new TextAreaHighlightPainter(new Color(150, 235, 150));
    public static final TextAreaHighlightPainter errorPainter = new TextAreaHighlightPainter(new Color(200, 200, 200));
    public static final TextAreaHighlightPainter warningPainter = new TextAreaHighlightPainter(new Color(230, 230, 230));

    public static final String VERSION = "2.3";
    public static final String VIZZY_PROPERTIES_FILENAME = "vizzy.properties";
    public static String vizzyRootDir;
    public static final String URL_PROJECT_HOME = "http://code.google.com/p/flash-tracer/";
    public static final String URL_PROJECT_DOWNLOAD = "http://code.google.com/p/flash-tracer/downloads/list";
    public static final String URL_DONATION = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=JPVRL8G9JYAVL&lc=EE&item_name=Vizzy%20Flash%20Tracer&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted";
    public static final String URL_MAILTO = "mailto:sergei.ledvanov@gmail.com";
    public static final String URL_VIZZY_PLUGIN = "http://code.google.com/p/flash-tracer/wiki/FlashDevelopPlugin";
}
