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

package org.mwrm.plants.servlets;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Vector;
import java.util.Enumeration;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import org.mwrm.plants.SelectData;
import org.mwrm.plants.Constants;

    /**
     * <p>The servlet client for the plant application.<p>
     *
     * <p>Extracts the <code>show</code> parameter from the request
     * to determine what the user wants to see.
     * Valid values for <code>show</code> are:</p>
     *
     * <ul>
     * <li><code>common</code>: Order by common name</li>
     * <li><code>family</code>: Order by family</li>
     * <li><code>name</code>: Order by botanical name (default)</li>
     * </ul>
     *
     * <p>If the client sends a <code>letter</code> parameter,
     * then the search is limited to records that begin with that letter.</p>
     */
public class PlantServlet extends HttpServlet {

    /**
     * <p>Extracts the <code>show</code> parameter from the request
     * to determine what the user wants to see.
     * Valid values for <code>show</code> are:</p>
     *
     * <ul>
     * <li><code>common</code>: Order by common name</li>
     * <li><code>family</code>: Order by family</li>
     * <li><code>name</code>: Order by botanical name (default)</li>
     * </ul>
     *
     * <p>If the client sends a <code>letter</code> parameter,
     * then the search is limited to records that begin with that letter.</p>
     *
     * <p>Once the choice has been extracted,
     * this servlet uses the <code>org.mwrm.plants.SelectData</code> class
     * to get results from the database. Once it has the results,
     * it places them in the session under the name "results"
     * and forwards the request to <code>/plants/displayPage.jsp</code>,
     * which displays the first page of the results.</p>
     *
     * <p>If the <code>debug</code> servlet initialization parameter
     * is set to <code>true</code> the results
     * will also be sent to standard <code>out</code>.</p>
     *
     * @param request The request object.
     * @param response The response object.
     *
     * @throws ServletException
     * If there is a problem when processing the request
     * @throws IOException If there is a problem writing the response
     */
    public final void doGet(final HttpServletRequest request,
                            final HttpServletResponse response)
        throws ServletException, IOException {
        // The default choice
        int choice = Constants.SORT_BY_NAME;

        // Check the show parameter
        // to see if the user wants to select anything else
        if (request.getParameter("show").equals("common")) {
            choice = Constants.SORT_BY_COMMON_NAME;
        } else if (request.getParameter("show").equals("family")) {
            choice = Constants.SORT_BY_FAMILY;
        }

        // The results from the database
        Vector results = null;
        // The letter that the user wants to sort by
        String letter = null;

        // If the letter parameter is set, then make the appropriate choice
        if (request.getParameter("letter") != null) {
            letter = request.getParameter("letter").toLowerCase();
            choice = Constants.SORT_BY_CHOSEN_LETTER;
        }

        // Obtain the results. This is a Vector of HashMaps
        try {
            results = SelectData.getData(choice, letter);
        } catch (ClassNotFoundException cnfe) {
            throw new ServletException(cnfe.getMessage());
        } catch (SQLException sqle) {
            throw new ServletException(sqle.getMessage());
        }

        // The debug servlet initialization parameter sets output options
        if (getServletConfig().getInitParameter("debug").equals("true")) {
            // The top of the results display
            System.out.println("\n-----------------------------");

            // If there is no data in the results, tell the user
            if (results.isEmpty()) {
                System.out.println("No results found.");
                System.out.println("-----------------------------");
            } else {

                // Each record in the database is a HashMap
                HashMap record = new HashMap();

                // Iterate over the results
                for (Enumeration enum = results.elements();
                     enum.hasMoreElements();) {

                    // The current record
                    record = (HashMap) enum.nextElement();

                    // The cultivar name is optional
                    String cultivar = "";

                    // If the cultivar_name column is present,
                    // add it to the output string
                    if (!(record.get("cultivar_name") == null)) {
                        cultivar = " '" + record.get("cultivar_name") + "'";
                    }

                    // Print them to standard out
                    System.out.println("Name: "
                                       + record.get("name") + cultivar);
                    System.out.println("Common name: "
                                       + record.get("common_name"));
                    System.out.println("Family: "
                                       + record.get("family"));
                    System.out.println("Description: "
                                       + record.get("description"));
                    System.out.println("Image: "
                                       + record.get("image"));
                    System.out.println("-----------------------------");
                }
            }
        }

        // Now store the results in the session
        HttpSession session = request.getSession(true);

        // If the results are empty,
        // then store a flag to tell the application as such,
        // then remove the results object so the pages don't try to work with it
        if (results.isEmpty()) {
            session.setAttribute("noResults", "true");
            session.removeAttribute("results");

            // There are some results,
            // so place them in the session and remove the noResults flag,
            // so that the application knows there are results
        } else {
            session.setAttribute("results", results);
            session.removeAttribute("noResults");

            // The pages will need to know the number of results
            session.setAttribute("resultsSize", "" + results.size() + "");
        }

        // Forward the request to the display page
        getServletContext()
            .getRequestDispatcher("/plants/displayResults.jsp?start=0")
            .forward(request, response);
    }

    /**
     * Sends any <code>POST</code> requests to the <code>doGet</code> method.
     *
     * @param request The request object.
     * @param response The response object.
     *
     * @throws ServletException
     * If there is a problem when processing the request
     * @throws IOException If there is a problem writing the response
     */
    public final void doPost(final HttpServletRequest request,
                             final HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
}
