/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * Helper mixin that will render a variable element type.
 * Similar to the Any component in Tapestry3.
 *
 * @version $Id$
 */
@Import(library = {"../Chenillekit.js", "../Cookie.js", "${tapestry.scriptaculous}/dragdrop.js",
		"Resizable.js"}, stylesheet = {"Resizable.css"})
public class Resizable implements ClientElement
{
	/**
	 * The field component to which this mixin is attached.
	 */
	@InjectContainer
	private ClientElement clientElement;

	@Inject
	private ComponentResources resources;

	@Environmental
	private JavaScriptSupport javascriptSupport;

	@Inject
	private Cookies cookies;

	/**
	 * The id used to generate a page-unique client-side identifier for the component. If a component renders multiple
	 * times, a suffix will be appended to the to id to ensure uniqueness.
	 */
	@Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
	private String clientId;

	/**
	 * let you allow to restrict the direction of resizing.
	 * 'vertical', 'horizontal' or empty for both
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String constraint;

	/**
	 * if true, width and height of the resizable element persists via cookie.
	 * default is 'false'
	 */
	@Parameter(value = "false", defaultPrefix = BindingConstants.PROP)
	private boolean persist;

	private String assignedClientId;

	@SetupRender
	void setupRender()
	{
		assignedClientId = javascriptSupport.allocateClientId(clientId);
	}

	/**
	 * Tapestry render phase method. End a tag here.
	 *
	 * @param writer the markup writer
	 */
	@AfterRender
	void afterRender(MarkupWriter writer)
	{
		// start handle south
		writer.element("div", "id", "handle_" + clientElement.getClientId(),
					   "class", "ck-resizable-handle");
		// end handle south
		writer.end();

		if (persist)
		{
			int width = getIntValueFromCookie(clientElement.getClientId() + ".width");
			int height = getIntValueFromCookie(clientElement.getClientId() + ".height");

			if (width > 0)
				javascriptSupport.addScript("$('%s').style.width = '%dpx';", clientElement.getClientId(), width);

			if (height > 0)
				javascriptSupport.addScript("$('%s').style.height = '%dpx';", clientElement.getClientId(), height);
		}

		String jsString = "%s = new Resizable('%s', {handle:$('handle_%s')";
		if (constraint != null)
			jsString += String.format(",constraint:'%s'", constraint);
		jsString += ", persist:%s});";

		javascriptSupport.addScript(jsString, getClientId(), clientElement.getClientId(), clientElement.getClientId(), persist);
	}

	/**
	 * Returns a unique id for the element. This value will be unique for any given rendering of a
	 * page. This value is intended for use as the id attribute of the client-side element, and will
	 * be used with any DHTML/Ajax related JavaScript.
	 */
	public String getClientId()
	{
		return assignedClientId;
	}

	/**
	 * get an integer value from cookie.
	 *
	 * @param key the key of the value
	 *
	 * @return the integer value
	 */
	private int getIntValueFromCookie(String key)
	{
		int intValue = 0;
		String cookieValue = cookies.readCookieValue(key);

		if (cookieValue != null)
		{
			try
			{
				intValue = Integer.parseInt(cookieValue);
			}
			catch (NumberFormatException e)
			{
				throw new RuntimeException(e);
			}
		}

		return intValue;
	}
}