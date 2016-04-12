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

import com.google.inject.assistedinject.Assisted;

import javax.validation.constraints.NotNull;

/**
 * The factory which creates instances of {@link EditorPropertyWidget}.
 *
 * @author Roman Nikitenko
 */
public interface EditorPropertyWidgetFactory {

    /**
     * Creates new instances of {@link EditorPropertyWidget}.
     *
     * @return an instance of {@link EditorPropertyWidget}
     */
    EditorPropertyWidget create(@NotNull @Assisted("propertyName") String propertyName,
                                @NotNull @Assisted("value") boolean value);

    EditorPropertyWidget create(@NotNull @Assisted("propertyName") String propertyName,
                                @NotNull @Assisted("value") String value,
                                EditorPropertyValueValidator valueValidator);
}
