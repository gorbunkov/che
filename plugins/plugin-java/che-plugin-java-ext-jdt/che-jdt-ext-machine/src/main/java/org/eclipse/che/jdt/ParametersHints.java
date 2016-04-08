/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors:
 * Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.jdt;

import com.google.inject.Singleton;

import org.eclipse.che.dto.server.DtoFactory;
import org.eclipse.che.ide.ext.java.shared.dto.model.MethodParameters;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.eclipse.jdt.core.IJavaElement.METHOD;

/**
 * The class provides business logic which allows to find hints of method's parameters. Also it finds methods parameter hints in
 * super classes. The class represents founded parameters as ordinary string. Parameters in string separated by coma from each other.
 * Example:
 * class Test{
 *      void x(int x, double y);
 * }
 * <p/>
 * class Test2 extends Test {
 *      void x(String y);
 * }
 *
 * Test2 test2 = new Test2();
 * test2.x();
 *
 * When we call method {@link org.eclipse.che.jdt.ParametersHints#findHints(IJavaProject, String, int)} for test2.x()
 * we will get following:
 * ________
 * int x, double y
 * String y
 * --------
 *
 * @author Dmitry Shnurenko
 */
@Singleton
public class ParametersHints {

    public List<MethodParameters> findHints(IJavaProject project, String fqn, int offset) throws JavaModelException {
        IType type = project.findType(fqn);
        if (type.isBinary()) {
            return Collections.emptyList();
        }

        IJavaElement element = getSelectedElement(type, offset);
        if (element == null) {
            return Collections.emptyList();
        }

        IJavaElement parent = element.getParent();
        if (!(parent instanceof IType) || !(element.getElementType() == METHOD)) {
            return Collections.emptyList();
        }

        List<MethodParameters> result = new ArrayList<>();
        findHintsRecursive(element, parent, result);

        return result;
    }

    private void findHintsRecursive(IJavaElement method, IJavaElement parent, List<MethodParameters> result) throws JavaModelException {
        findHints(method, parent, result);

        IType type = (IType)parent;
        ITypeHierarchy typeHierarchy = type.newTypeHierarchy(new NullProgressMonitor());
        IType[] superTypes = typeHierarchy.getAllSupertypes(type);

        for (IType iType : superTypes) {
            findHintsRecursive(method, iType, result);
        }
    }

    private void findHints(IJavaElement method, IJavaElement parent, List<MethodParameters> result) throws JavaModelException {
        String methodName = method.getElementName();

        for (IMethod iMethod : ((IType)parent).getMethods()) {
            int methodFlag = iMethod.getFlags();
            if (Flags.isPrivate(methodFlag) || !methodName.equals(iMethod.getElementName())) {
                continue;
            }

            MethodParameters methodParameters = DtoFactory.newDto(MethodParameters.class);

            String parameters = getMethodParametersAsString(iMethod);

            methodParameters.setMethodName(methodName);
            methodParameters.setParameters(parameters);

            if (!result.contains(methodParameters)) {
                result.add(methodParameters);
            }
        }
    }

    private IJavaElement getSelectedElement(IType type, int offset) throws JavaModelException {
        ICompilationUnit compilationUnit = type.getCompilationUnit();
        IJavaElement[] javaElements = compilationUnit.codeSelect(offset, 0);

        if (javaElements.length == 0) {
            return null;
        }

        return javaElements[0];
    }

    private String getMethodParametersAsString(IMethod method) throws JavaModelException {
        ILocalVariable[] parameters = method.getParameters();

        Map<String, String> parametersMap = new LinkedHashMap<>();

        for (ILocalVariable parameter : parameters) {
            parametersMap.put(getParameterType(parameter.getTypeSignature()), parameter.getElementName());
        }

        return getParametersAsString(parametersMap);
    }

    private String getParameterType(String typeSignature) {
        String type;
        if (typeSignature.contains("<")) {
            type = Signature.getSimpleName(typeSignature);
        } else {
            type = Signature.getSignatureSimpleName(typeSignature);
        }

        return type.replaceAll("[;|*]*", "");
    }

    private String getParametersAsString(Map<String, String> parameters) {
        StringBuilder builder = new StringBuilder();

        int size = parameters.size();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String type = entry.getKey();
            String name = entry.getValue();

            builder.append(type).append(' ').append(name);

            size--;

            if (size > 0) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }
}
