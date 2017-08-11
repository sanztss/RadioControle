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


public class Participant {
    @com.google.gson.annotations.SerializedName("email")
    private String email = null;
    @com.google.gson.annotations.SerializedName("promotionId")
    private long promotionId;
    @com.google.gson.annotations.SerializedName("radioId")
    private String radioId = null;

    /**
     * Gets email
     *
     * @return email
     **/
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of email.
     *
     * @param email the new value
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets promotionId
     *
     * @return promotionId
     **/
    public long getPromotionId() {
        return promotionId;
    }

    /**
     * Sets the value of promotionId.
     *
     * @param promotionId the new value
     */
    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
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

}
