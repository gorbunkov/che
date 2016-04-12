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
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.machine.gwt.client.events.WsAgentStateEvent;
import org.eclipse.che.api.machine.gwt.client.events.WsAgentStateHandler;
import org.eclipse.che.ide.api.event.EditorSettingsChangedEvent;
import org.eclipse.che.ide.jseditor.client.preference.EditorPreferenceSection;
import org.eclipse.che.ide.jseditor.client.preference.editorproperties.property.EditorPropertyValueValidator;

import java.util.Map;

/** Presenter for the editor propertiesPanel section in the 'Preferences' menu. */
public class EditorPropertiesSectionPresenter implements EditorPreferenceSection, EditorPropertiesSectionView.ActionDelegate,
                                                         WsAgentStateHandler {
    /** The preference page presenter. */
    private       ParentPresenter             parentPresenter;
    private final EventBus                    eventBus;
    private final EditorPropertiesSectionView view;
    private final EditorPropertiesManager     editorPropertiesManager;

    @Inject
    public EditorPropertiesSectionPresenter(final EditorPropertiesSectionView view,
                                            final EventBus eventBus,
                                            final EditorPropertiesManager editorPropertiesManager) {
        this.view = view;
        this.view.setDelegate(this);
        this.eventBus = eventBus;
        this.editorPropertiesManager = editorPropertiesManager;

        eventBus.addHandler(WsAgentStateEvent.TYPE, this);
    }

    @Override
    public void storeChanges() {
        Map<String, JSONValue> editorProperties = editorPropertiesManager.getEditorProperties();
        for (String property : editorProperties.keySet()) {
            JSONValue actualValue = view.getPropertyValueById(property);
            actualValue = actualValue != null ? actualValue : editorProperties.get(property);
            editorProperties.put(property, actualValue);
        }
        editorPropertiesManager.storePreferences(editorProperties);
        eventBus.fireEvent(new EditorSettingsChangedEvent());
    }

    @Override
    public void refresh() {
        fillData();
    }

    @Override
    public boolean isDirty() {
        Map<String, JSONValue> editorProperties = editorPropertiesManager.getEditorProperties();
        for (String property : editorProperties.keySet()) {
            JSONValue actualValue = view.getPropertyValueById(property);
            if (actualValue != null && !actualValue.equals(editorProperties.get(property))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setParent(final ParentPresenter parent) {
        this.parentPresenter = parent;
    }

    @Override
    public void onWsAgentStarted(WsAgentStateEvent event) {
        fillData();
    }

    @Override
    public void onWsAgentStopped(WsAgentStateEvent event) {

    }

    private void fillData() {
        Map<String, JSONValue> editorProperties = editorPropertiesManager.getEditorProperties();
        for (String property : editorProperties.keySet()) {
            JSONValue value = editorProperties.get(property);
            if (value.isBoolean() != null) {
                view.addProperty(property, value.isBoolean().booleanValue());
            } else {
                view.addProperty(property, value.toString(), getNumericalPropertyValidator());
            }
        }
    }


    @Override
    public void onPropertyChanged() {
        parentPresenter.signalDirtyState();
    }

    private EditorPropertyValueValidator getNumericalPropertyValidator() {
        return new EditorPropertyValueValidator() {
            @Override
            public boolean isPropertyValueCorrect(String value) {
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };
    }
}
