

## Overview ##
Available since 2.3<br><br>
Adding SWC to your project allows to open corresponding source file on double click in Vizzy. Also, right clicking the mouse on the trace line will cause a popup open with trace line highlighted. Tracer knows from where every line was dispatched and can therefore can open proper file and even highlight a line with <code>trace</code>.<br>
<br>
<h2>Installation</h2>
<ul><li>Download VizzyTrace.swc from Downloads page. Copy it to your project's <code>src</code> or <code>lib</code> directory. Add swc to library path of fla file project (in case of Flash IDE) or link as swc in case of Flash Builder project.<br>
</li><li>Open Vizzy, go to <code>Extra</code> -> <code>Options</code>, open <code>Trace Parser</code> tab, tick <code>Connect VizzyTrace library</code>.<br>
</li><li>If using FlashDevelop, see <a href='http://code.google.com/p/flash-tracer/wiki/FlashDevelopPlugin'>VizzyPlugin</a> installation instructions.<br>
</li><li>Make sure <code>Permit Debugging</code> is checked in your fla file project.</li></ul>

<h2>Code Changes</h2>
The only code change is required is replacing native <code>trace</code> with <code>trc</code>. No imports required or something else. Package function <code>trc</code> has the same syntax as native <code>trace</code> so passing comma separated parameters is supported.<br>
<pre><code>trc("hello world!");<br>
</code></pre>
You can turn to a native trace anytime with the following piece of code:<br>
<pre><code>VizzyTrace.mode = VizzyTrace.SIMPLE;<br>
</code></pre>
In the background, <code>trc</code> adds debug information to trace statement about file path and line number from where <code>trace</code> is dispatched. This allows to double click on any line in Vizzy Flash Tracer which will cause that file to open in your preferred Actionscript editor and even highlighting line with <code>trace</code> (see <a href='http://code.google.com/p/flash-tracer/wiki/FlashDevelopPlugin'>VizzyPlugin</a>).<br>
<br>
<h2>Screenshots</h2>
<ul><li>Right click on a trace line and popup appears<br>
<img src='http://dl.dropbox.com/u/9443658/vizzy/screens/trace_code_popup.jpg' />
</li><li>Once double-clicked, corresponding source file is opened and trace line highlighted<br>
<img src='http://dl.dropbox.com/u/9443658/vizzy/screens/open_trace.jpg' />