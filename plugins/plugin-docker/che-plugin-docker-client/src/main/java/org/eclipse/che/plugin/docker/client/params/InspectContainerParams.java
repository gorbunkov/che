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
package org.eclipse.che.plugin.docker.client.params;

/**
 * Arguments holder for{@link org.eclipse.che.plugin.docker.client.DockerConnector#inspectContainer(InspectContainerParams)}.
 *
 * @author Mykola Morhun
 */
public class InspectContainerParams {

    private String  container;
    private Boolean returnContainerSize;

    /**
     * @param container
     *         id or name of container
     */
    public InspectContainerParams withContainer(String container) {
        this.container = container;
        return this;
    }

    /**
     * @param getContainerSize
     *         if {@code true} it will return container size information
     */
    public InspectContainerParams withReturnContainerSize(boolean getContainerSize) {
        this.returnContainerSize = getContainerSize;
        return this;
    }

    public String getContainer() {
        return container;
    }

    public Boolean isGetContainerSize() {
        return returnContainerSize;
    }

}
