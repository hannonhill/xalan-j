package org.apache.xalan.extensions;

import java.io.IOException;
import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.functions.FuncExtFunction;

/**
 * Extension Handler that does nothing.  Used to prevent Java and/or Javascript extensions
 * from being executed.
 * 
 * @author  Mike Strauch
 * @version $Id$
 * @since   HH-2.7.1
 */
public class ExtensionHandlerEmpty extends ExtensionHandler
{
    /**
     * Default constructor
     */
    public ExtensionHandlerEmpty(String namespaceUri, String scriptLang)
    {
        super(namespaceUri, scriptLang);
    }
    
    public boolean isFunctionAvailable(String function)
    {
        return false;
    }
    
    public boolean isElementAvailable(String element)
    {
        return false;
    }
    
    public Object callFunction(String funcName, Vector args, Object methodKey,
              ExpressionContext exprContext) throws TransformerException
    {
        return null;
    }
    
    public Object callFunction(FuncExtFunction extFunction, Vector args,
              ExpressionContext exprContext) throws TransformerException
    {
        return null;
    }

    public void processElement(String localPart, ElemTemplateElement element, TransformerImpl transformer,
              Stylesheet stylesheetTree, Object methodKey) throws TransformerException, IOException
    {
        return;
    }
}