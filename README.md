# CukeDoc
Doclet for Generating Cucumber API Documentatrion

CukeDoc is a Doclet that can be used to describe a test harness is a easy to read way.

## Documentation

Since CukeDoc is a Doclet it is launched through JavaDoc

<pre>
  javadoc -cp <application-classpath> -doclet cucumber.doc.Main $* -docletpath <location-of-cukedoc> -sourcepath <source> -subpackages <packages> <cukeDoc-options>
</pre>

Where the <cukeDoc-options> are any of the following:


<pre>
  -i18n <locale>       Language of generated documentation
  -link <path>         Path to an XML report from another project. The details will be added to this project
  -windowtitle <text>  Browser window title for the documentation
  -footer <html-code>  Include footer text for each page
  -top <html-code>     Include top text for each page
  -bottom <html-code>  Include bottom text for each page
  -d <directory>       Destination directory for output files
  -format <formats>    Comma separated list containing one or more of 'BASIC', 'XML' or 'HTML'
  -icon <path>         Browser window favicon for the documentation
  -notes <path>        Optional set of notes that will be included in the report
  -description <path>  Add a description to the Overview page
  -trace               Generate additional output while creating document
  -help, -h            Display this page and exit
</pre>



## Libraries used

Other than Java 8+, CukeDoc has just one external dependency. The HTML generator uses the excellent j2html (https://j2html.com/)
