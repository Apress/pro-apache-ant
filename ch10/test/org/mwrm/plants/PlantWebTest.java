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

package org.mwrm.plants;

import java.io.IOException;
import java.net.MalformedURLException;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * <p>Tests the plant servlet.
 * It checks that the web application is running
 * and then checks the session is emptied
 * if the there are no results in the query.</p>
 */
public class PlantWebTest extends TestCase {

    /** The URL of the server. */
    private static final String SERVER_URL = "http://localhost:8080";

    /** The web application's name. */
    private static final String WEB_APP = "/antBook";

    /** The response code we're looking for. */
    private static final int RESPONSE_CODE = 200;

    /**
     * <p>The constructor,
     * which simply calls <code>super(name)</code>.</p>
     * @param name The name of the test
     */
    public PlantWebTest(final String name) {
        super(name);
    }

    /**
     * <p>We want to make sure that the web application is running.</p>
     * @throws MalformedURLException
     * If the URL of the web server is not correct
     * @throws SAXException
     * If the response can't be processed properly
     */
    public final void testIsRunning()
        throws MalformedURLException, SAXException {
        // Create a WebConversation object
        WebConversation wc = new WebConversation();
        try {
            // Send a request to the web application's root
            WebResponse resp = wc.getResponse(SERVER_URL + WEB_APP);

            // If there is a 200 return code, then it is available
            assertEquals("Web application not available at "
                         + SERVER_URL + WEB_APP,
                         RESPONSE_CODE, resp.getResponseCode());
        } catch (IOException ioe) {
            // We can't find the server, so we fail the test
            fail("Server not available");
        }
    }

    /**
     * <p>The application should detect that no results have been obtained.</p>
     * @throws MalformedURLException
     * If the URL of the web server is not correct
     * @throws SAXException
     * If the response can't be processed properly
     */
    public final void testSession()
        throws MalformedURLException, SAXException {
        // Create a WebConversation object
        WebConversation wc = new WebConversation();

        try {
            // First send a request that will not produce any results
            WebResponse resp =
                wc.getResponse(SERVER_URL + WEB_APP
                               + "/plants/listPlants.jsp?show=name&letter=X");
            // Check that this is the case
            assertTrue("Session not cancelled after empty results",
                       (resp.getText().indexOf("Sorry") > -1));

            // Now go to the index page,
            // where there should not be an error message
            resp = wc.getResponse(SERVER_URL + WEB_APP + "/plants/");
            assertTrue("Session not cancelled after empty results",
                       !(resp.getText().indexOf("Sorry") > -1));
        } catch (IOException ioe) {
            // We can't find the server, so we fail the test
            fail("Server not available");
        }
    }
}
