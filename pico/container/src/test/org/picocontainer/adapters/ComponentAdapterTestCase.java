/*****************************************************************************
 * Copyright (C) 2003-2011 PicoContainer Committers. All rights reserved.    *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
package org.picocontainer.adapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Type;

import org.junit.Test;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.Parameter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.PicoVisitor;
import org.picocontainer.injectors.AbstractInjector;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.parameters.ConstantParameter;
import org.picocontainer.parameters.ConstructorParameters;

/**
 * Test AbstractAdapter behaviour
 * @author J&ouml;rg Schaible
 */
public class ComponentAdapterTestCase {

    @SuppressWarnings("serial")
	private static class TestAdapter<T> extends AbstractAdapter<T> {
    	
        TestAdapter(Object key, Class<T> impl, ComponentMonitor monitor) {
            super(key, impl, monitor);
        }
        TestAdapter(Object key, Class<T> impl) {
            super(key, impl);
        }

        public T getComponentInstance(PicoContainer container, Type into) throws PicoCompositionException {
            return null;
        }
        public void verify(PicoContainer container) throws PicoVerificationException {
        }

        public String getDescriptor() {
            return TestAdapter.class.getName() + ":" ;
        }
    }

    @SuppressWarnings("serial")
	private static class TestMonitoringComponentAdapter<T> extends AbstractAdapter<T> {
        TestMonitoringComponentAdapter(ComponentMonitor monitor) {
            super(null, null, monitor);
        }

        public T getComponentInstance(PicoContainer container, Type into) throws PicoCompositionException {
            return null;
        }
        public void verify(PicoContainer container) throws PicoVerificationException {
        }
        public Object getComponentKey() {
            return null;
        }
        public Class<T> getComponentImplementation() {
            return null;
        }
        public void accept(PicoVisitor visitor) {
        }

        public String getDescriptor() {
            return null;
        }
    }
    
    @SuppressWarnings("serial")
	private static class TestInstantiatingAdapter<T> extends AbstractInjector<T> {
        TestInstantiatingAdapter(Object key, Class<T> impl, Parameter... parameters) {
            super(key, impl, new NullComponentMonitor(), false, new ConstructorParameters(parameters));
        }
        @Override
        public void verify(PicoContainer container) throws PicoCompositionException {
        }

        public T getComponentInstance(PicoContainer container, Type into) throws PicoCompositionException {
            return null;
        }

        public String getDescriptor() {
            return null;
        }
    }
    
    @Test public void testComponentImplementationMayNotBeNull() {
        try {
            new TestAdapter<Object>("Key", null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            assertEquals("impl", e.getMessage());
        }
    }

    @Test public void testComponentKeyCanBeNullButNotRequested() {
        ComponentAdapter<String> componentAdapter = new TestAdapter<String>(null, String.class);
        try {
            componentAdapter.getComponentKey();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            assertEquals("key", e.getMessage());
        }
    }

    @Test public void testComponentMonitorMayNotBeNull() {
        try {
            new TestAdapter<String>("Key", String.class, null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            assertEquals("ComponentMonitor==null", e.getMessage());
        }
        try {
            new TestMonitoringComponentAdapter<Object>(null);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            assertEquals("ComponentMonitor==null", e.getMessage());
        }
    }

    @Test public void testParameterMayNotBeNull() throws Exception {
        try {
            new TestInstantiatingAdapter<String>("Key", String.class, new Parameter[]{new ConstantParameter("Value"), null});
            fail("Thrown " + NullPointerException.class.getName() + " expected");
        } catch (final NullPointerException e) {
        	assertTrue(e.getMessage().contains("Parameter 1"));
            assertTrue(e.getMessage().endsWith(" is null"));
        }
    }
    
    @Test public void testStringRepresentation() {
        ComponentAdapter<Integer> componentAdapter = new TestAdapter<Integer>("Key", Integer.class);
        assertEquals(TestAdapter.class.getName() + ":Key", componentAdapter.toString());
    }
}
