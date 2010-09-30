/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vizzy.model;

/**
 *
 * @author sergeil
 */
public class SettingsModel {
    public static final String OS_LINUX = "linux";
    public static final String OS_MAC_OS_X = "mac os x";
    public static final String OS_WINDOWS = "windows";
    public static final String OS_WINDOWS_VISTA = "vista";

    public static String OSName = System.getProperty("os.name").toLowerCase();
    public static String newLine = System.getProperty("line.separator");
    public static String userHome = System.getProperty("user.home");
}
