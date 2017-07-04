/*
 * Copyright 2010-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package accessweb.com.br.radiocontrole.model;

import java.util.*;

public class Channel {
    @com.google.gson.annotations.SerializedName("main")
    private Boolean main = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("rds")
    private String rds = null;
    @com.google.gson.annotations.SerializedName("highStreams")
    private List<String> highStreams = null;
    @com.google.gson.annotations.SerializedName("lowStreams")
    private List<String> lowStreams = null;

    /**
     * Gets main
     *
     * @return main
     **/
    public Boolean getMain() {
        return main;
    }

    /**
     * Sets the value of main.
     *
     * @param main the new value
     */
    public void setMain(Boolean main) {
        this.main = main;
    }

    /**
     * Gets name
     *
     * @return name
     **/
    public String getName() {
        return name;
    }

    /**
     * Sets the value of name.
     *
     * @param name the new value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets rds
     *
     * @return rds
     **/
    public String getRds() {
        return rds;
    }

    /**
     * Sets the value of rds.
     *
     * @param rds the new value
     */
    public void setRds(String rds) {
        this.rds = rds;
    }

    /**
     * Gets highStreams
     *
     * @return highStreams
     **/
    public List<String> getHighStreams() {
        return highStreams;
    }

    /**
     * Sets the value of highStreams.
     *
     * @param highStreams the new value
     */
    public void setHighStreams(List<String> highStreams) {
        this.highStreams = highStreams;
    }

    /**
     * Gets lowStreams
     *
     * @return lowStreams
     **/
    public List<String> getLowStreams() {
        return lowStreams;
    }

    /**
     * Sets the value of lowStreams.
     *
     * @param lowStreams the new value
     */
    public void setLowStreams(List<String> lowStreams) {
        this.lowStreams = lowStreams;
    }

}
