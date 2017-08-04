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

package accessweb.com.br.radiocontrole.util;

import java.util.*;

import accessweb.com.br.radiocontrole.model.Group;
import accessweb.com.br.radiocontrole.model.Error;
import accessweb.com.br.radiocontrole.model.Settings;
import accessweb.com.br.radiocontrole.model.Hosts;
import accessweb.com.br.radiocontrole.model.Podcasts;
import accessweb.com.br.radiocontrole.model.Posts;
import accessweb.com.br.radiocontrole.model.Post;
import accessweb.com.br.radiocontrole.model.Comments;
import accessweb.com.br.radiocontrole.model.Comment;
import accessweb.com.br.radiocontrole.model.Empty;
import accessweb.com.br.radiocontrole.model.Programs;
import accessweb.com.br.radiocontrole.model.Promotions;


@com.amazonaws.mobileconnectors.apigateway.annotation.Service(endpoint = "https://wt0vuyzu4c.execute-api.us-east-1.amazonaws.com/alpha")
public interface RadiocontroleClient {


    /**
     * A generic invoker to invoke any API Gateway endpoint.
     * @param request
     * @return ApiResponse
     */
    com.amazonaws.mobileconnectors.apigateway.ApiResponse execute(com.amazonaws.mobileconnectors.apigateway.ApiRequest request);

    /**
     *
     *
     * @param radioGroupId
     * @return Group
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/radiogroup/{radioGroupId}", method = "GET")
    Group radiogroupRadioGroupIdGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioGroupId", location = "path")
                    String radioGroupId);

    /**
     *
     *
     * @param radioId
     * @return Settings
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}", method = "GET")
    Settings radioIdGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId);

    /**
     *
     *
     * @param radioId
     * @return Hosts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/hosts", method = "GET")
    Hosts radioIdHostsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId);

    /**
     *
     *
     * @param radioId
     * @return Podcasts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/podcasts", method = "GET")
    Podcasts radioIdPodcastsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId);

    /**
     *
     *
     * @param radioId
     * @param lastKey
     * @return Posts
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/posts", method = "GET")
    Posts radioIdPostsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "lastKey", location = "query")
                    String lastKey);

    /**
     *
     *
     * @param radioId
     * @param body
     * @return Post
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/posts", method = "POST")
    Post radioIdPostsPost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId,
            Post body);

    /**
     *
     *
     * @param postId
     * @param radioId
     * @param lastKey
     * @return Comments
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/posts/{postId}/comments", method = "GET")
    Comments radioIdPostsPostIdCommentsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "postId", location = "path")
                    String postId,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "lastKey", location = "query")
                    String lastKey);

    /**
     *
     *
     * @param postId
     * @param radioId
     * @param body
     * @return Empty
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/posts/{postId}/comments", method = "POST")
    Empty radioIdPostsPostIdCommentsPost(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "postId", location = "path")
                    String postId,
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId,
            Comment body);

    /**
     *
     *
     * @param radioId
     * @return Programs
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/programs", method = "GET")
    Programs radioIdProgramsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId);

    /**
     *
     *
     * @param radioId
     * @return Promotions
     */
    @com.amazonaws.mobileconnectors.apigateway.annotation.Operation(path = "/{radioId}/promotions", method = "GET")
    Promotions radioIdPromotionsGet(
            @com.amazonaws.mobileconnectors.apigateway.annotation.Parameter(name = "radioId", location = "path")
                    String radioId);
}

