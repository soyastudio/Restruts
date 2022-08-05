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
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts.tiles2.commands;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.chain.contexts.ServletActionContext;
import org.apache.struts.config.ForwardConfig;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;


/**
 * <p>Command class intended to perform responsibilities of the
 * TilesRequestProcessor in Struts 1.1.  Does not actually dispatch requests,
 * but simply prepares the chain context for a later forward as
 * appropriate.  Should be added to a chain before something which
 * would handle a conventional ForwardConfig.</p>
 *
 * <p>This class will never have any effect on the chain unless a
 * <code>TilesDefinitionFactory</code> can be found; however it does not
 * consider the absence of a definition factory to be a fatal error; the
 * command simply returns false and lets the chain continue.</p>
 *
 * <p>To initialize the <code>TilesDefinitionFactory</code>, use
 * <code>org.apache.struts.chain.commands.legacy.TilesPlugin</code>.  This class
 * is a simple extension to <code>org.apache.struts.tiles2.TilesPlugin</code>
 * which simply does not interfere with your choice of <code>RequestProcessor</code>
 * implementation.
 *  </p>
 *
 *
 */
public class TilesPreProcessor implements Command {


    // ------------------------------------------------------ Instance Variables


    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory.getLog(TilesPreProcessor.class);

    // ---------------------------------------------------------- Public Methods


    /**
     * <p>If the current <code>ForwardConfig</code> is using "tiles",
     * perform necessary pre-processing to set up the <code>TilesContext</code>
     * and substitute a new <code>ForwardConfig</code> which is understandable
     * to a <code>RequestDispatcher</code>.</p>
     *
     * <p>Note that if the command finds a previously existing
     * <code>AttributeContext</code> in the request, then it
     * infers that it has been called from within another tile,
     * so instead of changing the <code>ForwardConfig</code> in the chain
     * <code>Context</code>, the command uses <code>RequestDispatcher</code>
     * to <em>include</em> the tile, and returns true, indicating that the processing
     * chain is complete.</p>
     *
     * @param context The <code>Context</code> for the current request
     *
     * @throws Exception If something goes wrong.
     * @return <code>false</code> in most cases, but true if we determine
     * that we're processing in "include" mode.
     */
    public boolean execute(Context context) throws Exception {

        // Is there a Tiles Definition to be processed?
        ServletActionContext sacontext = (ServletActionContext) context;
        ForwardConfig forwardConfig = sacontext.getForwardConfig();
        if (forwardConfig == null || forwardConfig.getPath() == null) {
            // this is not a serious error, so log at low priority
            LOG.debug("No forwardConfig or no path, so pass to next command.");
            return (false);
        }


        TilesContainer container = TilesAccess.getContainer(sacontext
                .getContext());
        if (container == null) {
            LOG.debug("Tiles container not found, so pass to next command.");
            return false;
        }

        if (container.isValidDefinition(forwardConfig.getPath(), new Object[] {
                sacontext.getRequest(), sacontext.getResponse() })) {
            container.render(forwardConfig.getPath(), new Object[] {
                    sacontext.getRequest(), sacontext.getResponse() });
            sacontext.setForwardConfig(null);
        } else {
            // ignore not found
            if (LOG.isDebugEnabled()) {
                LOG.debug("Cannot find definition '" + forwardConfig.getPath()
                        + "'");
            }
        }

        return false;
    }
}
