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

package org.mwrm.plants.client;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Vector;
import java.util.Enumeration;

import org.mwrm.plants.SelectData;
import org.mwrm.plants.Constants;

/**
 * <p>The <code>PlantClient</code> class is a command-line client
 * for the plant application.</p>
 * <p>Usage:</p>
 * <ul>
 * <li><code>-c</code> Order by common name</li>
 * <li><code>-f</code> Order by family</li>
 * <li><code>-n</code> Order by botanical name (default)</li>
 * <li><code>-n [letter]</code> Order by botanical name
 * and limit the search to plants beginning with the specified letter</li>
 * </ul>
 */

public final class PlantClient {

    /**
     * <p>A simple constructor.</p>
     */
    private PlantClient() { }

    /**
     * Checks the arguments,
     * then uses the <code>org.mwrm.plants.SelectData</code>
     * class to get results from the database.
     * Once it has the results, it displays them to standard <code>out</code>.
     * @param args The command-line arguments.
     * @throws ClassNotFoundException If the database driver is not found
     * @throws SQLException If there is a problem with the database
     */
    public static void main(final String[] args)
        throws ClassNotFoundException, SQLException {

        // The default choice
        int choice = Constants.SORT_BY_NAME;

        // The user may want to select by a certain letter
        String letter = "";

        // Check that an argument has been provided
        if (args.length > 0) {
        // Check the type of argument
            if (args[0].equals("-c") || args[0].equals("")) {
                // Cannot be used with an argument just now,
                // though Ant may pass an empty string
                if (args.length > 1 && !args[1].equals("")) {
                    usage();
                }
                // Order the results by common name
                choice = Constants.SORT_BY_COMMON_NAME;

            } else if (args[0].equals("-f")) {
                // Cannot be used with an argument just now,
                // though Ant may pass an empty string
                if (args.length > 1 && !args[1].equals("")) {
                   usage();
                }
                // Order the results by family, then botanical name
                choice = Constants.SORT_BY_FAMILY;

            } else if (args[0].equals("-n")) {
                // Order the results by botanical name
                // This is the default if no arguments are specified

                // The user can provide another argument.
                // The search will only return those plants whose botanical name
                // begins with the specifed letter.
                if (args.length > 1 && !args[1].equals("")) {
                    choice = Constants.SORT_BY_CHOSEN_LETTER;
                    letter = args[1];
                }
            } else {
                // Usage information
                usage();
            }
        }

        // Obtain the results. This is a Vector of HashMaps
        Vector results = SelectData.getData(choice, letter);

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

                record = (HashMap) enum.nextElement();

                // The cultivar name is optional
                String cultivar = "";

                if (!(record.get("cultivar_name") == null)) {
                    cultivar = " '" + record.get("cultivar_name") + "'";
                }

                System.out.println("Name: " + record.get("name") + cultivar);
                System.out.println("Common name: " + record.get("common_name"));
                System.out.println("Family: " + record.get("family"));
                System.out.println("Description: " + record.get("description"));
                System.out.println("-----------------------------");
            }
        }
    }

    /**
     * <p>Print the usage information.</p>
     */
    private static void usage() {
        System.out.println("\nUsage: \n");
        System.out.println("-c \t\t Order by common name");
        System.out.println("-f \t\t Order by family");
        System.out.println("-n \t\t Order by botanical name (default)");
        System.out.println("-n [letter] \t Order by botanical name"
                           + " and limit the search to plants ");
        System.out.println("\t\t beginning with the specified letter");
        System.exit(0);
    }
}
