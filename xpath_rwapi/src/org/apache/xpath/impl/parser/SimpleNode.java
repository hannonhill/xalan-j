/* Generated By:JJTree: Do not edit this line. SimpleNode.java */
/*
 * The Apache Software License, Version 1.1
 * 
 * Copyright (c) 2002-2003 The Apache Software Foundation. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  3. The end-user documentation included with the redistribution, if any,
 * must include the following acknowledgment: "This product includes software
 * developed by the Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself, if and
 * wherever such third-party acknowledgments normally appear.
 *  4. The names "Xalan" and "Apache Software Foundation" must not be used to
 * endorse or promote products derived from this software without prior written
 * permission. For written permission, please contact apache@apache.org.
 *  5. Products derived from this software may not be called "Apache", nor may
 * "Apache" appear in their name, without prior written permission of the
 * Apache Software Foundation.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * APACHE SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 * 
 * This software consists of voluntary contributions made by many individuals
 * on behalf of the Apache Software Foundation and was originally based on
 * software copyright (c) 2002, International Business Machines Corporation.,
 * http://www.ibm.com. For more information on the Apache Software Foundation,
 * please see <http://www.apache.org/> .
 */
package org.apache.xpath.impl.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.xpath.expression.ExpressionFactory;
import org.apache.xpath.expression.StaticContext;
import org.apache.xpath.impl.CastableOrCastExprImpl;
import org.apache.xpath.impl.ConditionalExprImpl;
import org.apache.xpath.impl.ContextItemExprImpl;
import org.apache.xpath.impl.ExpressionFactoryImpl;
import org.apache.xpath.impl.ForAndQuantifiedExprImpl;
import org.apache.xpath.impl.FunctionCallImpl;
import org.apache.xpath.impl.InstanceOfExprImpl;
import org.apache.xpath.impl.KindTestImpl;
import org.apache.xpath.impl.LiteralImpl;
import org.apache.xpath.impl.NameTestImpl;
import org.apache.xpath.impl.OperatorImpl;
import org.apache.xpath.impl.PathExprImpl;
import org.apache.xpath.impl.SequenceTypeImpl;
import org.apache.xpath.impl.SingleTypeImpl;
import org.apache.xpath.impl.StepExprImpl;
import org.apache.xpath.impl.TreatExprImpl;
import org.apache.xpath.impl.VariableImpl;

/**
 * JavaCC-based foundation class for XPath AST nodes. Provides basic
 * functionnalities like AST node creation, AST manipulation (addition,
 * removal) and clonage
 */
public class SimpleNode implements Node, Cloneable
{
	public static boolean PRODUCE_RAW_TREE = false;
	public static boolean DIAGNOSE_CREATE = false;

	static private ExpressionFactory m_exprFact = new ExpressionFactoryImpl();

	public static boolean inSequenceType; // should be in parser
	public static boolean inSingleType; // should be in parser
	private static short fqState; // ForAndQuantified state
	final public static short FQ_EXPECT_RANGE_VAR = 0;
	final public static short FQ_EXPECT_IN_SEQ = 1;
	final public static short FQ_EXPECT_RESULT = 2;

	// Static methods
	
	// TODO: thread-safe
	
	public static ExpressionFactory getExpressionFactory()
	{
		return m_exprFact;
	}
	
	public static void setExpressionFactory(ExpressionFactory factory)
	{
		m_exprFact = factory;
	}

	public static StaticContext getStaticContext()
	{
		return ((ExpressionFactoryImpl) m_exprFact).getStaticContext();
	}

	public static short getFQState()
	{
		return fqState;
	}

	public static void setFQState(short state)
	{
		fqState = state;
	}

	public static Node jjtCreate(XPath parser, int id)
	{
		if (DIAGNOSE_CREATE)
			System.out.println("jjtCreate: "
					+ XPathTreeConstants.jjtNodeName[id]);

		if (PRODUCE_RAW_TREE)
		{
			return new SimpleNode(id);
		}
		else
		{
			return jjtCreate(parser.getNodeFactory(), id);
		}
	}
	
	protected static Node jjtCreate(NodeFactory nodeFactory, int id)
	{
		Node newNode;
		switch (id)
		{
			case XPathTreeConstants.JJTNAMETEST :
				newNode = nodeFactory.createNameTestNode(id);

			if (newNode == null)
			{
				newNode = new NameTestImpl(id);
			}

				break;

			case XPathTreeConstants.JJTPITEST :
			// [Aug22Draft]
			case XPathTreeConstants.JJTCOMMENTTEST :
			case XPathTreeConstants.JJTTEXTTEST :
			case XPathTreeConstants.JJTANYKINDTEST :
			case XPathTreeConstants.JJTELEMENTTEST :
			case XPathTreeConstants.JJTATTRIBUTETEST :
			case XPathTreeConstants.JJTDOCUMENTTEST :
				// If in the context of a sequence type, create a SequenceType!
				if (inSequenceType)
				{
					newNode = nodeFactory.createSequenceTypeNode(id);

					if (newNode == null)
					{
						newNode = new SequenceTypeImpl(id);
					}
				}
				else
				{
					newNode = nodeFactory.createKindTestNode(id);

					if (newNode == null)
					{
						newNode = new KindTestImpl(id);
					}
				}
				break;

			case XPathTreeConstants.JJTSTEPEXPR :
			case XPathTreeConstants.JJTPATTERNSTEP :
				newNode = nodeFactory.createStepNode(id);

			if (newNode == null)
			{
				newNode = new StepExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTINTEGERLITERAL :
			case XPathTreeConstants.JJTDOUBLELITERAL :
			case XPathTreeConstants.JJTSTRINGLITERAL :
			case XPathTreeConstants.JJTDECIMALLITERAL :
				newNode = (LiteralImpl) nodeFactory.createLiteralNode(id);

			if (newNode == null)
			{
				newNode = new LiteralImpl(id);
			}

				break;

			case XPathTreeConstants.JJTINSTANCEOFEXPR :
				newNode = (InstanceOfExprImpl) nodeFactory.createNode(id);

			if (newNode == null)
			{
				newNode = new InstanceOfExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTQUANTIFIEDEXPR :
			case XPathTreeConstants.JJTFOREXPR :
				// [Aug22Draft]
				newNode = (ForAndQuantifiedExprImpl) nodeFactory.createNode(id);

			if (newNode == null)
			{
				newNode = new ForAndQuantifiedExprImpl(id);
			}

			fqState = FQ_EXPECT_RESULT;

				break;

			case XPathTreeConstants.JJTIFEXPR :
				newNode = (ConditionalExprImpl) nodeFactory.createNode(id);

			if (newNode == null)
			{
				newNode = new ConditionalExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTPATHEXPR :
			case XPathTreeConstants.JJTPATHPATTERN :
				newNode = nodeFactory.createPathNode(id);

			if (newNode == null)
			{
				newNode = new PathExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTUNARYEXPR :
				newNode = nodeFactory.createOperatorNode(id);

			if (newNode == null)
			{
				newNode = new OperatorImpl(id);
			}
				break;

			// [Aug22Draft]
			case XPathTreeConstants.JJTEXPR :
			case XPathTreeConstants.JJTADDITIVEEXPR :
			case XPathTreeConstants.JJTMULTIPLICATIVEEXPR :
			case XPathTreeConstants.JJTUNIONEXPR :
			case XPathTreeConstants.JJTRANGEEXPR :
			case XPathTreeConstants.JJTOREXPR :
			case XPathTreeConstants.JJTANDEXPR :
			case XPathTreeConstants.JJTCOMPARISONEXPR :
			case XPathTreeConstants.JJTPATTERN :
			case XPathTreeConstants.JJTINTERSECTEXCEPTEXPR :
				newNode = nodeFactory.createOperatorNode(id);

			if (newNode == null)
			{
				newNode = new OperatorImpl(id);
			}

				break;

			case XPathTreeConstants.JJTFUNCTIONCALL :
			case XPathTreeConstants.JJTIDKEYPATTERN :
				newNode = nodeFactory.createFunctionCallNode(id);

			if (newNode == null)
			{
				newNode = new FunctionCallImpl(id);
			}

				break;

			case XPathTreeConstants.JJTVARNAME :
				newNode = nodeFactory.createVarNameNode(id);

			if (newNode == null)
			{
				newNode = new VariableImpl(id);
			}

				break;

			case XPathTreeConstants.JJTEMPTY :
			case XPathTreeConstants.JJTQNAMEFORSEQUENCETYPE :
				if (inSingleType)
				{
					newNode = new IQNameWrapper(id);
				}
				else
				{
					newNode = nodeFactory.createSequenceTypeNode(id);

					if (newNode == null)
					{
						newNode = new SequenceTypeImpl(id);
					}
				}
				break;

			case XPathTreeConstants.JJTSINGLETYPE :
				newNode = nodeFactory.createSingleTypeNode(id);

			if (newNode == null)
			{
				newNode = new SingleTypeImpl(id);
			}
				break;

			case XPathTreeConstants.JJTCASTEXPR :
				newNode = nodeFactory.createCastAsNode(id);

			if (newNode == null)
			{
				newNode = new CastableOrCastExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTCASTABLEEXPR :
				newNode = (CastableOrCastExprImpl) nodeFactory
						.createCastableAsNode(id);

			if (newNode == null)
			{
				newNode = new CastableOrCastExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTTREATEXPR :
				newNode = nodeFactory.createTreatAsNode(id);

			if (newNode == null)
			{
				newNode = new TreatExprImpl(id);
			}

				break;

			case XPathTreeConstants.JJTDOT :
				newNode = nodeFactory.createContextItemNode(id);

			if (newNode == null)
			{
				newNode = new ContextItemExprImpl(id);
			}
				break;

			// The nodes below are filtered: no customisation possible

			case XPathTreeConstants.JJTSTRINGLITERALFORKINDTEST :
			case XPathTreeConstants.JJTNCNAMEFORPI :
				newNode = new IStringLiteral(id);
				break;

			case XPathTreeConstants.JJTNODENAME :
			case XPathTreeConstants.JJTTYPENAME :
				newNode = new INodeName(id);
				break;

			case XPathTreeConstants.JJTANYNAME :
				newNode = Singletons.ANYNAME;
				break;

			case XPathTreeConstants.JJTSTAR :
			case XPathTreeConstants.JJTNCNAMECOLONSTAR :
			case XPathTreeConstants.JJTSTARCOLONNCNAME :
			case XPathTreeConstants.JJTQNAME :
			case XPathTreeConstants.JJTQNAMEFORITEMTYPE :
				newNode = new IQNameWrapper(id);
				break;

			case XPathTreeConstants.JJTQNAMELPAR :
			case XPathTreeConstants.JJTKEYLPAR :
			case XPathTreeConstants.JJTIDLPAR :
				newNode = new IQNameWrapper(XPathTreeConstants.JJTQNAMELPAR);
				break;

			case XPathTreeConstants.JJTDOTDOT :
				newNode = Singletons.DOTDOT;

				break;

			case XPathTreeConstants.JJTAT :
				// Filter?
				newNode = Singletons.AT;

				break;

			// [Aug22Draft][NR]
			case XPathTreeConstants.JJTUNARYPLUS :
				newNode = Singletons.PLUS;

				break;

			case XPathTreeConstants.JJTUNARYMINUS :
				newNode = Singletons.MINUS;

				break;

			case XPathTreeConstants.JJTXPATH :
				newNode = XPathNode.m_singleton;

				break;

			case XPathTreeConstants.JJTSEQUENCETYPE :
				newNode = ISequenceType.getSingleton();
				break;

			case XPathTreeConstants.JJTXPATH2 :
			case XPathTreeConstants.JJTMATCHPATTERN :
				newNode = XPath2Node.m_singleton;
				break;

			case XPathTreeConstants.JJTPREDICATES :
				newNode = new IPredicates(id);
				// can use a singleton (but children need to be reinitialized)
				break;

			case XPathTreeConstants.JJTNODETEST :
				newNode = new SimpleNode(id);
				break;

			case XPathTreeConstants.JJTELEMENTTYPE :
				// filter?
				newNode = Singletons.ELEMENT;
				break;

			case XPathTreeConstants.JJTATTRIBUTETYPE :
				// filter?
				newNode = Singletons.ATTRIBUTE;

				break;

			case XPathTreeConstants.JJTITEM :
				newNode = Singletons.ITEM;
				break;

			// [Aug22Draft][NR]
			case XPathTreeConstants.JJTOCCURRENCEZEROORONE :
				newNode = Singletons.QMARK;
				break;

			case XPathTreeConstants.JJTOCCURRENCEONEORMORE :
				newNode = Singletons.OCC_PLUS;
				break;

			case XPathTreeConstants.JJTOCCURRENCEZEROORMORE :
				newNode = Singletons.OCC_MULTIPLY;
				break;

			case XPathTreeConstants.JJTROOT :
				newNode = Singletons.ROOT;

				break;

			case XPathTreeConstants.JJTROOTDESCENDANTS :
				newNode = Singletons.ROOTDESCENDANT;

				break;

			case XPathTreeConstants.JJTSLASHSLASH :
				newNode = Singletons.SLASHSLASH;

				break;

			case XPathTreeConstants.JJTAXISCHILD :
				newNode = IAxis.AXIS_CHILD;

				break;

			case XPathTreeConstants.JJTAXISATTRIBUTE :
				newNode = IAxis.AXIS_ATTRIBUTE;

				break;

			case XPathTreeConstants.JJTAXISDESCENDANT :
				newNode = IAxis.AXIS_DESCENDANT;

				break;

			case XPathTreeConstants.JJTAXISSELF :
				newNode = IAxis.AXIS_SELF;

				break;

			case XPathTreeConstants.JJTAXISDESCENDANTORSELF :
				newNode = IAxis.AXIS_DESCENDANTORSELF;

				break;

			case XPathTreeConstants.JJTAXISFOLLOWINGSIBLING :
				newNode = IAxis.AXIS_FOLLOWINGSIBLING;

				break;

			case XPathTreeConstants.JJTAXISFOLLOWING :
				newNode = IAxis.AXIS_FOLLOWING;

				break;

			case XPathTreeConstants.JJTAXISNAMESPACE :
				newNode = IAxis.AXIS_NAMESPACE;

				break;

			case XPathTreeConstants.JJTAXISPARENT :
				newNode = IAxis.AXIS_PARENT;

				break;

			case XPathTreeConstants.JJTAXISANCESTOR :
				newNode = IAxis.AXIS_ANCESTOR;

				break;

			case XPathTreeConstants.JJTAXISPRECEDINGSIBLING :
				newNode = IAxis.AXIS_PRECEDINGSIBLING;

				break;

			case XPathTreeConstants.JJTAXISPRECEDING :
				newNode = IAxis.AXIS_PRECEDING;

				break;

			case XPathTreeConstants.JJTAXISANCESTORORSELF :
				newNode = IAxis.AXIS_ANCESTORORSELF;
				break;

			case XPathTreeConstants.JJTRETURN :
			case XPathTreeConstants.JJTSATISFIES :
				newNode = Singletons.RETURN;
				break;

			case XPathTreeConstants.JJTIN :
				newNode = Singletons.IN;
				break;

			case XPathTreeConstants.JJTSOME :
				newNode = Singletons.SOME;
				break;

			case XPathTreeConstants.JJTEVERY :
				newNode = Singletons.EVERY;
				break;

			case XPathTreeConstants.JJTNILLABLE :
				newNode = Singletons.NILLABLE;
				break;

			case XPathTreeConstants.JJTELEMENTTYPEFORDOCUMENTTEST :
				// Filter?
				newNode = Singletons.ELEMENTTYPEFORDOCUMENTTEST;
				break;

			case XPathTreeConstants.JJTELEMENTTYPEFORKINDTEST :

			// Below: xpath grammar unit not implemented yet
			case XPathTreeConstants.JJTVOID :
				// don't belong to the XPath grammar. Generated by javaCC??
				newNode = new SimpleNode(id);
				break;

			default :
				// if (DIAGNOSE_CREATE)
				System.out.println();
			System.out.flush();
			System.err.println("\n   ** case not implemented: "
					+ XPathTreeConstants.jjtNodeName[id]);
			newNode = new SimpleNode(id);
			//System.exit(-1);
			return newNode;
		}

		if (DIAGNOSE_CREATE)
			System.out.println("   node created: "
					+ newNode.getClass().getName());

		return newNode;
	}

	/**
	 * @param id
	 * @return
	 */
	public static Node jjtCreate(int id)
	{
		// When this one is called?
		return new SimpleNode(id);
	}

	// State
	/**
	 * List of child nodes
	 */
	protected List m_children;

	/**
	 * Parent AST node
	 */
	protected SimpleNode m_parent;

	protected int id;

	// Constructors
	protected SimpleNode()
	{
		initChildren();
	}

	/**
	 * Creates a new SimpleNode object.
	 */
	public SimpleNode(int i)
	{
		this();
		id = i;
	}

	// Methods
	private void initChildren()
	{
		if (initialChildNumber() >= 0)
		{
			m_children = new ArrayList(initialChildNumber());
		}
	}

	/**
	 * Gets the number of potential initial. May be used internally to
	 * optimally build the children list.
	 * 
	 * @return if -1: AST won't never have children.
	 */
	protected int initialChildNumber()
	{
		return 3;
	}

	/**
	 * Gets the node as a string
	 * 
	 * @param expr
	 * @param abbreviate
	 */
	public void getString(StringBuffer expr, boolean abbreviate)
	{
	}

	public Object clone()
	{
		SimpleNode clone;
		try
		{
			clone = (SimpleNode) super.clone();
			deepClone(clone);
			clone.jjtSetParent(null);
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException();
		}

	}

	/**
	 * Clone children
	 * 
	 * @return
	 */
	private void deepClone(SimpleNode clone)
	{
		if (m_children != null && !m_children.isEmpty())
		{
			int size = m_children.size();
			ArrayList cloneChildren = new ArrayList(size);

			SimpleNode sn;
			for (int i = 0; i < size; i++)
			{
				sn = (SimpleNode) ((SimpleNode) m_children.get(i)).clone();
				cloneChildren.add(sn);
				sn.jjtSetParent(clone);
			}
			clone.m_children = cloneChildren;
		}
	}

	/**
	 * Gets the precedence order of the operator (Infinite if the expression is
	 * not an operator)
	 */
	protected short getOperatorPrecedence()
	{
		return Short.MIN_VALUE;
	}

	/**
	 * Tells whenever this operator has a lower precedence than its parent.
	 */
	protected boolean lowerPrecedence()
	{
		return m_parent != null
				&& m_parent.getOperatorPrecedence() > getOperatorPrecedence();
	}

	// Parser
	/**
	 * Insert child at the the ith position
	 * 
	 * @param n
	 */
	public void jjtInsertChild(Node n, int idx)
	{
		if (STATS)
			m_arrayCopyCount++;

		m_children.add(idx, n);
		n.jjtSetParent(this);
	}

	/**
	 * Insert children of the given node at the given position
	 * 
	 * @param sn
	 */
	public void jjtInsertNodeChildren(Node sn, int idx)
	{
		List n = ((SimpleNode) sn).m_children;

		if (n != null)
		{
			if (STATS)
				m_arrayCopyCount++;

			m_children.addAll(idx, n);

			for (int i = n.size() - 1; i >= 0; i--)
			{
				((SimpleNode) n.get(i)).jjtSetParent(this);
			}
		}
	}

	/**
	 * Remove the given node from the list of children
	 * 
	 * @param child
	 * @return The node that has been removed or null whether the given node
	 *         doesn't belong to the child list
	 */
	protected Node jjtRemoveChild(Node child)
	{
		if (m_children == null || m_children.isEmpty())
		{
			return null;
		}

		if (m_children.remove(child))
		{
			child.jjtSetParent(null);
			return child;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Method processToken.
	 * 
	 * @param token
	 */
	public void processToken(Token token)
	{
	}

	/**
	 * Tells whether this node can be reduced. See subclasses in which cases an
	 * AST node can be reduced.
	 * 
	 * @return boolean
	 */
	public boolean canBeReduced()
	{
		return false;
	}

	/**
	 * Gets reduced node or the specified node itself if not possible to reduce
	 * (canBeReduced returns false)
	 * 
	 * @param node
	 * @return
	 */
	protected SimpleNode reducedNode(SimpleNode node)
	{
		if (node.canBeReduced())
		{
			return (SimpleNode) node.jjtGetChild(0);
		}
		return node;
	}

	// Implements org.apache.xpath.impl.parser.Node (JJTree)
	public void jjtOpen()
	{
	}

	public void jjtClose()
	{
	}

	public void jjtSetParent(Node n)
	{
		m_parent = (SimpleNode) n;
	}

	public Node jjtGetParent()
	{
		return m_parent;
	}

	public void jjtAddChild(Node n, int i)
	{
		if (m_children.size() <= i)
		{
			for (int j = i - m_children.size(); j >= 0; j--)
			{
				m_children.add(null);
			}
		}

		m_children.set(i, n);
		n.jjtSetParent(this);
	}

	public Node jjtGetChild(int i)
	{
		//assert m_children != null;

		return (Node) m_children.get(i);
	}

	public int jjtGetNumChildren()
	{
		return (m_children == null) ? 0 : m_children.size();
	}

	public Object jjtAccept(XPathVisitor visitor, Object data)
	{
		return visitor.visit(this, data);
	}

	public int getId()
	{
		return id;
	}

	// Debugging
	final public static boolean STATS = false;
	public static int m_arrayCopyCount;

	/*
	 * You can override these two methods in subclasses of SimpleNode to
	 * customize the way the node appears when the tree is dumped. If your
	 * output uses more than one line you should override toString(String),
	 * otherwise overriding toString() is probably all
	 */
	public String toString()
	{
		return XPathTreeConstants.jjtNodeName[id] + " classname=" + getClass();
	}

	/**
	 *  
	 */
	public String toString(String prefix)
	{
		return prefix + toString();
	}

	/**
	 * Dump AST
	 */
	public void dump(String prefix)
	{
		dump(prefix, System.out);
	}

	/*
	 * Override this method if you want to customize how the node dumps
	 */
	public void dump(String prefix, java.io.PrintStream out)
	{
		out.println(toString(prefix));

		for (int i = 0; i < jjtGetNumChildren(); ++i)
		{
			SimpleNode n = (SimpleNode) jjtGetChild(i);

			if (n != null)
			{
				n.dump(prefix + " ", out);
			}
		}
	}

}