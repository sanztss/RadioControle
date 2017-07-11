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

import java.math.BigDecimal;

public class Podcast {
    @com.google.gson.annotations.SerializedName("duration")
    private BigDecimal duration = null;
    @com.google.gson.annotations.SerializedName("timestamp")
    private long timestamp;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("file")
    private String file = null;

    /**
     * Gets duration
     *
     * @return duration
     **/
    public BigDecimal getDuration() {
        return duration;
    }

    /**
     * Sets the value of duration.
     *
     * @param duration the new value
     */
    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    /**
     * Gets timestamp
     *
     * @return timestamp
     **/
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of timestamp.
     *
     * @param timestamp the new value
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
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
     * Gets file
     *
     * @return file
     **/
    public String getFile() {
        return file;
    }

    /**
     * Sets the value of file.
     *
     * @param file the new value
     */
    public void setFile(String file) {
        this.file = file;
    }

}
