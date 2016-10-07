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

/**
 * The <code>Constants</code> class contains four constants
 * that represent sort options.
 *
 */

public class Constants {

    /** Use to sort the plants by their botanical name. */
    public static final int SORT_BY_NAME = 1;

    /** Use to sort the plants by their common name. */
    public static final int SORT_BY_COMMON_NAME = 2;

    /** Use to sort the plants by their family name. */
    public static final int SORT_BY_FAMILY = 3;

    /**
     * Use to sort the plants by their botanical name
     * and exclude those plants that do not begin with the chosen letter.
     */
    public static final int SORT_BY_CHOSEN_LETTER = 4;

    /**
     * A simple constructor.
     */
    public Constants() { }

}
