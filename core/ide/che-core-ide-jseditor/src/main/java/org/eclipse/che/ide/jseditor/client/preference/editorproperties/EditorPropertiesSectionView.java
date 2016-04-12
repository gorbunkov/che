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
package org.eclipse.che.ide.jseditor.client.preference.editorproperties;

import com.google.gwt.json.client.JSONValue;
import com.google.inject.ImplementedBy;


import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.jseditor.client.preference.editorproperties.property.EditorPropertyValueValidator;

import javax.validation.constraints.NotNull;

/**
 * Provides methods to control panel of editor properties.
 *
 * @author Roman Nikitenko
 */
@ImplementedBy(EditorPropertiesSectionViewImpl.class)
public interface EditorPropertiesSectionView extends View<EditorPropertiesSectionView.ActionDelegate> {

    /** Adds special property widget on special panel on view. */
    void addProperty(@NotNull String propertyId, boolean value);

    /** Adds special property widget on special panel on view. */
    void addProperty(@NotNull String propertyId, String value, EditorPropertyValueValidator validator);

    JSONValue getPropertyValueById(@NotNull String propertyId);

    interface ActionDelegate {
        /**
         * Performs some action when user change value of property.
         */
        void onPropertyChanged();
    }
}
