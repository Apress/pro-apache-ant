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
package org.mwrm.ant.listeners;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.BuildEvent;

/**
 * <p>A class that demonstrates some of the functionality
 * of a custom listener.</p>
 */
public class BuildEventListener implements BuildListener {

    /**
     * <p>Signals that a build has started. This event
     * is fired before any targets have started.</p>
     *
     * @param start An event with any relevant extra information.
     *              Must not be <code>null</code>.
     */
    public final void buildStarted(final BuildEvent start) {
        start.getProject().log("buildStarted() called.", Project.MSG_ERR);
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
    public final void buildFinished(final BuildEvent finish) {
        finish.getProject().log("buildFinished() called.", Project.MSG_ERR);
    }

    /**
     * <p>Signals that a target is starting.</p>
     *
     * @param start An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getTarget()
     */
    public final void targetStarted(final BuildEvent start) {
        start.getProject().log("Target [" + start.getTarget().getName()
                               + "] started.", Project.MSG_ERR);
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
    public final void targetFinished(final BuildEvent finish) {
        finish.getProject().log("Target [" + finish.getTarget().getName()
                                + "] finished.", Project.MSG_ERR);
    }

    /**
     * <p>Signals that a task is starting.</p>
     *
     * @param start An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getTask()
     */
    public final void taskStarted(final BuildEvent start) {
        start.getProject().log("Task [" + start.getTask().getTaskName()
                               + "] started.", Project.MSG_ERR);
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
    public final void taskFinished(final BuildEvent finish) {
        finish.getProject().log("Task [" + finish.getTask().getTaskName()
                                + "] finished.", Project.MSG_ERR);
    }

    /** <p>When a message is sent to this logger, Ant calls this method.</p>
     * @param event An event with any relevant extra information.
     *              Must not be <code>null</code>.
     *
     * @see BuildEvent#getMessage()
     * @see BuildEvent#getPriority()
     */
    public void messageLogged(final BuildEvent event) {
        // empty
    }
}
