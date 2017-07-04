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
import java.util.*;
import accessweb.com.br.radiocontrole.model.Post;

public class Posts {
    @com.google.gson.annotations.SerializedName("lastKey")
    private BigDecimal lastKey = null;
    @com.google.gson.annotations.SerializedName("posts")
    private List<Post> posts = null;

    /**
     * Gets lastKey
     *
     * @return lastKey
     **/
    public BigDecimal getLastKey() {
        return lastKey;
    }

    /**
     * Sets the value of lastKey.
     *
     * @param lastKey the new value
     */
    public void setLastKey(BigDecimal lastKey) {
        this.lastKey = lastKey;
    }

    /**
     * Gets posts
     *
     * @return posts
     **/
    public List<Post> getPosts() {
        return posts;
    }

    /**
     * Sets the value of posts.
     *
     * @param posts the new value
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}
