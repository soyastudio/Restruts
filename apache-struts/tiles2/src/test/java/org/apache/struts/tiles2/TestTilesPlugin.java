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

package org.apache.struts.tiles2;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ModuleConfigFactory;
import org.apache.struts.config.PlugInConfig;
import org.apache.struts.mock.MockActionServlet;
import org.apache.struts.mock.TestMockBase;
import org.apache.struts.util.RequestUtils;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.apache.tiles.impl.KeyedDefinitionsFactoryTilesContainer;

/**
 * Tests the Tiles plugin.
 *
 * @version $Rev$ $Date$
 */
public class TestTilesPlugin extends TestMockBase {

    /**
     * The first module to configure.
     */
    protected ModuleConfig module1;

    /**
     * The second module to configure.
     */
    protected ModuleConfig module2;

    /**
     * A testing action servlet.
     */
    protected MockActionServlet actionServlet;

    /**
     * The logging object.
     */
    private static final Log LOG = LogFactory.getLog(TestTilesPlugin.class);

    // ----------------------------------------------------------------- Basics

    /**
     * Constructor.
     *
     * @param name The name of the test.
     */
    public TestTilesPlugin(String name) {
        super(name);
    }

    /**
     * Sample main method.
     *
     * @param args Arguments.
     */
    public static void main(String[] args) {
        junit.awtui.TestRunner.main(new String[] { TestTilesPlugin.class
                .getName() });
    }

    /**
     * Test suite method.
     *
     * @return The test.
     */
    public static Test suite() {
        return (new TestSuite(TestTilesPlugin.class));
    }

    // ----------------------------------------------------- Instance Variables

    // ----------------------------------------------------- Setup and Teardown

    /** {@inheritDoc} */
    public void setUp() {

        super.setUp();
        actionServlet = new MockActionServlet(context, config);
    }

    /** {@inheritDoc} */
    public void tearDown() {

        super.tearDown();

    }

    // ------------------------------------------------------- Individual Tests

    // ---------------------------------------------------------- absoluteURL()

    /**
     * Test multi factory creation when moduleAware=true.
     *
     * @throws ServletException
     *             If something goes wrong during initialization.
     * @throws InvocationTargetException
     *             Bean properties problems.
     * @throws InstantiationException
     *             Bean properties problems.
     * @throws IllegalAccessException
     *             Bean properties problems.
     * @throws ClassNotFoundException
     *             Bean properties problems.
     */
    public void testMultiFactory() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            InvocationTargetException, ServletException {
        // init TilesPlugin
        module1 = createModuleConfig("/module1", "tiles-defs.xml", true);
        module2 = createModuleConfig("/module2", "tiles-defs.xml", true);
        initModulePlugIns(module1);
        initModulePlugIns(module2);

        // mock request context
        request.setAttribute(Globals.MODULE_KEY, module1);
        request.setPathElements("/myapp", "/module1/foo.do", null, null);

        // Retrieve TilesContainer
        TilesContainer container = TilesAccess.getContainer(actionServlet
                .getServletContext());
        assertSame(container.getClass().getName(),
                KeyedDefinitionsFactoryTilesContainer.class.getName());

        // Retrieve factory for module1
        DefinitionsFactory factory1 = ((KeyedDefinitionsFactoryTilesContainer) container)
                .getDefinitionsFactory("/module1");

        assertNotNull("factory found", factory1);

        // mock request context
        request.setAttribute(Globals.MODULE_KEY, module2);
        request.setPathElements("/myapp", "/module2/foo.do", null, null);
        // Retrieve factory for module2
        DefinitionsFactory factory2 = ((KeyedDefinitionsFactoryTilesContainer) container)
                .getDefinitionsFactory("/module2");
        assertNotNull("factory found", factory2);

        // Check that factory are different
        // FIXME This assert fails!
        assertNotSame("Factory from different modules", factory1, factory2);
    }

    /**
     * Tests if the TilesPlugin does a fail-fast on multiple configuration of
     * the same module.
     *
     * @throws ServletException If something goes wrong during initialization.
     * @throws InvocationTargetException Bean properties problems.
     * @throws InstantiationException Bean properties problems.
     * @throws IllegalAccessException Bean properties problems.
     * @throws ClassNotFoundException Bean properties problems.
     */
    public void testMultiModuleFailFast() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            InvocationTargetException, ServletException {
        // init TilesPlugin
        module1 = createModuleConfig("/module1", "tiles-defs.xml", true);

        // The name is "/module1" on purpose
        module2 = createModuleConfig("/module1", "tiles-defs.xml", true);
        initModulePlugIns(module1);
        try {
            initModulePlugIns(module2);
            fail("An exception should have been thrown");
        } catch (ServletException e) {
            // It is ok
            LOG.debug("Intercepted a ServletException, it is ok", e);
        }
    }

    /**
     * Test single factory creation when moduleAware=false.
     *
     * @throws ServletException If something goes wrong during initialization.
     * @throws InvocationTargetException Bean properties problems.
     * @throws InstantiationException Bean properties problems.
     * @throws IllegalAccessException Bean properties problems.
     * @throws ClassNotFoundException Bean properties problems.
     */
    public void testSingleSharedFactory() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            InvocationTargetException, ServletException {
        // init TilesPlugin
        module1 = createModuleConfig("/module1", "tiles-defs.xml", false);
        module2 = createModuleConfig("/module2", "tiles-defs.xml", false);
        initModulePlugIns(module1);
        try {
            initModulePlugIns(module2);
            fail("An exception should have been thrown");
        } catch (ServletException e) {
            // It is ok
            LOG.debug("Intercepted a ServletException, it is ok", e);
        }

        // mock request context
        request.setAttribute(Globals.MODULE_KEY, module1);
        request.setPathElements("/myapp", "/module1/foo.do", null, null);
        // Retrieve TilesContainer
        TilesContainer container = TilesAccess.getContainer(actionServlet
                .getServletContext());
        assertSame(container.getClass().getName(), BasicTilesContainer.class
                .getName());

        // Retrieve factory for module1
        DefinitionsFactory factory1 = ((BasicTilesContainer) container)
                .getDefinitionsFactory();
        assertNotNull("factory found", factory1);

        // mock request context
        request.setAttribute(Globals.MODULE_KEY, module2);
        request.setPathElements("/myapp", "/module2/foo.do", null, null);
        // Retrieve factory for module2
        DefinitionsFactory factory2 = ((BasicTilesContainer) container)
                .getDefinitionsFactory();
        assertNotNull("factory found", factory2);

        // Check that factory are different
        assertEquals("Same factory", factory1, factory2);
    }

    /**
     * Create a module configuration.
     *
     * @param moduleName The name of the module.
     * @param configFileName The name of the configuration file.
     * @param moduleAware <code>true</code> if the configuration must be
     * module-aware.
     * @return The configuration object.
     */
    private ModuleConfig createModuleConfig(String moduleName,
            String configFileName, boolean moduleAware) {

        ModuleConfig moduleConfig = ModuleConfigFactory.createFactory()
                .createModuleConfig(moduleName);

        context.setAttribute(Globals.MODULE_KEY + moduleName, moduleConfig);

        // Set tiles plugin
        PlugInConfig pluginConfig = new PlugInConfig();
        pluginConfig.setClassName("org.apache.struts.tiles2.TilesPlugin");

        pluginConfig.addProperty("moduleAware",
                (moduleAware ? "true" : "false"));

        pluginConfig.addProperty("definitions-config",
                "/org/apache/struts/tiles2/config/" + configFileName);

        moduleConfig.addPlugInConfig(pluginConfig);
        return moduleConfig;
    }

    /**
     * Fake call to init module plugins.
     *
     * @param moduleConfig The configuration of the module.
     * @throws ServletException If something goes wrong during initialization.
     * @throws InvocationTargetException Bean properties problems.
     * @throws InstantiationException Bean properties problems.
     * @throws IllegalAccessException Bean properties problems.
     * @throws ClassNotFoundException Bean properties problems.
     */
    private void initModulePlugIns(ModuleConfig moduleConfig)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, InvocationTargetException, ServletException {
        PlugInConfig[] plugInConfigs = moduleConfig.findPlugInConfigs();
        PlugIn[] plugIns = new PlugIn[plugInConfigs.length];

        context.setAttribute(Globals.PLUG_INS_KEY + moduleConfig.getPrefix(),
                plugIns);
        for (int i = 0; i < plugIns.length; i++) {
            plugIns[i] = (PlugIn) RequestUtils
                    .applicationInstance(plugInConfigs[i].getClassName());
            BeanUtils.populate(plugIns[i], plugInConfigs[i].getProperties());
            // Pass the current plugIn config object to the PlugIn.
            // The property is set only if the plugin declares it.
            // This plugin config object is needed by Tiles
            BeanUtils.copyProperty(plugIns[i], "currentPlugInConfigObject",
                    plugInConfigs[i]);
            plugIns[i].init(actionServlet, moduleConfig);
        }
    }
}
