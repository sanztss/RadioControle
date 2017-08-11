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

import accessweb.com.br.radiocontrole.model.Channel;
import java.util.*;
import accessweb.com.br.radiocontrole.model.Social;

public class Settings {
    @com.google.gson.annotations.SerializedName("modules")
    private List<String> modules = null;
    @com.google.gson.annotations.SerializedName("adverts")
    private List<Advert> adverts = null;
    @com.google.gson.annotations.SerializedName("linkAndroid")
    private String linkAndroid = null;
    @com.google.gson.annotations.SerializedName("linkIos")
    private String linkIos = null;
    @com.google.gson.annotations.SerializedName("color")
    private String color = null;
    @com.google.gson.annotations.SerializedName("logo")
    private String logo = null;
    @com.google.gson.annotations.SerializedName("splash")
    private String splash = null;
    @com.google.gson.annotations.SerializedName("announcement")
    private String announcement = null;
    @com.google.gson.annotations.SerializedName("rss")
    private String rss = null;
    @com.google.gson.annotations.SerializedName("social")
    private List<Social> social = null;
    @com.google.gson.annotations.SerializedName("channels")
    private List<Channel> channels = null;

    /**
     * Gets modules
     *
     * @return modules
     **/
    public List<String> getModules() {
        return modules;
    }

    /**
     * Sets the value of modules.
     *
     * @param modules the new value
     */
    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    /**
     * Gets adverts
     *
     * @return adverts
     **/
    public List<Advert> getAdverts() {
        return adverts;
    }

    /**
     * Sets the value of adverts.
     *
     * @param adverts the new value
     */
    public void setAdverts(List<Advert> adverts) {
        this.adverts = adverts;
    }

    /**
     * Gets linkAndroid
     *
     * @return linkAndroid
     **/
    public String getLinkAndroid() {
        return linkAndroid;
    }

    /**
     * Sets the value of linkAndroid.
     *
     * @param linkAndroid the new value
     */
    public void setLinkAndroid(String linkAndroid) {
        this.linkAndroid = linkAndroid;
    }

    /**
     * Gets linkIos
     *
     * @return linkIos
     **/
    public String getLinkIos() {
        return linkIos;
    }

    /**
     * Sets the value of linkIos.
     *
     * @param linkIos the new value
     */
    public void setLinkIos(String linkIos) {
        this.linkIos = linkIos;
    }

    /**
     * Gets color
     *
     * @return color
     **/
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of color.
     *
     * @param color the new value
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets logo
     *
     * @return logo
     **/
    public String getLogo() {
        return logo;
    }

    /**
     * Sets the value of logo.
     *
     * @param logo the new value
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * Gets splash
     *
     * @return splash
     **/
    public String getSplash() {
        return splash;
    }

    /**
     * Sets the value of splash.
     *
     * @param splash the new value
     */
    public void setSplash(String splash) {
        this.splash = splash;
    }

    /**
     * Gets announcement
     *
     * @return announcement
     **/
    public String getAnnouncement() {
        return announcement;
    }

    /**
     * Sets the value of announcement.
     *
     * @param announcement the new value
     */
    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    /**
     * Gets rss
     *
     * @return rss
     **/
    public String getRss() {
        return rss;
    }

    /**
     * Sets the value of rss.
     *
     * @param rss the new value
     */
    public void setRss(String rss) {
        this.rss = rss;
    }

    /**
     * Gets social
     *
     * @return social
     **/
    public List<Social> getSocial() {
        return social;
    }

    /**
     * Sets the value of social.
     *
     * @param social the new value
     */
    public void setSocial(List<Social> social) {
        this.social = social;
    }

    /**
     * Gets channels
     *
     * @return channels
     **/
    public List<Channel> getChannels() {
        return channels;
    }

    /**
     * Sets the value of channels.
     *
     * @param channels the new value
     */
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

}
