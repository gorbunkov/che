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

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.preferences.PreferencesManager;
import org.eclipse.che.ide.jseditor.client.JsEditorConstants;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.AUTO_COMPLETE_COMMENTS;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.AUTO_PAIR_ANGLE_BRACKETS;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.AUTO_PAIR_BRACES;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.AUTO_PAIR_PARENTHESES;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.AUTO_PAIR_QUOTATIONS;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.AUTO_PAIR_SQUARE_BRACKETS;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.EXPAND_TAB;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_ANNOTATION_RULER;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_CONTENT_ASSIST_AUTOMATICALLY;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_FOLDING_RULER;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_LINE_NUMBER_RULER;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_OCCURRENCES;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_OVERVIEW_RULER;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SHOW_ZOOM_RULER;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.SMART_INDENTATION;
import static org.eclipse.che.ide.jseditor.client.preference.editorproperties.EditorProperties.TAB_SIZE;

/**
 * The class contains editor properties IDs which match to properties' names.
 *
 * @author Roman Nikitenko
 */

@Singleton
public class EditorPropertiesManager {

    /** The editor settings property name. */
    private static final String EDITOR_SETTINGS_PROPERTY = "editorSettings";

    private final static Map<String, String> names = new HashMap<>();
    private static Map<String, JSONValue> defaultProperties;

    private PreferencesManager preferencesManager;

    @Inject
    public EditorPropertiesManager(JsEditorConstants locale,
                                   PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;

        names.put(TAB_SIZE.toString(), locale.propertyTabSize());
        names.put(EXPAND_TAB.toString(), locale.propertyExpandTab());
        names.put(AUTO_PAIR_PARENTHESES.toString(), locale.propertyAutoPairParentheses());
        names.put(AUTO_PAIR_BRACES.toString(), locale.propertyAutoPairBraces());
        names.put(AUTO_PAIR_SQUARE_BRACKETS.toString(), locale.propertyAutoPairSquareBrackets());
        names.put(AUTO_PAIR_ANGLE_BRACKETS.toString(), locale.propertyAutoPairAngelBrackets());
        names.put(AUTO_PAIR_QUOTATIONS.toString(), locale.propertyAutoPairQuotations());
        names.put(AUTO_COMPLETE_COMMENTS.toString(), locale.propertyAutoCompleteComments());
        names.put(SMART_INDENTATION.toString(), locale.propertySmartIndentation());
        names.put(SHOW_ANNOTATION_RULER.toString(), locale.propertyShowAnnotationRuler());
        names.put(SHOW_LINE_NUMBER_RULER.toString(), locale.propertyShowLineNumberRuler());
        names.put(SHOW_FOLDING_RULER.toString(), locale.propertyShowFoldingRuler());
        names.put(SHOW_OVERVIEW_RULER.toString(), locale.propertyShowOverviewRuler());
        names.put(SHOW_ZOOM_RULER.toString(), locale.propertyShowZoomRuler());
        names.put(SHOW_OCCURRENCES.toString(), locale.propertyShowOccurrences());
        names.put(SHOW_CONTENT_ASSIST_AUTOMATICALLY.toString(), locale.propertyShowContentAssistAutomatically());
    }

    /**
     * Returns property name using special id.
     * Note: method can throw {@link IllegalArgumentException} if name not found.
     *
     * @param propertyId
     *         id for which name will be returned
     * @return name of property
     */
    @NotNull
    public String getPropertyNameById(@NotNull String propertyId) {
        String name = names.get(propertyId);

        if (name == null) {
            throw new IllegalArgumentException(getClass() + " Property name is not found for property ID " + propertyId);
        }

        return name;
    }

    public Map<String, JSONValue> getEditorProperties() {
        String properties = preferencesManager.getValue(EDITOR_SETTINGS_PROPERTY);
        if (properties == null) {
            return getDefaultEditorProperties();
        }

        return readPropertiesFromJson(properties);
    }

    public JSONObject getJsonEditorProperties() {
        Map<String, JSONValue> editorProperties = getEditorProperties();
        JSONObject jsonProperties = new JSONObject();
        for (String property : editorProperties.keySet()) {
            jsonProperties.put(property, editorProperties.get(property));
        }

        return jsonProperties;
    }

    private static Map<String, JSONValue> readPropertiesFromJson(String jsonProperties) {
        Map<String, JSONValue> result = new HashMap<>();

        JSONValue parsed = JSONParser.parseStrict(jsonProperties);
        JSONObject jsonObj = parsed.isObject();
        if (jsonObj != null) {
            for (String key : jsonObj.keySet()) {
                JSONValue jsonValue = jsonObj.get(key);
                result.put(key, jsonValue);
            }
        }
        return result;
    }

    public static Map<String, JSONValue> getDefaultEditorProperties() {
        if (defaultProperties != null) {
            return defaultProperties;
        }
        defaultProperties = new HashMap<>();

        // TextViewOptions (tabs)
        defaultProperties.put(TAB_SIZE.toString(), new JSONNumber(4));
        defaultProperties.put(EXPAND_TAB.toString(), JSONBoolean.getInstance(true));

        // SourceCodeActions (typing)
        defaultProperties.put(AUTO_PAIR_PARENTHESES.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(AUTO_PAIR_BRACES.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(AUTO_PAIR_SQUARE_BRACKETS.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(AUTO_PAIR_ANGLE_BRACKETS.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(AUTO_PAIR_QUOTATIONS.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(AUTO_COMPLETE_COMMENTS.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(SMART_INDENTATION.toString(), JSONBoolean.getInstance(true));

        // editor features (rulers)
        defaultProperties.put(SHOW_ANNOTATION_RULER.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(SHOW_LINE_NUMBER_RULER.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(SHOW_FOLDING_RULER.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(SHOW_OVERVIEW_RULER.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(SHOW_ZOOM_RULER.toString(), JSONBoolean.getInstance(true));

        // language tools
        defaultProperties.put(SHOW_OCCURRENCES.toString(), JSONBoolean.getInstance(true));
        defaultProperties.put(SHOW_CONTENT_ASSIST_AUTOMATICALLY.toString(), JSONBoolean.getInstance(true));

        return defaultProperties;
    }

    public void storePreferences(Map<String, JSONValue> editorProperties) {
        JSONObject jsonProperties = new JSONObject();
        for (String property : editorProperties.keySet()) {
            jsonProperties.put(property, editorProperties.get(property));
        }
        preferencesManager.setValue(EDITOR_SETTINGS_PROPERTY, jsonProperties.toString());
    }
}
