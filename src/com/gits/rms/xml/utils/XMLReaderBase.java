/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management (pm@jdom.org).
 
 In addition, we request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos 
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the JDOM Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 JDOM Project, please see <http://www.jdom.org/>.
 
 */

package com.gits.rms.xml.utils;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Base class for implementing an XML reader.
 * 
 * Adapted from David Megginson's XMLFilterImpl and XMLFilterBase.
 */
public abstract class XMLReaderBase extends DefaultHandler implements LexicalHandler, XMLReader {

    // //////////////////////////////////////////////////////////////////
    // Constructors.
    // //////////////////////////////////////////////////////////////////

    protected static final Attributes EMPTY_ATTS = new AttributesImpl();

    // //////////////////////////////////////////////////////////////////
    // Convenience methods.
    // //////////////////////////////////////////////////////////////////

    protected static final String[] LEXICAL_HANDLER_NAMES = {
        "http://xml.org/sax/properties/lexical-handler",
        "http://xml.org/sax/handlers/LexicalHandler" };

    private ContentHandler contentHandler = null;

    private DTDHandler dtdHandler = null;

    private EntityResolver entityResolver = null;

    private ErrorHandler errorHandler = null;

    private LexicalHandler lexicalHandler = null;

    /**
     * Creates new XMLReaderBase.
     */
    public XMLReaderBase() {
    }

    /**
     * Sends character data.
     * 
     * @param ch
     *            An array of characters.
     * @param start
     *            The starting position in the array.
     * @param length
     *            The number of characters to use from the array.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#characters
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.characters(ch, start, length);
        }
    }

    /**
     * Add a string of character data, with XML escaping.
     * 
     * <p>
     * This is a convenience method that takes an XML String, converts it to a
     * character array, then invokes {@link @see
     * org.xml.sax.ContentHandler#characters}.
     * </p>
     * 
     * @param data
     *            The character data.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see @see org.xml.sax.ContentHandler#characters
     */
    public void characters(String data) throws SAXException {
        char ch[] = data.toCharArray();
        this.characters(ch, 0, ch.length);
    }

    /*
     * Sends comment.
     * 
     * @param ch An array holding the characters in the comment.
     * 
     * @param start The starting position in the array.
     * 
     * @param length The number of characters to use from the array.
     * 
     * @exception org.xml.sax.SAXException If a filter further down the chain
     * raises an exception.
     * 
     * @see org.xml.sax.ext.LexicalHandler#comment
     */
    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.comment(ch, start, length);
        }
    }

    /**
     * Add an element with character data content but no Namespace URI or qname.
     * 
     * <p>
     * This is a convenience method to add a complete element with character
     * data content, including the start tag and end tag. The method provides an
     * empty string for the Namespace URI, and empty string for the qualified
     * name. It invokes
     * {@link #dataElement(String, String, String, Attributes, String)}
     * directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @param atts
     *            The element's attributes.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement(String localName, Attributes atts, String content) throws SAXException {
        this.dataElement("", localName, "", atts, content);
    }

    /**
     * Add an element with character data content but no attributes or Namespace
     * URI.
     * 
     * <p>
     * This is a convenience method to add a complete element with character
     * data content, including the start tag and end tag. The method provides an
     * empty string for the Namespace URI, and empty string for the qualified
     * name, and an empty attribute list. It invokes
     * {@link #dataElement(String, String, String, Attributes, String)}
     * directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement(String localName, String content) throws SAXException {
        this.dataElement("", localName, "", EMPTY_ATTS, content);
    }

    /**
     * Add an element with character data content but no qname or attributes.
     * 
     * <p>
     * This is a convenience method to add a complete element with character
     * data content, including the start tag and end tag. This method provides
     * an empty string for the qname and an empty attribute list. It invokes
     * {@link #dataElement(String, String, String, Attributes, String)}
     * directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement(String uri, String localName, String content) throws SAXException {
        this.dataElement(uri, localName, "", EMPTY_ATTS, content);
    }

    /**
     * Add an element with character data content.
     * 
     * <p>
     * This is a convenience method to add a complete element with character
     * data content, including the start tag and end tag.
     * </p>
     * 
     * <p>
     * This method invokes {@link @see org.xml.sax.ContentHandler#startElement},
     * followed by {@link #characters(String)}, followed by {@link @see
     * org.xml.sax.ContentHandler#endElement}.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @param qName
     *            The element's default qualified name.
     * @param atts
     *            The element's attributes.
     * @param content
     *            The character data content.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see #characters(String)
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void dataElement(String uri, String localName, String qName, Attributes atts, String content) throws SAXException {
        this.startElement(uri, localName, qName, atts);
        this.characters(content);
        this.endElement(uri, localName, qName);
    }

    // //////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.XMLReader.
    // //////////////////////////////////////////////////////////////////

    /**
     * Add an empty element without a Namespace URI, qname or attributes.
     * 
     * <p>
     * This method will supply an empty string for the qname, and empty string
     * for the Namespace URI, and an empty attribute list. It invokes
     * {@link #emptyElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see #emptyElement(String, String, String, Attributes)
     */
    public void emptyElement(String localName) throws SAXException {
        this.emptyElement("", localName, "", EMPTY_ATTS);
    }

    /**
     * Add an empty element without a Namespace URI or qname.
     * 
     * <p>
     * This method will provide an empty string for the Namespace URI, and empty
     * string for the qualified name. It invokes
     * {@link #emptyElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @param atts
     *            The element's attribute list.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void emptyElement(String localName, Attributes atts) throws SAXException {
        this.emptyElement("", localName, "", atts);
    }

    /**
     * Add an empty element without a qname or attributes.
     * 
     * <p>
     * This method will supply an empty string for the qname and an empty
     * attribute list. It invokes
     * {@link #emptyElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see #emptyElement(String, String, String, Attributes)
     */
    public void emptyElement(String uri, String localName) throws SAXException {
        this.emptyElement(uri, localName, "", EMPTY_ATTS);
    }

    /**
     * Add an empty element.
     * 
     * Both a {@link #startElement startElement} and an {@link #endElement
     * endElement} event will be passed on down the filter chain.
     * 
     * @param uri
     *            The element's Namespace URI, or the empty string if the
     *            element has no Namespace or if Namespace processing is not
     *            being performed.
     * @param localName
     *            The element's local name (without prefix). This parameter must
     *            be provided.
     * @param qName
     *            The element's qualified name (with prefix), or the empty
     *            string if none is available. This parameter is strictly
     *            advisory: the writer may or may not use the prefix attached.
     * @param atts
     *            The element's attribute list.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void emptyElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        this.startElement(uri, localName, qName, atts);
        this.endElement(uri, localName, qName);
    }

    /*
     * Sends end of CDATA.
     * 
     * @exception org.xml.sax.SAXException If a filter further down the chain
     * raises an exception.
     * 
     * @see org.xml.sax.ext.LexicalHandler#endCDATA
     */
    @Override
    public void endCDATA() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endCDATA();
        }
    }

    /**
     * Send end of document.
     * 
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#endDocument
     */
    @Override
    public void endDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.endDocument();
        }
    }

    /**
     * Sends end of DTD.
     * 
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#endDTD
     */
    @Override
    public void endDTD() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endDTD();
        }
    }

    /**
     * End an element without a Namespace URI or qname.
     * 
     * <p>
     * This method will supply an empty string for the qName and an empty string
     * for the Namespace URI. It invokes
     * {@link #endElement(String, String, String)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement(String localName) throws SAXException {
        this.endElement("", localName, "");
    }

    /**
     * End an element without a qname.
     * 
     * <p>
     * This method will supply an empty string for the qName. It invokes
     * {@link #endElement(String, String, String)} directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    public void endElement(String uri, String localName) throws SAXException {
        this.endElement(uri, localName, "");
    }

    /**
     * Sends end of element.
     * 
     * @param uri
     *            The element's Namespace URI, or the empty string.
     * @param localName
     *            The element's local name, or the empty string.
     * @param qName
     *            The element's qualified (prefixed) name, or the empty string.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#endElement
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.endElement(uri, localName, qName);
        }
    }

    /*
     * Sends end of entity.
     * 
     * @param name The name of the entity that is ending.
     * 
     * @exception org.xml.sax.SAXException If a filter further down the chain
     * raises an exception.
     * 
     * @see org.xml.sax.ext.LexicalHandler#endEntity
     */
    @Override
    public void endEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endEntity(name);
        }
    }

    /**
     * Sends end of namespace prefix mapping.
     * 
     * @param prefix
     *            The Namespace prefix.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#endPrefixMapping
     */
    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.endPrefixMapping(prefix);
        }
    }

    /**
     * Sends error.
     * 
     * @param e
     *            The error as an exception.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ErrorHandler#error
     */
    @Override
    public void error(SAXParseException e) throws SAXException {
        if (this.errorHandler != null) {
            this.errorHandler.error(e);
        }
    }

    /**
     * Sends fatal error.
     * 
     * @param e
     *            The error as an exception.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ErrorHandler#fatalError
     */
    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        if (this.errorHandler != null) {
            this.errorHandler.fatalError(e);
        }
    }

    // //////////////////////////////////////////////////////////////////
    // Registration of org.xml.sax.ext.LexicalHandler.
    // //////////////////////////////////////////////////////////////////

    /**
     * Get the content event handler.
     * 
     * @return The current content handler, or null if none was set.
     * @see org.xml.sax.XMLReader#getContentHandler
     */
    @Override
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    /**
     * Get the current DTD event handler.
     * 
     * @return The current DTD handler, or null if none was set.
     * @see org.xml.sax.XMLReader#getDTDHandler
     */
    @Override
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    // //////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.EntityResolver.
    // //////////////////////////////////////////////////////////////////

    /**
     * Get the current entity resolver.
     * 
     * @return The current entity resolver, or null if none was set.
     * @see org.xml.sax.XMLReader#getEntityResolver
     */
    @Override
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    // //////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.DTDHandler.
    // //////////////////////////////////////////////////////////////////

    /**
     * Get the current error event handler.
     * 
     * @return The current error handler, or null if none was set.
     * @see org.xml.sax.XMLReader#getErrorHandler
     */
    @Override
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    /**
     * Look up the state of a feature.
     * 
     * <p>
     * This will always fail.
     * </p>
     * 
     * @param name
     *            The feature name.
     * @return The current state of the feature.
     * @exception org.xml.sax.SAXNotRecognizedException
     *                When the XMLReader does not recognize the feature name.
     * @exception org.xml.sax.SAXNotSupportedException
     *                When the XMLReader recognizes the feature name but cannot
     *                determine its state at this time.
     * @see org.xml.sax.XMLReader#getFeature
     */
    @Override
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Feature: " + name);
    }

    // //////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.ContentHandler.
    // //////////////////////////////////////////////////////////////////

    /**
     * Get the current lexical handler.
     * 
     * @return The current lexical handler, or null if none was set.
     */
    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    /**
     * Look up the value of a property.
     * 
     * <p>
     * Only lexical-handler properties are recognized.
     * </p>
     * 
     * @param name
     *            The property name.
     * @return The current value of the property.
     * @exception org.xml.sax.SAXNotRecognizedException
     *                When the XMLReader does not recognize the feature name.
     * @exception org.xml.sax.SAXNotSupportedException
     *                When the XMLReader recognizes the property name but cannot
     *                determine its value at this time.
     * @see org.xml.sax.XMLReader#setFeature
     */
    @Override
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                return this.getLexicalHandler();
            }
        }
        throw new SAXNotRecognizedException("Property: " + name);
    }

    /**
     * Sends ignorable whitespace.
     * 
     * @param ch
     *            An array of characters.
     * @param start
     *            The starting position in the array.
     * @param length
     *            The number of characters to use from the array.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#ignorableWhitespace
     */
    @Override
    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.ignorableWhitespace(ch, start, length);
        }
    }

    /**
     * Add notation declaration.
     * 
     * @param name
     *            The notation name.
     * @param publicId
     *            The notation's public identifier, or null.
     * @param systemId
     *            The notation's system identifier, or null.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.DTDHandler#notationDecl
     */
    @Override
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        if (this.dtdHandler != null) {
            this.dtdHandler.notationDecl(name, publicId, systemId);
        }
    }

    /**
     * Parse a document. Subclass must implement.
     * 
     * @param input
     *            The input source for the document entity.
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     * @exception java.io.IOException
     *                An IO exception from the parser, possibly from a byte
     *                stream or character stream supplied by the application.
     * @see org.xml.sax.XMLReader#parse(org.xml.sax.InputSource)
     */
    @Override
    public abstract void parse(InputSource input) throws SAXException, IOException;

    /**
     * Parse a document.
     * 
     * @param systemId
     *            The system identifier as a fully-qualified URI.
     * @exception org.xml.sax.SAXException
     *                Any SAX exception, possibly wrapping another exception.
     * @exception java.io.IOException
     *                An IO exception from the parser, possibly from a byte
     *                stream or character stream supplied by the application.
     * @see org.xml.sax.XMLReader#parse(java.lang.String)
     */
    @Override
    public void parse(String systemId) throws SAXException, IOException {
        this.parse(new InputSource(systemId));
    }

    /**
     * Sends processing instruction.
     * 
     * @param target
     *            The processing instruction target.
     * @param data
     *            The text following the target.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#processingInstruction
     */
    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.processingInstruction(target, data);
        }
    }

    /**
     * Resolves an external entity.
     * 
     * @param publicId
     *            The entity's public identifier, or null.
     * @param systemId
     *            The entity's system identifier.
     * @return A new InputSource or null for the default.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @exception java.io.IOException
     *                The client may throw an I/O-related exception while
     *                obtaining the new InputSource.
     * @see org.xml.sax.EntityResolver#resolveEntity
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException /*
                                                                                            * IOException
                                                                                            * added
                                                                                            * in
                                                                                            * SAX2
                                                                                            * .01
                                                                                            * bugfix
                                                                                            * release
                                                                                            */
    {
        if (this.entityResolver != null) {
            try {
                return this.entityResolver.resolveEntity(publicId, systemId);
            } catch (IOException ex) {
                throw new SAXException(ex);
            }
        } else {
            return null;
        }
    }

    /**
     * Set the content event handler.
     * 
     * @param resolver
     *            The new content handler.
     * @exception java.lang.NullPointerException
     *                If the handler is null.
     * @see org.xml.sax.XMLReader#setContentHandler
     */
    @Override
    public void setContentHandler(ContentHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Null content handler");
        } else {
            this.contentHandler = handler;
        }
    }

    /**
     * Assigns the document locator.
     * 
     * @param locator
     *            The document locator.
     * @see org.xml.sax.ContentHandler#setDocumentLocator
     */
    @Override
    public void setDocumentLocator(Locator locator) {
        if (this.contentHandler != null) {
            this.contentHandler.setDocumentLocator(locator);
        }
    }

    /**
     * Set the DTD event handler.
     * 
     * @param resolver
     *            The new DTD handler.
     * @exception java.lang.NullPointerException
     *                If the handler is null.
     * @see org.xml.sax.XMLReader#setDTDHandler
     */
    @Override
    public void setDTDHandler(DTDHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Null DTD handler");
        } else {
            this.dtdHandler = handler;
        }
    }

    // //////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.ErrorHandler.
    // //////////////////////////////////////////////////////////////////

    /**
     * Set the entity resolver.
     * 
     * @param resolver
     *            The new entity resolver.
     * @exception java.lang.NullPointerException
     *                If the resolver is null.
     * @see org.xml.sax.XMLReader#setEntityResolver
     */
    @Override
    public void setEntityResolver(EntityResolver resolver) {
        if (resolver == null) {
            throw new NullPointerException("Null entity resolver");
        } else {
            this.entityResolver = resolver;
        }
    }

    /**
     * Set the error event handler.
     * 
     * @param handle
     *            The new error handler.
     * @exception java.lang.NullPointerException
     *                If the handler is null.
     * @see org.xml.sax.XMLReader#setErrorHandler
     */
    @Override
    public void setErrorHandler(ErrorHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Null error handler");
        } else {
            this.errorHandler = handler;
        }
    }

    /**
     * Set the state of a feature.
     * 
     * <p>
     * This will always fail.
     * </p>
     * 
     * @param name
     *            The feature name.
     * @param state
     *            The requested feature state.
     * @exception org.xml.sax.SAXNotRecognizedException
     *                When the XMLReader does not recognize the feature name.
     * @exception org.xml.sax.SAXNotSupportedException
     *                When the XMLReader recognizes the feature name but cannot
     *                set the requested value.
     * @see org.xml.sax.XMLReader#setFeature
     */
    @Override
    public void setFeature(String name, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Feature: " + name);
    }

    // //////////////////////////////////////////////////////////////////
    // Implementation of org.xml.sax.ext.LexicalHandler.
    // //////////////////////////////////////////////////////////////////

    /**
     * Set the lexical handler.
     * 
     * @param handler
     *            The new lexical handler.
     * @exception java.lang.NullPointerException
     *                If the handler is null.
     */
    public void setLexicalHandler(LexicalHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Null lexical handler");
        } else {
            this.lexicalHandler = handler;
        }
    }

    /**
     * Set the value of a property.
     * 
     * <p>
     * Only lexical-handler properties are recognized.
     * </p>
     * 
     * @param name
     *            The property name.
     * @param state
     *            The requested property value.
     * @exception org.xml.sax.SAXNotRecognizedException
     *                When the XMLReader does not recognize the property name.
     * @exception org.xml.sax.SAXNotSupportedException
     *                When the XMLReader recognizes the property name but cannot
     *                set the requested value.
     * @see org.xml.sax.XMLReader#setProperty
     */
    @Override
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                this.setLexicalHandler((LexicalHandler) value);
                return;
            }
        }
        throw new SAXNotRecognizedException("Property: " + name);
    }

    /**
     * Sends skipped entity.
     * 
     * @param name
     *            The name of the skipped entity.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#skippedEntity
     */
    @Override
    public void skippedEntity(String name) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.skippedEntity(name);
        }
    }

    /*
     * Sends start of CDATA.
     * 
     * @exception org.xml.sax.SAXException If a filter further down the chain
     * raises an exception.
     * 
     * @see org.xml.sax.ext.LexicalHandler#startCDATA
     */
    @Override
    public void startCDATA() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startCDATA();
        }
    }

    /**
     * Send start of document.
     * 
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    @Override
    public void startDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.startDocument();
        }
    }

    /**
     * Sends start of DTD.
     * 
     * @param name
     *            The document type name.
     * @param publicId
     *            The declared public identifier for the external DTD subset, or
     *            null if none was declared.
     * @param systemId
     *            The declared system identifier for the external DTD subset, or
     *            null if none was declared.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ext.LexicalHandler#startDTD
     */
    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startDTD(name, publicId, systemId);
        }
    }

    /**
     * Start a new element without a Namespace URI, qname, or attributes.
     * 
     * <p>
     * This method will provide an empty string for the Namespace URI, and empty
     * string for the qualified name, and a default empty attribute list. It
     * invokes {@link #startElement(String, String, String, Attributes)}
     * directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(String localName) throws SAXException {
        this.startElement("", localName, "", EMPTY_ATTS);
    }

    // //////////////////////////////////////////////////////////////////
    // Internal state.
    // //////////////////////////////////////////////////////////////////

    /**
     * Start a new element without a Namespace URI or qname.
     * 
     * <p>
     * This method will provide an empty string for the Namespace URI, and empty
     * string for the qualified name. It invokes
     * {@link #startElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param localName
     *            The element's local name.
     * @param atts
     *            The element's attribute list.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(String localName, Attributes atts) throws SAXException {
        this.startElement("", localName, "", atts);
    }

    /**
     * Start a new element without a qname or attributes.
     * 
     * <p>
     * This method will provide a default empty attribute list and an empty
     * string for the qualified name. It invokes
     * {@link #startElement(String, String, String, Attributes)} directly.
     * </p>
     * 
     * @param uri
     *            The element's Namespace URI.
     * @param localName
     *            The element's local name.
     * @exception org.xml.sax.SAXException
     *                If a filter further down the chain raises an exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    public void startElement(String uri, String localName) throws SAXException {
        this.startElement(uri, localName, "", EMPTY_ATTS);
    }

    /**
     * Sends start of element.
     * 
     * @param uri
     *            The element's Namespace URI, or the empty string.
     * @param localName
     *            The element's local name, or the empty string.
     * @param qName
     *            The element's qualified (prefixed) name, or the empty string.
     * @param atts
     *            The element's attributes.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#startElement
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.startElement(uri, localName, qName, atts);
        }
    }

    /*
     * Sends start of entity.
     * 
     * @param name The name of the entity. If it is a parameter entity, the name
     * will begin with '%', and if it is the external DTD subset, it will be
     * "[dtd]".
     * 
     * @exception org.xml.sax.SAXException If a filter further down the chain
     * raises an exception.
     * 
     * @see org.xml.sax.ext.LexicalHandler#startEntity
     */
    @Override
    public void startEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startEntity(name);
        }
    }

    /**
     * Sends start of namespace prefix mapping.
     * 
     * @param prefix
     *            The Namespace prefix.
     * @param uri
     *            The Namespace URI.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ContentHandler#startPrefixMapping
     */
    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.startPrefixMapping(prefix, uri);
        }
    }

    // //////////////////////////////////////////////////////////////////
    // Constants.
    // //////////////////////////////////////////////////////////////////

    /**
     * Add unparsed entity declaration.
     * 
     * @param name
     *            The entity name.
     * @param publicId
     *            The entity's public identifier, or null.
     * @param systemId
     *            The entity's system identifier, or null.
     * @param notationName
     *            The name of the associated notation.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     */
    @Override
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        if (this.dtdHandler != null) {
            this.dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
        }
    }

    /**
     * Sends warning.
     * 
     * @param e
     *            The nwarning as an exception.
     * @exception org.xml.sax.SAXException
     *                The client may throw an exception during processing.
     * @see org.xml.sax.ErrorHandler#warning
     */
    @Override
    public void warning(SAXParseException e) throws SAXException {
        if (this.errorHandler != null) {
            this.errorHandler.warning(e);
        }
    }

}

// end of XMLReaderBase.java
