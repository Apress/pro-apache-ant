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
import java.util.Vector;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * <p>At each stage in a task's life cycle, this class displays information
 * to show the internal state of the task and its position with in the project.
 * It takes a <code>name</code> attribute.</p>
 *
 * <p>This class demonstrates how to nest elements within a custom task.
 * Its nested element is called <code>&lt;name&gt;</code>
 * and cannot be used in conjunction with a <code>name</code> attribute.</p>
 */
public class LifeCycleNestedTask extends Task {

    /** The name attribute of this task. */
    private String name;

    /** The body text of this task. */
    private String text;

    /** The collection of name elements. */
    private Vector nameElements = new Vector();

    /**
     * <p>The constructor displays the state of the task
     * as it is instantiated.</p>
     */
    public LifeCycleNestedTask() {
        System.out.println("---------------");
        System.out.println("Constructor called");
        System.out.println("Value of name attribute: " + this.name);
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
        if (name != null && nameElements.size() > 0) {
            String msg = "You can't specify a name attribute "
                + "and <name> elements.";
            throw new BuildException(msg);
        }
        if (name == null && nameElements.size() == 0) {
            String msg = "You must specify either a name attribute "
                + "or at least one <name> element.";
            throw new BuildException(msg);
        }
        if (nameElements.size() > 0 && text != null) {
            String msg = "You can't specify <name> elements "
                + "and body text.";
            throw new BuildException(msg);
        }

        logAll("execute()");

        // If name is not set, we want to check nested elements
        if (name == null) {
            // Get the name elements
            Enumeration e = nameElements.elements();

            // And then iterate over them
            while (e.hasMoreElements()) {
                NameElement nameElement = (NameElement) e.nextElement();

                // Usage check
                if (nameElement.getName() == null) {
                    String msg = "You must specify a name attribute "
                        + "or body text for a nested <name> element.";
                    throw new BuildException(msg);
                }
                log("Value of name element: " + nameElement.getName(),
                    Project.MSG_VERBOSE);
            }
            log("---------------", Project.MSG_VERBOSE);
        }
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

            // Check to see if this object is a custom task
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

        // If name is set, we only have one value to print
        if (name != null) {
            log("Value of name attribute: " + this.name, Project.MSG_VERBOSE);
        }

        log("Value of the body text: " + text, Project.MSG_VERBOSE);
        log("Project: " + getProject().getName(), Project.MSG_VERBOSE);

        // Here we build some information on the location
        // within the build file
        String locationString = getLocation().getFileName();
        locationString = locationString
            + " at line " + getLocation().getLineNumber();

        // Location.getColumnNumber() is for Ant 1.7+
        // Comment it out if you are using Ant 1.6.x
        //locationString = locationString
        //    + " and column " + getLocation().getColumnNumber();

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

    /**
     * <p>Adds a <code>&lt;name&gt;</code> element
     * that has not been initialized.</p>
     * @param nameElement The <code>NameElement</code>
     * object that represents the nested element
     * @see LifeCycleNestedTask.NameElement
     */
    public final void addName(final NameElement nameElement) {
        nameElements.add(nameElement);

        logAll("addName()");
        log("Value of this name: "
            + nameElement.getName(), Project.MSG_VERBOSE);
    }

    /**
     * <p>Adds a <code>&lt;name&gt;</code> element
     * that has been initialized.</p>
     * @param nameElement The <code>NameElement</code>
     * object that represents the nested element
     * @see LifeCycleNestedTask.NameElement
     */
    public final void addConfiguredName(final NameElement nameElement) {
        nameElements.add(nameElement);

        logAll("addConfiguredName()");
        log("Value of this name: " + nameElement.getName(),
            Project.MSG_VERBOSE);
    }

    /**
     * <p>Adds a <code>&lt;name&gt;</code> element
     * that has not been initialized.
     * In this case, the <code>createName()</code> method
     * has the responsibility
     * for creating the object.</p>
     * @see LifeCycleNestedTask.NameElement
     */
    public final NameElement createName() {
        NameElement nameElement = new NameElement("Madeleine");

        nameElements.add(nameElement);

        logAll("createName()");
        log("Value of this name: " + nameElement.getName(),
            Project.MSG_VERBOSE);

        return nameElement;
    }

    /**
     * <p>A class that implements
     * the nested <code>&lt;name&gt;</code> element
     * of a <code>LifeCycleNestedTask</code>.
     * @see LifeCycleNestedTask
     */
    public static class NameElement {

        /** The <code>name</code> attribute of this element. */
        private String name;

        /** Tells the class if we've used the overridden constructor. */
        private boolean usedConstructor = false;

        /** The empty constructor. */
        public NameElement() {
            // Empty
        }

        /**
         * <p>Used by the <code>LifeCycleNestedTask.createName()</code> method
         * to created a nested <code>&lt;name&gt;</code> element.</p>
         * @see LifeCycleNestedTask#createName()
         */
        public NameElement(final String text) {
            this.name = text;
            usedConstructor = true;
        }

        /**
         * <p>The mutator method for the <code>name</code> attribute.</p>
         * @param aName The name to display
         */
        public final void setName(final String aName) {
            this.name = aName;
        }

        /**
         * <p>The accessor method for the <code>name</code> attribute.</p>
         * @return String The name to display
         */
        public final String getName() {
            return name;
        }

        /**
         * <p>Sets the body text of the <code>&lt;name&gt;</code> element.
         * It contains a usage check.</p>
         * @param text The body text
         */
        public final void addText(final String text) {
            // Usage check
            if (name != null && !usedConstructor) {
                String msg = "You can't specify a name attribute "
                    + "and nested text in <name> elements.";
                throw new BuildException(msg);
            } else {
                this.name = text.trim();
            }
        }
    }
}

