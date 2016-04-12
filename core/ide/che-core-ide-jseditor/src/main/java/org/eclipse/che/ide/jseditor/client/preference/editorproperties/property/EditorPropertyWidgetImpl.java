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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import javax.validation.constraints.NotNull;

/**
 * @author Roman Nikitenko
 */
public class EditorPropertyWidgetImpl extends Composite implements EditorPropertyWidget {
    private static final PropertyWidgetImplUiBinder UI_BINDER = GWT.create(PropertyWidgetImplUiBinder.class);

    @UiField
    Label     title;
    @UiField
    FlowPanel valuePanel;

    HasValue valueHolder;

    private ActionDelegate delegate;

    private final boolean                      isBooleanProperty;
    private       EditorPropertyValueValidator valueValidator;

    @AssistedInject
    public EditorPropertyWidgetImpl(@Assisted("propertyName") String propertyName,
                                    @Assisted("value") boolean value) {
        initWidget(UI_BINDER.createAndBindUi(this));
        isBooleanProperty = true;
        this.title.setText(propertyName);

        CheckBox propertyValueBox = new CheckBox();
        propertyValueBox.setValue(value);
        propertyValueBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                delegate.onPropertyChanged();
            }
        });
        valuePanel.add(propertyValueBox);
        valueHolder = propertyValueBox;
    }

    @AssistedInject
    public EditorPropertyWidgetImpl(final @Assisted("propertyName") String propertyName,
                                    final @Assisted("value") String value,
                                    final @Assisted EditorPropertyValueValidator validator) {
        initWidget(UI_BINDER.createAndBindUi(this));
        isBooleanProperty = false;
        this.valueValidator = validator;
        this.title.setText(propertyName);

        final TextBox propertyValueBox = new TextBox();
        propertyValueBox.setVisibleLength(5);
        propertyValueBox.setValue(value);
        propertyValueBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                delegate.onPropertyChanged();
            }
        });

        valuePanel.add(propertyValueBox);
        valueHolder = propertyValueBox;
    }

    @Override
    public JSONValue getValue() {
        if (isBooleanProperty) {
            return JSONBoolean.getInstance((boolean)valueHolder.getValue());
        }
        return new JSONNumber(new Double(valueHolder.getValue().toString()));
    }

    @Override
    public void setValue(boolean value) {
        valueHolder.setValue(value);
    }

    public void setValue(String value) {
        valueHolder.setValue(value);
    }

    @Override
    public boolean isPropertyValueCorrect() {
        return valueValidator == null || valueValidator.isPropertyValueCorrect(valueHolder.getValue().toString());
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(@NotNull ActionDelegate delegate) {
        this.delegate = delegate;
    }

    interface PropertyWidgetImplUiBinder extends UiBinder<Widget, EditorPropertyWidgetImpl> {
    }
}
