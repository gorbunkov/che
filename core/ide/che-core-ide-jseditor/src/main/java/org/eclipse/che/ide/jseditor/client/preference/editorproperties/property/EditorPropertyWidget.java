/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.jseditor.client.preference.editorproperties.property;

import com.google.gwt.json.client.JSONValue;

import org.eclipse.che.ide.api.mvp.View;

/**
 * The interface provides methods to control property's widget which contains name and value of property.
 *
 * @author Roman Nikitenko
 */
public interface EditorPropertyWidget extends View<EditorPropertyWidget.ActionDelegate> {

    /** Returns property value from the property widget */
    JSONValue getValue();

    void setValue(boolean value);
    void setValue(String value);

    /** Returns {@code true} if the given value is correct and {@code false} -  otherwise. */
    boolean isPropertyValueCorrect();

    interface ActionDelegate {
        /**
         * Performs some action when user change value of property.
         */
        void onPropertyChanged();
    }
}
