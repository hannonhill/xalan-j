/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id$
 */
package org.apache.xml.utils;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Creates XMLReader objects and caches them for re-use.
 * This class follows the singleton pattern.
 */
public class XMLReaderManager
{

    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    private static final XMLReaderManager m_singletonManager = new XMLReaderManager();

    /**
     * Parser factory to be used to construct XMLReader objects
     */
    private static SAXParserFactory m_parserFactory;

    // Hannon Hill Modification, 11-06-2007, see the following:
    // http://issues.apache.org/jira/browse/XALANJ-2195
    // the m_inUse and m_readers instance members have been removed
    // as all caching logic was removed

    /**
     * Hidden constructor
     */
    private XMLReaderManager()
    {
    }

    /**
     * Retrieves the singleton reader manager
     */
    public static XMLReaderManager getInstance()
    {
        return m_singletonManager;
    }

    /**
     * Retrieves a cached XMLReader for this thread, or creates a new
     * XMLReader, if the existing reader is in use.  When the caller no
     * longer needs the reader, it must release it with a call to
     * {@link releaseXMLReader}.
     */
    public XMLReader getXMLReader() throws SAXException
    {

        // Hannon Hill Modification, 11-06-2007, see the following:
        // http://issues.apache.org/jira/browse/XALANJ-2195
        // all caching logic which previous revolved around a thread local
        // and a Hashtable of "in use" readers was removed, and a new reader
        // is ALWAYS returned. In addition this method was changed to be not
        // synchronized as the only thing necessitating synchronization was the
        // caching logic.

        XMLReader reader = null;

        try
        {
            try
            {
                // According to JAXP 1.2 specification, if a SAXSource
                // is created using a SAX InputSource the Transformer or
                // TransformerFactory creates a reader via the
                // XMLReaderFactory if setXMLReader is not used
                reader = XMLReaderFactory.createXMLReader();
            }
            catch (Exception e)
            {
                try
                {
                    // If unable to create an instance, let's try to use
                    // the XMLReader from JAXP
                    if (m_parserFactory == null)
                    {
                        m_parserFactory = SAXParserFactory.newInstance();
                        m_parserFactory.setNamespaceAware(true);
                    }

                    reader = m_parserFactory.newSAXParser().getXMLReader();
                }
                catch (ParserConfigurationException pce)
                {
                    throw pce; // pass along pce
                }
            }
            try
            {
                reader.setFeature(NAMESPACES_FEATURE, true);
                reader.setFeature(NAMESPACE_PREFIXES_FEATURE, false);
            }
            catch (SAXException se)
            {
                // Try to carry on if we've got a parser that
                // doesn't know about namespace prefixes.
            }
        }
        catch (ParserConfigurationException ex)
        {
            throw new SAXException(ex);
        }
        catch (FactoryConfigurationError ex1)
        {
            throw new SAXException(ex1.toString());
        }
        catch (NoSuchMethodError ex2)
        {
        }
        catch (AbstractMethodError ame)
        {
        }

        return reader;
    }

    /**
     * Mark the cached XMLReader as available.  If the reader was not
     * actually in the cache, do nothing.
     *
     * @param reader The XMLReader that's being released.
     */
    public void releaseXMLReader(XMLReader reader)
    {
        // Hannon Hill Modification, 11-06-2007, see the following:
        // http://issues.apache.org/jira/browse/XALANJ-2195
        // the content of this method was completely removed as caching
        // XMLReaders is no longer done. In addition this method was changed
        // to not be synchronized as this is now unnecessary.
    }
}
