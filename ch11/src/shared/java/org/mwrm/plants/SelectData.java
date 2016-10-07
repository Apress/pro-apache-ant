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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Vector;
import java.util.Properties;

/**
 * <p>The <code>SelectData</code> class establishes a connection
 * with a database and executes a query, as selected by the client.</p>
 *
 * <p>It gets the database driver name and the URL
 * from the <code>database.properties</code> file.
 * When the results come back from the database,
 * this class places them as <code>HashMap</code> records
 * in a <code>Vector</code>. It then passes this <code>Vector</code>
 * back to the calling client.</p>
 *
 * <p>The SQL strings are:</p>
 *
 * <ul>
 * <li><code>Constants.SORT_BY_NAME</code> (the default):
 * <code>SELECT * FROM plants ORDER BY name</code></li>
 * <li><code>Constants.SORT_BY_COMMON_NAME</code>:
 * <code>SELECT * FROM plants ORDER BY common_name</code></li>
 * <li><code>Constants.SORT_BY_FAMILY</code>:
 * <code>SELECT * FROM plants ORDER BY family, name</code></li>
 * <li><code>Constants.SORT_BY_CHOSEN_LETTER</code>:
 * <code>SELECT * FROM plants WHERE name REGEXP '^X'</code>
 * where <code>X</code> is a letter supplied by the client</li>
 * </ul>
 */

public final class SelectData {

    /**
     *<p>The default constructor.</p>
     */
    private SelectData() { }

    /**
     * Get the data from the database.
     * @param choice The criteria for sorting the results.
     * This choice is held in the <code>Constants</code> class.
     * @param letter The letter to use when limiting the search,
     * should that option be chosen.
     * @return Vector
     * @throws ClassNotFoundException If the database driver is not found
     * @throws SQLException If there's a problem with database operations
     */
    public static Vector getData(final int choice, final String letter)
        throws ClassNotFoundException, SQLException {

        // Read properties file
        PropertiesLoader loader = new PropertiesLoader();
        Properties properties = loader.loadProperties();

        // First load the MySQL JDBC driver
        Class.forName(properties.getProperty("driver.name"));

        // The datasource
        String url = properties.getProperty("database.root")
            + properties.getProperty("database.name");

        // Open the connection
        Connection con = DriverManager.getConnection(url, "antBook", "antB00k");

        Statement stmt = con.createStatement();

        String select = getSelectString(choice, letter);

        // Now we get the data
        ResultSet rs = stmt.executeQuery(select);

        // We'll need the metadata when we come to populate the session object
        ResultSetMetaData rsmd = rs.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();

        Vector results = new Vector();

        while (rs.next()) {
            // We need a fresh entry every time
            HashMap record = new HashMap(numberOfColumns);

            String columnName = "";

            // For each column in the table,
            // we want to add an entry to the HashMap
            // with the same key as the column name
            for (int i = 1; i <= numberOfColumns; i++) {
                columnName = rsmd.getColumnName(i);
                record.put(columnName, rs.getString(columnName));
            }
            results.add(record);
        }

        // Close the Statement and the Connection
        stmt.close();
        con.close();

        return results;
    }

    /**
     * <p>Returns the appropriate SQL string for the choice.</p>
     * @param choice The user's choice of search criteria.
     * @param letter The letter to use when modifying the search.
     * @return String
     */
    private static String
        getSelectString(final int choice, final String letter) {

        // This is the default SELECT statement if no arguments are specified
        String selectString = "SELECT * FROM plants ORDER BY name";

        // Check the type of argument
        if (choice == Constants.SORT_BY_COMMON_NAME) {
            // Order the results by common name
            selectString = "SELECT * FROM plants ORDER BY common_name";

        } else if (choice == Constants.SORT_BY_FAMILY) {
            // Order the results by family, then botanical name
            selectString = "SELECT * FROM plants ORDER BY family, name";

        } else if (choice == Constants.SORT_BY_CHOSEN_LETTER) {
            // The search will only return those plants whose botanical name
            // begins with the specifed letter.
            selectString = "SELECT * FROM plants WHERE name REGEXP '^"
                + letter + "'";
        }

        return selectString;
    }
}
