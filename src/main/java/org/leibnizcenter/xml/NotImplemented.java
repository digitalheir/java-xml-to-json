package org.leibnizcenter.xml;

import org.w3c.dom.Node;

/**
 * Created by maarten on 21-12-15.
 */
public class NotImplemented extends Exception {
    public NotImplemented() {
        this("No implemented means to handle tag, \n" + "  Send a message to the developer.");
    }

    public NotImplemented(String message) {
        super(message);
    }

    public NotImplemented(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplemented(Throwable cause) {
        super(cause);
    }

    public NotImplemented(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotImplemented(Node n) {
        this("No implemented means to handle tag: \n"
                + n.toString() + " \n\n Send a message to the developer.");
    }
}
