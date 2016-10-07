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
package org.mwrm.ant.tasks;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * <p>The <code>ClassSetTask</code> class
 * demonstrates how to use a <code>Class</code> argument
 * in a custom class attribute.</p>
 *
 */

public class ClassSetTask extends Task {

    /** The qualified class name. */
    private Class qualifiedName;

    /**
     * <p>Runs the task and displays the qualified name of the class
     * that is set as the <code>setQualifiedName</code> attribute.</p>
     *
     */
    public final void execute() {
        log("qualifiedName: " + qualifiedName, Project.MSG_INFO);
    }

    /**
     * <p>Sets the fully qualified name of the class.</p>
     ** @param qName The fully qualified name of a class
     */
    public final void setQualifiedName(final Class qName) {
        if (qName.getName().equals("java.lang.Integer")
            ||
            qName.getName().equals("java.lang.String")) {
            log(qName.getName() + " found.", Project.MSG_INFO);
        } else {
            String msg = "You can only specify java.lang.Integer "
                + "or java.lang.String in qualifiedName.";
            throw new BuildException(msg);
        }
        this.qualifiedName = qName;
    }
}

