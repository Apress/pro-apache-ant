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

import java.util.Hashtable;
import java.util.Enumeration;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * <p>At each stage in a task's life cycle, this class displays information
 * to show the internal state of the task and its position with in the project.
 * It takes a <code>name</code> attribute.</p>
 */
public class LifeCycleTask extends Task {

    /** The <code>name</code> attribute of this task. */
    private String name;

    /** The body text of this task. */
    private String text;

    /**
     * <p>The constructor displays the state of the task
     * as it is instantiated.</p>
     */
    public LifeCycleTask() {
        System.out.println("---------------");
        System.out.println("Constructor called");
        System.out.println("Value of name attribute: " + name);
        System.out.println("Value of the body text: " + text);
        System.out.println("Project: " + getProject());
        System.out.println("Location: " + getLocation());
        System.out.println("Target: " + getOwningTarget());
        System.out.println("---------------");
    }

    /**
     * <p>Displays the state of the task at initialization.</p>
     * @see #logAll(String method)
     */
    public final void init() {
        logAll("init()");
    }

    /**
     * <p>Displays the state of the task when Ant runs it.
     * This method also runs some usage checks
     * to ensure the task is being used properly.</p>
     */
    public final void execute() {
        if (name == null) {
            throw new BuildException("You must specify a name attribute in "
                                     + getTaskName() + ".");
        }
        logAll("execute()");

        // Write the name to output
        log(name, Project.MSG_INFO);
    }

    /**
     * <p>Sets the name to display
     * and shows the state of the task afterwards.</p>
     * @param aName The name to display
     */
    public final void setName(final String aName) {
        // The value of the name attribute
        this.name = aName;
        logAll("setName()");
    }

    /**
     * <p>Sets the body text of the task
     * and shows the state of the task afterwards.</p>
     * @param bodyText The body text
     */
    public final void addText(final String bodyText) {
        // If the body text is just whitespace, it might as well be null
        if (bodyText.trim().equals("")) {
            this.text = null;
        } else {
            this.text = bodyText.trim();
        }
        logAll("addText()");
    }

    /** <p>Checks for task references.</p>
     * @return String
     * A string that tells us details of the reference check
     */
    private String referenceCheck() {

        // The default setting
        String setString = "Reference not found.";

        // We need the references that have been set in this project
        Hashtable refs = getProject().getReferences();
        Enumeration e = refs.elements();

        // Let's iterate over them
        while (e.hasMoreElements()) {
            // We want to work with each object, so we'll instantiate an object
            Object obj = e.nextElement();

            // Check to see whether this object is a task
            // If it is, we'll build a string that contains its name and type
            if (obj.getClass().getName().
                equals("org.apache.tools.ant.UnknownElement")
                ||
                obj.getClass().getName().
                equals(this.getClass().getName())) {

                Task aTask = (Task) obj;

                setString =
                    "Reference to " + aTask.getTaskName() + " found, of type "
                    + aTask.getClass().getName() + ". ";
                setString = setString + "Its id is "
                    + aTask.getRuntimeConfigurableWrapper().
            getAttributeMap().get("id") + ".";
            }
        }
        return setString;
    }

    /**
     * <p>A central logging method that all the life-cycle methods call
     * to display the state of the task.
     * It displays the value of the <code>name</code> attribute
     * and other information about the task,
     * including the name of its project and its location in the build file.</p>
     * @param method The name of the method that issued the logging call
     */
    public final void logAll(final String method) {
        log("---------------", Project.MSG_VERBOSE);
        log(method + " called", Project.MSG_VERBOSE);
        log("Value of name attribute: " + name, Project.MSG_VERBOSE);
        log("Value of the body text: " + text, Project.MSG_VERBOSE);
        log("Project: " + getProject().getName(), Project.MSG_VERBOSE);

        // Here we build some information on the location
        // within the build file
        String locationString = getLocation().getFileName();
        locationString = locationString + " at line "
            + getLocation().getLineNumber();

        // Location.getColumnNumber() is for Ant 1.7+
        // Comment it out if you are using Ant 1.6.x
        //locationString = locationString + " and column "
        // + getLocation().getColumnNumber();

        log("Location: " + locationString, Project.MSG_VERBOSE);

        // We could use the Location.toString() method
        //log("Location: " + getLocation(), Project.MSG_VERBOSE);

        log("Target: " + getOwningTarget(), Project.MSG_VERBOSE);

        // referenceCheck() returns a string with information
        // on any references to custom tasks
        log(referenceCheck(), Project.MSG_VERBOSE);

        // If the configuration wrapper is null, we use its
        // run-time equivalent
        if (getWrapper() == null) {
            log("Reference id: "
                + getRuntimeConfigurableWrapper().getAttributeMap().get("id"),
                Project.MSG_VERBOSE);
        } else {
            // This time we use the protected getWrapper() method
            log("Reference id: " + getWrapper().getAttributeMap().get("id"),
                Project.MSG_VERBOSE);
        }

        log("---------------", Project.MSG_VERBOSE);
    }
}

