## Troubleshooting ##
If you do not see anything in your Vizzy debug screen, please check the following:
<br><br>
<b>Vizzy and Program Files folder</b>
<br>
Windows users should not put Vizzy to <code>Program Files</code> folder because Windows does not allow applications in that folder to modify files. Put Vizzy to some other locations like your Desktop.<br>
<br><br>
<b>Debug Flash Player</b>
<br>
Check that you have debug flash player installed instead of a regular one. To check, just right click on your any flash object and if you see Debugger option there, then you have debug flash player installed.<br>
Install debug Flash Players from <a href='http://www.adobe.com/support/flashplayer/downloads.html'>http://www.adobe.com/support/flashplayer/downloads.html</a>
By default, Vizzy should detect flash player automatically at first launch.<br>
<br><br>
<b>mm.cfg</b>
<br>
Check that you have mm.cfg file in your user's directory (Macintosh OS X: /Library/Application Support/Macromedia, Windows 2000/XP: c:\Documents and Settings\username\, Windows Vista: C:\Users\username, Linux: /home/username)<br>
If it does not exist, please create mm.cfg in that directly with 2 lines of content:<br>
<pre><code>TraceOutputFileEnable=1<br>
ErrorReportingEnable=1<br>
</code></pre>
<br><br>
<b>flashlog.txt</b>
<br>
Check that Vizzy has correctly detected path to your flashlog.txt file. Open Vizzy -> Extra -> Options -> Log File. In "flashlog.txt" input field you should see a path to flashlog.txt. If it's not correct, please fix it and click OK.<br>
<br><br>
<b>Locking a file</b>
<br>
If you have Flash CS5+ installed you should check your processes for running Adobe CS5 Service Manager (you should see it in Windows Task Manager). You should terminate that process because it locks flashlog.txt file for some reason and does not allow to clear it from external applications.<br>
<br><br>
<b>Chrome issues</b>
<br>
Chrome has a built in Flash Player plugin, so you have to disable it first and install debug plugin instead.<br>
<br>
Go to Preferences - Under the Hood - Content Settings (button) - Plugins - Disable individual plug-ins (text). Disable built in plugin. Be careful: if you just disable the flash, it is going to disable all flash plugins because both the built-in and the external one are nested under one entry called "Flash." On the top right hand corner of the plugins page, there is a "[+] Details" link. Upon clicking that, it expands the "flash" entry and you can see two plugins. Disable only the built-in one. (thanks Shashi for the tip).<br>
<br>
<br><br>
<b>Firefox issues</b>
<br>
Flash runs in a separate process in Firefox, called plugincontainer.exe. This enables keeping browser running if your plugin content hangs or starts working slow. However, this procecss might lock flashlog.txt file and block Vizzy from deleting it. If you notice, that you cannot clear flash log in Vizzy, then try killing plugincontainer.exe from task manager first.<br>
<br>
<br>
<br>

Scripting extension in Photoshop is also blocking flash log file. It's located here: C:\Program Files\Adobe\Adobe Photoshop CS5 (64 Bit)\Plug-ins\Extensions. You should delete these files or just backup them to other folder:<br>
<ul><li>ScriptingSupport.8li<br>
</li><li>ScriptUIFlexPhotoshop.swf<br>
</li><li>ScriptUIFlexServer.swf<br>
</li><li>ScriptUIFlexServer-app.xml</li></ul>




<br><br><br><br>



<h2>System requierments</h2>
<ul><li>Java Runtime 1.6+