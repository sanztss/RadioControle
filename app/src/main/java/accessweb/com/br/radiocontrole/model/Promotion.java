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

public class Promotion {
    @com.google.gson.annotations.SerializedName("endingDate")
    private String endingDate = null;
    @com.google.gson.annotations.SerializedName("image")
    private String image = null;
    @com.google.gson.annotations.SerializedName("linkRegulation")
    private String linkRegulation = null;
    @com.google.gson.annotations.SerializedName("linkYoutube")
    private String linkYoutube = null;
    @com.google.gson.annotations.SerializedName("name")
    private String name = null;
    @com.google.gson.annotations.SerializedName("prizes")
    private List<PromotionPrizesItem> prizes = null;
    @com.google.gson.annotations.SerializedName("radioId")
    private String radioId = null;
    @com.google.gson.annotations.SerializedName("drawDate")
    private String drawDate = null;
    @com.google.gson.annotations.SerializedName("thumbnail")
    private String thumbnail = null;

    /**
     * Gets endingDate
     *
     * @return endingDate
     **/
    public String getEndingDate() {
        return endingDate;
    }

    /**
     * Sets the value of endingDate.
     *
     * @param endingDate the new value
     */
    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    /**
     * Gets image
     *
     * @return image
     **/
    public String getImage() {
        return image;
    }

    /**
     * Sets the value of image.
     *
     * @param image the new value
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets linkRegulation
     *
     * @return linkRegulation
     **/
    public String getLinkRegulation() {
        return linkRegulation;
    }

    /**
     * Sets the value of linkRegulation.
     *
     * @param linkRegulation the new value
     */
    public void setLinkRegulation(String linkRegulation) {
        this.linkRegulation = linkRegulation;
    }

    /**
     * Gets linkYoutube
     *
     * @return linkYoutube
     **/
    public String getLinkYoutube() {
        return linkYoutube;
    }

    /**
     * Sets the value of linkYoutube.
     *
     * @param linkYoutube the new value
     */
    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
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
     * Gets prizes
     *
     * @return prizes
     **/
    public List<PromotionPrizesItem> getPrizes() {
        return prizes;
    }

    /**
     * Sets the value of prizes.
     *
     * @param prizes the new value
     */
    public void setPrizes(List<PromotionPrizesItem> prizes) {
        this.prizes = prizes;
    }

    /**
     * Gets radioId
     *
     * @return radioId
     **/
    public String getRadioId() {
        return radioId;
    }

    /**
     * Sets the value of radioId.
     *
     * @param radioId the new value
     */
    public void setRadioId(String radioId) {
        this.radioId = radioId;
    }

    /**
     * Gets drawDate
     *
     * @return drawDate
     **/
    public String getDrawDate() {
        return drawDate;
    }

    /**
     * Sets the value of drawDate.
     *
     * @param drawDate the new value
     */
    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    /**
     * Gets thumbnail
     *
     * @return thumbnail
     **/
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Sets the value of thumbnail.
     *
     * @param thumbnail the new value
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
