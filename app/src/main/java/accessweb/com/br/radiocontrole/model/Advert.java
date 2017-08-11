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


public class Advert {
    @com.google.gson.annotations.SerializedName("type")
    private String type = null;
    @com.google.gson.annotations.SerializedName("value")
    private String value = null;
    @com.google.gson.annotations.SerializedName("local")
    private String local = null;
    @com.google.gson.annotations.SerializedName("link")
    private String link = null;

    /**
     * Gets type
     *
     * @return type
     **/
    public String getType() {
        return type;
    }

    /**
     * Sets the value of type.
     *
     * @param type the new value
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets value
     *
     * @return value
     **/
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets local
     *
     * @return local
     **/
    public String getLocal() {
        return local;
    }

    /**
     * Sets the value of local.
     *
     * @param local the new value
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * Gets link
     *
     * @return link
     **/
    public String getLink() {
        return link;
    }

    /**
     * Sets the value of link.
     *
     * @param link the new value
     */
    public void setLink(String link) {
        this.link = link;
    }

}
