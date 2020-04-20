# PowerViewer
An open source software to handle large text files efficiently

As the data growing rapidly the tools and softwares should also be compatible to handle such large amount of data. There are softwares which writes GB's of log files. The traditional text viewer softwares are inefficient to handle such large amount of data. The PowerViewer has the capability to handle large text files.Theorytically it can open files having tera bytes of size however the testing is done with 20GB (Yet the performance and capability are in the phase of testing).

### Features 
1. Version 1.0 gives the facility to open large files and view them page by page.
2. It has the capability to save the part of file currently viewed. 

### How to use 
Download the powerviewer.jar file under v1.0_Executable directory and just double click on it to open it. Use Right and Left Arrow Keys to scroll File pages. 
Make sure java 1.8 or higher version is installed on the system and JAVA_HOME is set properly. If double clicking does not work then open the terminal and run "java -jar powerviewer.jar" to run it.

### Future Work 
Soon Version 1.1 will be introduced with the below features 
1. Searching a text
2. Buffer Configuration as per the need
3. Goto some specific page 


### Architecture 
The core engine maintains a siding buffer technique to handle large files along with multithreading and thread synchronization. Apart from that a bulk data handling APIs are used to reduce the response time and efficient handling of data. Complete details of core engine architecture and working along with build instruction will be updated with Version 1.1

### Licensing 
Project is under GNU v3 License


