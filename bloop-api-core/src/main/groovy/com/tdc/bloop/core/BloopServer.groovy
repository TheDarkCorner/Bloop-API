package com.tdc.bloop.core

import com.esotericsoftware.kryonet.Server
import com.tdc.bloop.utilities.BloopAuditor
import groovy.transform.CompileStatic

/**
 * Represents the server that the BloopClients will connect to.
 * The Bloop Listener Service automatically identifies if the current
 * application instance will be the server or the client.
 *
 * @since 1.0
 * @author Ian Gabriel Sanchez
 */
@CompileStatic
class BloopServer extends Server {

    BloopSettings settings
    private boolean initialized = false

    /**
     * Initializes a BloopServer instance with the provided BloopSettings.
     * @param settings The BloopSettings that contains all the required parameters for Blooping.
     */
    BloopServer( BloopSettings settings ) {
        super()
        this.settings = settings
        this.initializeComponents()
    }

    private void initializeComponents() {
        try {
            this.start()
            this.bind( settings.hostPort )
            BloopAuditor.registerClasses( this.kryo )
            this.addListener( new BloopDefaultListeners() )
            initialized = true
        }
        catch( Exception ex ) {
            //TODO: Add exception logger
            initialized = false
        }
    }

    /**
     * Classes that are going to be sent over the network should be first registered
     * on both client and server. This allows them to be serialized by the
     * Kryo Serialization Library
     * @see <a href="https://github.com/EsotericSoftware/kryo">Kryo Serialization Library</a>
     * @param dataTypes The datatypes that need to be registered.
     */
    void registerClasses( Class[] dataTypes ) {
        BloopAuditor.registerClasses( this.kryo, dataTypes )
    }

    /**
     * Checks if the current BloopServer instance is initialized.
     * @return The initialized status of the BloopServer
     */
    boolean isInitialized() {
        return initialized
    }

}
