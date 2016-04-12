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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.validation.constraints.NotNull;

/**
 * @author Roman Nikitenko
 */
public class EditorBooleanPropertyWidgetImpl extends Composite implements EditorPropertyWidget {
    private static final PropertyWidgetImplUiBinder UI_BINDER = GWT.create(PropertyWidgetImplUiBinder.class);

    @UiField
    Label     title;
    @UiField
    FlowPanel valuePanel;

    CheckBox propertyValueBox;

    private ActionDelegate delegate;

    private EditorPropertyValueValidator valueValidator;

    @AssistedInject
    public EditorBooleanPropertyWidgetImpl(@Assisted("propertyName") String propertyName,
                                           @Assisted("value") boolean value) {
        initWidget(UI_BINDER.createAndBindUi(this));
        this.title.setText(propertyName);

        propertyValueBox = new CheckBox();
        propertyValueBox.setValue(value);
        propertyValueBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                delegate.onPropertyChanged();
            }
        });
        valuePanel.add(propertyValueBox);
    }

    @Override
    public JSONValue getValue() {
        return JSONBoolean.getInstance(propertyValueBox.getValue());
    }

    @Override
    public void setValue(JSONValue value) {
        if (value != null && value.isBoolean() != null) {
            propertyValueBox.setValue(value.isBoolean().booleanValue());
        }
    }

    @Override
    public boolean isPropertyValueCorrect() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(@NotNull ActionDelegate delegate) {
        this.delegate = delegate;
    }

    interface PropertyWidgetImplUiBinder extends UiBinder<Widget, EditorBooleanPropertyWidgetImpl> {
    }
}
