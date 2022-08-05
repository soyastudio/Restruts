/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.struts.tiles2.preparer;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.AttributeContext;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.preparer.PreparerException;
import org.apache.tiles.preparer.ViewPreparer;
import org.apache.tiles.servlet.context.ServletTilesRequestContext;

/**
 * @version $Rev$ $Date$
 */
public class UrlPreparer implements ViewPreparer {

    /**
     * The URL to be used as a preparer.
     */
    private String url;

    /**
     * Constructor.
     * 
     * @param url The URL to be used as a preparer.
     */
    public UrlPreparer(String url) {
        this.url = url;
    }

    /** {@inheritDoc} */
    public void execute(TilesRequestContext tilesContext,
            AttributeContext attributeContext) throws PreparerException {

        if (tilesContext instanceof ServletTilesRequestContext) {
            ServletTilesRequestContext servletTilesContext =
                (ServletTilesRequestContext) tilesContext;
            HttpServletRequest request = servletTilesContext.getRequest();
            HttpServletResponse response = servletTilesContext.getResponse();
            RequestDispatcher rd = request.getSession().getServletContext()
                    .getRequestDispatcher(url);
            if (rd == null) {
                throw new PreparerException(
                    "Controller can't find url '" + url + "'.");
            }

            try {
                rd.include(request, response);
            } catch (ServletException e) {
                throw new PreparerException(
                        "The request dispatcher threw an exception", e);
            } catch (IOException e) {
                throw new PreparerException(
                        "The request dispatcher threw an I/O exception", e);
            }
        } else {
            throw new PreparerException("Cannot dispatch url '" + url
                    + "' since this preparer has not been called under a servlet environment");
        }
    }

}
