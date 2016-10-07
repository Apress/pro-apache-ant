/*
 *  Uses org.apache.tools.ant.Main,
 *  which is Copyright 2000-2005 The Apache Software Foundation.
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

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Main;

/**
 * <p>The <code>ProjectHelpTask</code> class displays usage information
 * for the current project. This is the same information as is displayed
 * by <code>-projecthelp</code>.</p>
 *
 */

public class ProjectHelpTask extends Task {

    /** The location of the build file to use
     * when obtaining usage information. */
    private String buildfile;

    /**
     * <p>Runs the task.
     * It calls {@link org.apache.tools.ant.Main#main(String[] args)
     * org.apache.tools.ant.Main.main()} with the <code>-projecthelp</code>
     * parameter. It will also send the current build file's file name
     * via the <code>-f</code> parameter.</p>
     *
     * <p>The <code>buildfile</code> attribute is optional.
     * The default is the task's build file.</p>
     * @see org.apache.tools.ant.Main org.apache.tools.ant.Main
     */
    public final void execute() {
        // If the buildfile attribute is null, we'll use the task's build file
        if (buildfile == null) {
            buildfile = getLocation().getFileName();
        }

        // The arguments that we will pass to the Main class.
        // The buildfile attribute must follow the -f parameter.
        String[] args = {"-projecthelp", "-f", buildfile};

        // Call the Main Ant class with the arguments.
        Main.main(args);
    }

    /**
     * Setter method for the <code>buildfile</code> attribute.
     * @param file The file name of the build file to use.
     *
     */
    public final void setBuildfile(final String file) {
        this.buildfile = file;
    }

}

