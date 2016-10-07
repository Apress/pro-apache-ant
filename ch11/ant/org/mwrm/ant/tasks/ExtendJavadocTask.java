/*
 *  Extends org.apache.tools.ant.taskdefs.Javadoc
 *  and uses org.apache.tools.ant.taskdefs.UpToDate,
 *  which are Copyright 2000-2005 The Apache Software Foundation.
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

package org.mwrm.ant.tasks;

import java.io.File;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.taskdefs.Javadoc;
import org.apache.tools.ant.taskdefs.UpToDate;

import org.apache.tools.ant.types.FileSet;

/**
 * <p>The <code>ExtendJavadocTask</code> class
 * extends the {@link org.apache.tools.ant.taskdefs.Javadoc Javadoc} task.
 * It checks whether a set of source files are newer than a set of target files
 * and if so, it generates Javadocs.</p>
 *
 */

public class ExtendJavadocTask extends Javadoc {

    /** The attribute of the task element. */
    private File target;

    /** A set of file sets,
     * each of which is provided by a nested file set. */
    private Vector targetFileSets  = new Vector();

    /** The internal uptodate task. */
    private UpToDate utd;

    /**
     * <p>Creates a new instance of an internal
     * <code>&lt;uptodate&gt;</code> task
     * and adds it to the current project.</p>
     * @see UpToDate org.apache.tools.ant.taskdefs.UpToDate
     */
    public final void init() {
        // We need an instance of the <uptodate> task
        utd = new UpToDate();
        // We need to add the task to this project
        utd.setProject(this.getProject());
    }

    /**
     * <p>Checks if Javadocs should be created
     * and then calls <code>super.execute()</code> if so.</p>
     * <p>This method does usage checks on the task's attributes
     * and its nested elements.
     * It will throw a <code>BuildException</code> if there is a violation.</p>
     */
    public final void execute() {
        // This is the usage information

        // We can't have a target attribute
        // and nested targetfiles elements
        if (target != null && targetFileSets.size() > 0) {
            String msg = "You can't specify a targetfile attribute "
                + "and <targetfiles> elements.";
            throw new BuildException(msg);
        }
        // We have to specify either a target attribute
        // or at least one nested targetfiles elements
        if (target == null && targetFileSets.size() == 0) {
            String msg = "You must specify either a targetfile attribute "
                + "or at least one <targetfiles> element.";
            throw new BuildException(msg);
        }

        // If this is false, the files aren't up to date
        // and we have to run the javadocs
        boolean eval = false;

        // If we've got to this point, we know the usage must be correct.
        // Let's check whether a single target attribute has been used.

        if (target != null) {
            // Get the results of the check
            eval = getResult(target);
        } else {
            // If a target attribute wasn't specified,
            // at least one nested targetfiles element was.

            // We first get all the file sets represented by the nested elements
            Enumeration e = targetFileSets.elements();

            // And then iterate over them
            while (e.hasMoreElements()) {

                // The next element is a file set, so we get its files
                // in a semi-colon-separated list
                String files = e.nextElement().toString();

                // Next, we split the list into its file names
                StringTokenizer st = new StringTokenizer(files, ";");

                // And iterate over them to test each one
                while (st.hasMoreTokens()) {
                    // We create a file
                    //from the current file name in the iteration
                    File tempTarget = new File(st.nextToken());

                    // Get the results of the check
                    eval = getResult(tempTarget);

                    // One false result is enough to fail the whole file set
                    if (!eval) {
                        break;
                    }
                }
                // One false result is enough to fail the whole file set
                if (!eval) {
                    break;
                }
            }
        }

        // If the test failed, we want to generate Javadocs
        if (!eval) {
            super.execute();
        } else {
            log("Skipping Javadoc creation. The files are up to date.",
                Project.MSG_INFO);
        }
    }

    /** <p>Checks whether the files are up to date.</p>
     * @param file The file to evaluate
     * @return boolean The result
     */
    private boolean getResult(final File file) {
        // Set the target property in the <uptodate> task
        utd.setTargetFile(file);
        // Evaluate the files
        return utd.eval();
    }

    /**
     * <p>The setter method for the <code>target</code> attribute.</p>
     * @param targetFile A file to check against
     */
    public final void setTarget(final File targetFile) {
        this.target = targetFile;
    }

    /**
     * <p>The setter method for the file set
     * contained in the nested <code>&lt;srcfiles&gt;</code> element.</p>
     * @param fileset A file set of source files
     */
    public final void addSrcfiles(final FileSet fileset) {
        utd.addSrcfiles(fileset);
    }

    /**
     * <p>The setter method for the file sets
     * contained in nested <code>&lt;targetfiles&gt;</code> elements.</p>
     * @param fileset A file set of target files
     */
    public final void addTargetfiles(final FileSet fileset) {
        targetFileSets.add(fileset);
    }
}

