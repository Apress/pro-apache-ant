/*
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.mwrm.ant.api;

import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;

import org.apache.tools.ant.listener.Log4jListener;

import org.apache.catalina.ant.DeployTask;
import org.apache.catalina.ant.UndeployTask;

/**
 * <p>This class deploys or undeploys a web application from the Tomcat server,
 * using the Ant <code>&lt;deploy&gt;</code> and <code>&lt;undeploy&gt;</code>
 * tasks.</p>
 *
 * <p>The following is this class's usage information:</p>
 *
 * <table><tr><td><code>Deployer action [options]</code></td></tr>
 * <tr><td><code>Action:</code></td></tr>
 * <tr><td><code>-a, -action &lt;deploy filename.war [-path &lt;path&gt;] |
 * undeploy -path &lt;path&gt;&gt;</code></td></tr>
 * <tr><td><code>Options:</code></td></tr>
 * <tr><td><code>-url &lt;url&gt;</code></td></tr>
 * <tr><td><code>-u, -username &lt;username&gt;</code></td></tr>
 * <tr><td><code>-p, -password &lt;password&gt;</code></td></tr>
 * <tr><td><code>-l, -logfile &lt;logfile&gt;</code></td></tr>
 * <tr><td><code>-log4j</code></td></tr></table>
 *
 * <p>The <code>deploy</code> option uses the {@link DeployTask DeployTask}
 * and the <code>undeploy</code> option
 * uses the {@link UndeployTask UndeployTask}.
 * If the user specifies the <code>-log4j</code> option,
 * this class will log with the {@link Log4jListener Log4jListener}.</p>
 */
public final class Deployer {

    /**
     * <p>The default constructor.</p>
     */
    private Deployer() { }

    /** The default URL for the manager application. */
    private static String managerUrl = "localhost:8080/manager";

    /** The path. We'll build the default path below */
    private static String path = "";

    /** The username. */
    private static String username = "";

    /** The password. */
    private static String password = "";

    /** The filename of the WAR. */
    private static String filename;

    /** The user's desired action. */
    private static String action = "";

    /** Sets whether the default logger
     * will use the log file or <code>System.out</code>. */
    private static boolean useLogFile = false;

    /** The log file for the default logger. */
    private static String logFile;

    /** Sets whether we use the Log4j listener. */
    private static boolean useLog4j = false;

    /**
     * <p>Deploys or undeploys a web application,
     * depending on which command-line options
     * the user specifies.</p>
     * @param args The command-line arguments
     */
    public static void main(final String[] args) {

        // Process the arguments and set class members
        processArgs(args);

        // A final set of checks
        if (action.equals("undeploy") && path.equals("")) {
            usage("You must specify a path when undeploying.");
        } else if (action.equals("deploy") && filename == null) {
            usage("You must specify a file when deploying.");
        } else if (action.equals("")) {
            usage("You must specify an action with -a or -action.");
        }

        // Our tasks will need a project
        Project project = new Project();

        // Add the logger
        project.addBuildListener(getLogger());

        // Does the user want to use Log4j?
        if (useLog4j) {
            // The listener is configured with the log4j.properties file
            Log4jListener listener = new Log4jListener();
            project.addBuildListener(listener);
        }

        // The deployer that will deploy the WAR file
        DeployTask deployer = new DeployTask();
        // The undeployer that will undeploy the application
        UndeployTask undeployer = new UndeployTask();

        // Check what we want to do
        if (action.equals("deploy")) {
            // The task needs the project's logger
            deployer.setProject(project);

            // Call init() as good practice
            deployer.init();

            // The name of this task
            deployer.setTaskName("deployer");

            // The next few methods set the attributes of the task
            deployer.setUsername(username);
            deployer.setPassword(password);
            deployer.setUrl("http://" + managerUrl);
            deployer.setWar("file:" + filename);
            deployer.setPath(path);
            deployer.setUpdate(true);

            try {
                // Run the task
                deployer.execute();
            } catch (BuildException be) {
                // The three ways to log with a task
                //System.out.println(be.getMessage());
                //project.log(be.getMessage());
                if (!(be.getMessage().indexOf("FAIL") > -1)) {
                    deployer.log(be.getMessage());
                }
            }
        } else {
            // The task needs the project's logger
            undeployer.setProject(project);

            // Call init() as good practice
            undeployer.init();

            // The name of this task
            undeployer.setTaskName("undeployer");

            // The next few methods set the attributes of the task
            undeployer.setUsername(username);
            undeployer.setPassword(password);
            undeployer.setUrl("http://" + managerUrl);
            undeployer.setPath(path);

            try {
                // Run the task
                undeployer.execute();
                //System.out.println("Undeployed " + path + ".");
            } catch (BuildException be) {
                // The three ways to log with a task
                //System.out.println(be.getMessage());
                //project.log(be.getMessage());
                if (!(be.getMessage().indexOf("FAIL") > -1)) {
                    undeployer.log(be.getMessage());
                }
            }
        }
    } // end of main()

    /**
     * <p>Processes the command-line arguments
     * and sets member variables.</p>
     * @param args The command-line arguments
     */
    private static void processArgs(final String[] args) {
        try {
            // We'll go through the command-line arguments
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                // Check to see if the user has specified an action
                if (arg.equals("-a") || arg.equals("-action")) {
                    // If it's "undeploy", we'll remember that
                    if (args[i + 1].equals("undeploy")) {
                        action = "undeploy";
                        i++;
                        // If it's "deploy", we'll remember that
                    } else if (args[i + 1].equals("deploy")) {
                        action = "deploy";
                        i++;
                        // If it's not "undeploy" or "deploy", it's incorrect
                    } else {
                        usage();
                    }
                    // Check for the path
                } else if (arg.equals("-path")) {
                    path = args[i + 1];
                    i++;
                    // Check for the URL
                } else if (arg.equals("-url")) {
                    managerUrl = args[i + 1];
                    i++;
                    // Check for the username
                } else if (arg.equals("-u") || arg.equals("-username")) {
                    username = args[i + 1];
                    i++;
                    // Check for the password
                } else if (arg.equals("-p") || arg.equals("-password")) {
                    password = args[i + 1];
                    i++;
                    // Check if the user wants to use a log file
                } else if (arg.equals("-l") || arg.equals("-logfile")) {
                    logFile = args[i + 1];
                    useLogFile = true;
                    i++;
                    // Check if the user wants to use Log4j
                } else if (arg.equals("-log4j")) {
                    useLog4j = true;
                    // If the user has specified any other argument,
                    // it's incorrect
                } else if (arg.startsWith("-")) {
                    String msg = "Unknown argument: " + arg;
                    usage(msg);
                    // If there's no prefix it's our WAR file
                    // We only check for it if we're deploying
                } else if (action.equals("deploy")) {
                    // This must be our WAR file
                    filename = arg;

                    // Create a file object
                    File warFile = new File(filename);

                    // Check if this file actually exists
                    if (!warFile.exists()) {
                        String msg = "File " + arg + " does not exist.";
                        System.out.println(msg);
                        System.exit(-1);
                    }

                    // We should set the path if the user did not
                    // The path must begin with a '/'
                    if (path.equals("")) {
                        // If the WAR file is not in the current directory,
                        // there will be a slash
                        int begin = filename.lastIndexOf("/");
                        // We'll add a slash or not
                        // depending on where the WAR is
                        String slash = "";

                        // If there is no slash, the index will be -1
                        if (begin == -1) {
                            // Therefore, we need to take the whole file name
                            begin = 0;
                            // and add a slash to the path
                            slash = "/";
                        }
                        // Build the path by removing the .war extension
                        path = slash
                            + filename
                            .substring(begin, filename.lastIndexOf(".war"));
                    }
                }
            }
            // If a command-line options is not followed by another argument
            // the checks above will throw a ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            usage();
        }
    }

    /**
     * <p>Returns the default logger for the project.</p>
     * @return DefaultLogger
     */
    private static DefaultLogger getLogger() {
        // The logger for this class
        DefaultLogger logger = new DefaultLogger();

        // The default logger needs somewhere to write to
        PrintStream out = null;

        // Does the user want to write to a file?
        if (useLogFile) {
            try {
                // We'll log to the file the user specified
                out = new PrintStream(new FileOutputStream(logFile, true));
            } catch (FileNotFoundException fnfe) {
                // We can't use the log just yet
                System.out.println(fnfe.getMessage());
                // We'll fall back to System.out
                System.out.println("Using the console.");
                out = System.out;
            }
        } else {
            // The default is to print to System.out
            out = System.out;
        }

        // Set the output streams for the logger
        logger.setOutputPrintStream(out);
        logger.setErrorPrintStream(out);

        // Set the message threshold for this logger
        logger.setMessageOutputLevel(Project.MSG_INFO);

        return logger;
    }

    /** <p>Displays the usage information.</p>*/
    private static void usage() {
        System.out.println("Usage information:");
        System.out.println("Deployer action [options]");
        System.out.println("Action:");
        String actionMsg = "-a, -action <deploy filename.war [-path <path>] "
            + "| undeploy -path <path>>";
        System.out.println(actionMsg);
        System.out.println("Options:");
        System.out.println("-url <url>");
        System.out.println("-u, -username <username>");
        System.out.println("-p, -password <password>");
        System.out.println("-l, -logfile <logfile>");
        System.out.println("-log4j");

        System.exit(-1);
    }

    /** <p>Displays a custom message, then usage information.</p>
     * @param message The message to print
     */
    private static void usage(final String message) {
        System.out.println(message);
        usage();
    }
}
