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

import org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties;

/**
 * Validator for values of {@link EditorProperties}.
 *
 * @author Roman Nikitenko
 */
public interface EditorPropertyValueValidator {
    /**
     * Validates the given {@code value}.
     * <p/>
     * Returns {@code true} if the given value is correct and {@code false} -  otherwise.
     *
     * @param value
     *         value to check for validity
     */
    boolean isPropertyValueCorrect(String value);

}
