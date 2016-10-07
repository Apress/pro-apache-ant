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

import org.apache.tools.ant.Project;
import org.apache.tools.ant.DefaultLogger;

import org.apache.tools.ant.taskdefs.Copy;

import org.apache.tools.ant.types.FileSet;

/**
 * <p>Uses a {@link FileSet FileSet} to implement a batch copy.</p>
 */
public final class Copyer {

    /**
     * The default constructor.
     */
    private Copyer() { }

    /**
     * <p>Copies all files that match the following patterns:
     * <code>*.xml</code> and <code>*.xsl</code>.</p>
     *
     * @param args Command-line arguments,
     * though they have no effect in this class
     * @see FileSet
     */
    public static void main(final String[] args) {
        // Our tasks will need a project
        Project project = new Project();

        // Add the logger
        project.addBuildListener(getLogger());

        // Instantiate the copy task
        Copy copyTask = new Copy();

        // Build the file set
        FileSet fileset = new FileSet();
        fileset.setIncludes("*.xml");
        fileset.setIncludes("*.xsl");
        fileset.setDir(new File("."));

        // Add the file set to the copy task
        copyTask.addFileset(fileset);
        // Set the destination for the files
        copyTask.setTodir(new File("copydir"));

        // The name of this task
        copyTask.setTaskName("copyer");

        // Add the copy task to the project
        copyTask.setProject(project);

        // Call init() as good practice
        copyTask.init();

        // Copy the files
        copyTask.execute();
    }

    /**
     * <p>Returns the default logger for the project.</p>
     * @return DefaultLogger
     */
    private static DefaultLogger getLogger() {
        // The logger for this class
        DefaultLogger logger = new DefaultLogger();

        // The default logger needs somewhere to write to
        PrintStream out = System.out;

        // Set the output streams for the logger
        logger.setOutputPrintStream(out);
        logger.setErrorPrintStream(out);

        // Set the message threshold for this logger
        logger.setMessageOutputLevel(Project.MSG_INFO);

        return logger;
    }
}
