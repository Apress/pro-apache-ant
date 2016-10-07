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

package org.mwrm.plants.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;

    /**
     * <p>Converts an integer into a character.
     * The <code>letter</code> attribute takes the integer,
     * which is converted to a char by the time the tag gets it.
     * The tag then writes the char to the client.</p>
     */
public class LettersTag extends SimpleTagSupport {

    /**
     * The character to display.
     */
    private char letter;

    /**
     * <p>Processes the tag when it is encountered on the page.</p>
     * @throws JspException
     * If there is a problem processing the tag
     * @throws IOException
     * If there is a problem writing to the client
     */
    public final void doTag() throws JspException, IOException {

        // The page that the client will receive
        JspWriter out = getJspContext().getOut();

        // Write the letter to the client
        out.print(letter);
    }

    /**
     *
     * <p>The setter method for the <code>letter</code> attribute.</p>
     *
     * @param aLetter The letter to display.
     */
    public final void setLetter(final char aLetter) {
        this.letter = aLetter;
    }
}
