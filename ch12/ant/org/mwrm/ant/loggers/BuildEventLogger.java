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
package org.mwrm.ant.loggers;

import java.io.PrintStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.BuildEvent;

/**
 * <p>A class that demonstrates some of the functionality
 * of a custom logger.</p>
 */
public class BuildEventLogger implements BuildLogger {

    /**
     * PrintStream to write non-error messages to.
     */
    private PrintStream out;

    /**
     * PrintStream to write error messages to.
     */
    private PrintStream err;

    /**
     * Sets whether to tailor output for Emacs, etc.
     * The default is <code>false</code>.
     */
    private boolean emacsMode = false;

    /**
     * We'll set this logger to log only warnings.
     */
    private int msgOutputLevel = Project.MSG_WARN;

    /**
     * <p>Signals that a build has started. This event
     * is fired before any targets have started.</p>
     *
     * @param start An event with any relevant extra information.
     *              Must not be <code>null</code>.
     */
    public final void buildStarted(final BuildEvent start) {
        start.getProject().log("Message from buildStarted().", Project.MSG_ERR);
    }

    /**
     * <p>Signals that the last target has finished. This event
     * will still be fired if an error occurred during the build.</p>
     *
     * @param finish An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    public void buildFinished(final BuildEvent finish) {
        // empty
    }

    /**
     * <p>Signals that a target is starting.</p>
     *
     * @param start An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getTarget()
     */
    public void targetStarted(final BuildEvent start) {
        // empty
    }

    /**
     * <p>Signals that a target has finished. This event will
     * still be fired if an error occurred during the build.</p>
     *
     * @param finish An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    public void targetFinished(final BuildEvent finish) {
        // empty
    }

    /**
     * <p>Signals that a task is starting.</p>
     *
     * @param start An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getTask()
     */
    public void taskStarted(final BuildEvent start) {
        // empty
    }

    /**
     * <p>Signals that a task has finished. This event will still
     * be fired if an error occurred during the build.</p>
     *
     * @param finish An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getException()
     */
    public void taskFinished(final BuildEvent finish) {
        // empty
    }

    /** <p>When a message is sent to this logger, Ant calls this method.</p>
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getMessage()
     * @see BuildEvent#getPriority()
     */
    public final void messageLogged(final BuildEvent event) {
        /// We need to determine how important this message is
        int priority = event.getPriority();

        // If it's as important as our log level, we display it
        if (priority <= msgOutputLevel) {
            out.println("messageLogged: " + event.getMessage());
        }
    }

    /**
     * <p>Sets the output stream to which this logger is to send its output.</p>
     *
     * @param output The output stream for the logger.
     *               Must not be <code>null</code>.
     */
    public final void setOutputPrintStream(final PrintStream output) {
        this.out = new PrintStream(output, true);
    }

    /**
     * <p>Sets the output stream to which this logger
     * is to send error messages.</p>
     *
     * @param errorStream The error stream for the logger.
     *                    Must not be <code>null</code>.
     */
    public final void setErrorPrintStream(final PrintStream errorStream) {
        this.err = new PrintStream(errorStream, true);
    }

    /**
     * <p>Sets this logger to produce Emacs
     * (and other editor) friendly output.</p>
     *
     * @param mode <code>true</code> if output is to be unadorned so that
     *                  Emacs and other editors can parse files names, etc.
     */
    public final void setEmacsMode(final boolean mode) {
        this.emacsMode = mode;
    }

    /**
     * <p>Sets the highest level of message this logger should respond to.</p>
     *
     * <p>Only messages with a message level lower than or equal to the
     * given level should be written to the log.</p>
     * <p>
     * Constants for the message levels are in the
     * {@link Project Project} class. The order of the levels, from least
     * to most verbose, is <code>MSG_ERR</code>, <code>MSG_WARN</code>,
     * <code>MSG_INFO</code>, <code>MSG_VERBOSE</code>,
     * <code>MSG_DEBUG</code>.</p>
     * <p>The default for this logger is
     * {@link Project#MSG_WARN Project.MSG_WARN}.</p>
     *
     * @param level the logging level for the logger.
     */
    public void setMessageOutputLevel(final int level) {
        // We will leave this empty to use the default level,
        // which we set above
    }

}
