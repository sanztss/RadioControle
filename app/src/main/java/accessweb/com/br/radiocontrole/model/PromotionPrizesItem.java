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


public class PromotionPrizesItem {
    @com.google.gson.annotations.SerializedName("prizeImage")
    private String prizeImage = null;
    @com.google.gson.annotations.SerializedName("prizeName")
    private String prizeName = null;

    /**
     * Gets prizeImage
     *
     * @return prizeImage
     **/
    public String getPrizeImage() {
        return prizeImage;
    }

    /**
     * Sets the value of prizeImage.
     *
     * @param prizeImage the new value
     */
    public void setPrizeImage(String prizeImage) {
        this.prizeImage = prizeImage;
    }

    /**
     * Gets prizeName
     *
     * @return prizeName
     **/
    public String getPrizeName() {
        return prizeName;
    }

    /**
     * Sets the value of prizeName.
     *
     * @param prizeName the new value
     */
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

}
