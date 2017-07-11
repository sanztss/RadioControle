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

import java.io.Serializable;
import java.math.BigDecimal;

public class Program implements Serializable{
    @com.google.gson.annotations.SerializedName("id")
    private BigDecimal id = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("startTime")
    private String startTime = null;
    @com.google.gson.annotations.SerializedName("hostId")
    private String hostId = null;
    @com.google.gson.annotations.SerializedName("sun")
    private Boolean sun = null;
    @com.google.gson.annotations.SerializedName("mon")
    private Boolean mon = null;
    @com.google.gson.annotations.SerializedName("tue")
    private Boolean tue = null;
    @com.google.gson.annotations.SerializedName("wed")
    private Boolean wed = null;
    @com.google.gson.annotations.SerializedName("thu")
    private Boolean thu = null;
    @com.google.gson.annotations.SerializedName("fri")
    private Boolean fri = null;
    @com.google.gson.annotations.SerializedName("sat")
    private Boolean sat = null;

    /**
     * Gets id
     *
     * @return id
     **/
    public BigDecimal getId() {
        return id;
    }

    /**
     * Sets the value of id.
     *
     * @param id the new value
     */
    public void setId(BigDecimal id) {
        this.id = id;
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
     * Gets startTime
     *
     * @return startTime
     **/
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of startTime.
     *
     * @param startTime the new value
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets hostId
     *
     * @return hostId
     **/
    public String getHostId() {
        return hostId;
    }

    /**
     * Sets the value of hostId.
     *
     * @param hostId the new value
     */
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    /**
     * Gets sun
     *
     * @return sun
     **/
    public Boolean getSun() {
        return sun;
    }

    /**
     * Sets the value of sun.
     *
     * @param sun the new value
     */
    public void setSun(Boolean sun) {
        this.sun = sun;
    }

    /**
     * Gets mon
     *
     * @return mon
     **/
    public Boolean getMon() {
        return mon;
    }

    /**
     * Sets the value of mon.
     *
     * @param mon the new value
     */
    public void setMon(Boolean mon) {
        this.mon = mon;
    }

    /**
     * Gets tue
     *
     * @return tue
     **/
    public Boolean getTue() {
        return tue;
    }

    /**
     * Sets the value of tue.
     *
     * @param tue the new value
     */
    public void setTue(Boolean tue) {
        this.tue = tue;
    }

    /**
     * Gets wed
     *
     * @return wed
     **/
    public Boolean getWed() {
        return wed;
    }

    /**
     * Sets the value of wed.
     *
     * @param wed the new value
     */
    public void setWed(Boolean wed) {
        this.wed = wed;
    }

    /**
     * Gets thu
     *
     * @return thu
     **/
    public Boolean getThu() {
        return thu;
    }

    /**
     * Sets the value of thu.
     *
     * @param thu the new value
     */
    public void setThu(Boolean thu) {
        this.thu = thu;
    }

    /**
     * Gets fri
     *
     * @return fri
     **/
    public Boolean getFri() {
        return fri;
    }

    /**
     * Sets the value of fri.
     *
     * @param fri the new value
     */
    public void setFri(Boolean fri) {
        this.fri = fri;
    }

    /**
     * Gets sat
     *
     * @return sat
     **/
    public Boolean getSat() {
        return sat;
    }

    /**
     * Sets the value of sat.
     *
     * @param sat the new value
     */
    public void setSat(Boolean sat) {
        this.sat = sat;
    }

}
